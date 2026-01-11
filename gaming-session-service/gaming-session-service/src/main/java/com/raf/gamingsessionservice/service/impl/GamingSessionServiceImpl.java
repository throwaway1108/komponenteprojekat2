package com.raf.gamingsessionservice.service.impl;

import com.raf.gamingsessionservice.domain.*;
import com.raf.gamingsessionservice.dto.*;
import com.raf.gamingsessionservice.exception.NotFoundException;
import com.raf.gamingsessionservice.listener.helper.MessageHelper;
import com.raf.gamingsessionservice.mapper.GamingSessionMapper;
import com.raf.gamingsessionservice.repository.GamingSessionRepository;
import com.raf.gamingsessionservice.repository.InvitationRepository;
import com.raf.gamingsessionservice.service.GamingSessionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@Transactional
public class GamingSessionServiceImpl implements GamingSessionService {

    private final InvitationRepository invitationRepository;
    private final GamingSessionRepository sessionRepository;
    private final GamingSessionMapper sessionMapper;
    private final JmsTemplate jmsTemplate;
    private final MessageHelper messageHelper;
    private final RestTemplate userServiceRestTemplate;

    @Value("${destination.sendNotification}")
    private String sendNotificationDestination;

    public GamingSessionServiceImpl(InvitationRepository invitationRepository,
                                    GamingSessionRepository sessionRepository,
                                    GamingSessionMapper sessionMapper,
                                    JmsTemplate jmsTemplate,
                                    MessageHelper messageHelper,
                                    RestTemplate userServiceRestTemplate) {
        this.invitationRepository = invitationRepository;
        this.sessionRepository = sessionRepository;
        this.sessionMapper = sessionMapper;
        this.jmsTemplate = jmsTemplate;
        this.messageHelper = messageHelper;
        this.userServiceRestTemplate = userServiceRestTemplate;
    }

    @Override
    public Page<GamingSessionDto> findAll(Long gameId, String sessionType, Integer maxPlayers,
                                          String description, Long userId, Pageable pageable) {
        if (userId != null) {
            return sessionRepository.findAllByUserId(userId, pageable)
                    .map(sessionMapper::gamingSessionToDto);
        }

        return sessionRepository.findAll(pageable)
                .map(sessionMapper::gamingSessionToDto);
    }

    @Override
    public GamingSessionDto findById(Long id) {
        return sessionRepository.findById(id)
                .map(sessionMapper::gamingSessionToDto)
                .orElseThrow(() -> new NotFoundException(String.format("Session %d not found.", id)));
    }

    @Override
    @Retryable(value = { RestClientException.class }, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public GamingSessionDto create(GamingSessionCreateDto dto) {
        ResponseEntity<CheckUserEligibilityDto> response = userServiceRestTemplate.getForEntity(
                "/user/" + dto.getOrganizerId() + "/check-eligibility",
                CheckUserEligibilityDto.class
        );

        CheckUserEligibilityDto eligibility = response.getBody();
        if (eligibility == null || !eligibility.getEligible()) {
            SendNotificationDto rejectNotif = new SendNotificationDto("user@email.com", "REJECT_SESSION_CREATION", "Low attendance", dto.getOrganizerId());
            jmsTemplate.convertAndSend(sendNotificationDestination, messageHelper.createTextMessage(rejectNotif));

            throw new RuntimeException("User not eligible: " + (eligibility != null ? eligibility.getReason() : "Unknown"));
        }

        GamingSession session = sessionMapper.gamingSessionCreateDtoToSession(dto);
        session.setStatus(SessionStatus.SCHEDULED);
        session.setCurrentPlayerCount(1); // Organizator je prvi igrač
        session = sessionRepository.save(session);

        return sessionMapper.gamingSessionToDto(session);
    }

    @Override
    public void joinSession(Long sessionId, JoinSessionDto dto) {
        GamingSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Session not found"));

        if (session.getStatus() != SessionStatus.SCHEDULED) throw new RuntimeException("Session not active");
        if (session.getCurrentPlayerCount() >= session.getMaxPlayers()) throw new RuntimeException("Full");
        if (session.getParticipantIds().contains(dto.getUserId())) throw new RuntimeException("Already joined");

        ResponseEntity<CheckUserEligibilityDto> eligibilityResp = userServiceRestTemplate.getForEntity(
                "/user/" + dto.getUserId() + "/check-eligibility", CheckUserEligibilityDto.class);

        if (eligibilityResp.getBody() == null || !eligibilityResp.getBody().getEligible()) {
            throw new RuntimeException("User blocked or ineligible");
        }

        // SINHRONO: Povećaj broj prijavljenih u User servisu
        userServiceRestTemplate.postForEntity("/user/" + dto.getUserId() + "/increment-registered", null, Void.class);

        session.getParticipantIds().add(dto.getUserId());
        session.setCurrentPlayerCount(session.getCurrentPlayerCount() + 1); // ISPRAVKA: Povećaj brojač
        sessionRepository.save(session);

        SendNotificationDto notification = new SendNotificationDto("user@email.com", "JOIN_CONFIRMATION", session.getSessionName(), dto.getUserId());
        jmsTemplate.convertAndSend(sendNotificationDestination, messageHelper.createTextMessage(notification));
    }

    @Override
    public void cancelSession(Long sessionId, CancelSessionDto dto) {
        GamingSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Session not found"));

        if (!session.getOrganizerId().equals(dto.getUserId())) throw new RuntimeException("Only organizer can cancel");

        session.setStatus(SessionStatus.CANCELLED);
        sessionRepository.save(session);

        for (Long participantId : session.getParticipantIds()) {
            SendNotificationDto notif = new SendNotificationDto("user@email.com", "SESSION_CANCELLED", session.getSessionName(), participantId);
            jmsTemplate.convertAndSend(sendNotificationDestination, messageHelper.createTextMessage(notif));
        }
    }

    @Override
    @Transactional
    @Retryable(value = { Exception.class }, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public void completeSession(Long sessionId, CompleteSessionDto dto) {
        GamingSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Session not found"));

        if (!session.getOrganizerId().equals(dto.getOrganizerId())) {
            throw new RuntimeException("Only organizer can complete session");
        }

        session.setStatus(SessionStatus.COMPLETED);
        sessionRepository.save(session);

        // SINHRONO: Povećaj "Attended" za sve učesnike
        for (Long userId : session.getParticipantIds()) {
            UpdateUserStatsDto stats = new UpdateUserStatsDto(userId, true);
            userServiceRestTemplate.postForEntity("/user/update-stats", stats, Void.class);
        }

        // SINHRONO: Povećaj "Organized" za organizatora
        userServiceRestTemplate.postForEntity("/user/" + dto.getOrganizerId() + "/increment-organized", null, Void.class);
    }

    @Override
    public void inviteToSession(Long sessionId, InviteToSessionDto dto) {
        GamingSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Session not found"));

        String token = UUID.randomUUID().toString();
        Invitation invitation = new Invitation(sessionId, dto.getUserId(), token);
        invitationRepository.save(invitation);

        SendNotificationDto notification = new SendNotificationDto("user@email.com", "SESSION_INVITATION", token, dto.getUserId());
        jmsTemplate.convertAndSend(sendNotificationDestination, messageHelper.createTextMessage(notification));
    }

    @Override
    @Transactional
    @Retryable(value = { Exception.class }, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public void leaveSession(Long sessionId, Long userId) {
        GamingSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Session not found"));

        if (!session.getParticipantIds().contains(userId)) {
            throw new RuntimeException("User is not a participant of this session");
        }

        session.getParticipantIds().remove(userId);
        session.setCurrentPlayerCount(session.getCurrentPlayerCount() - 1);
        sessionRepository.save(session);

        UpdateUserStatsDto stats = new UpdateUserStatsDto(userId, false);
        userServiceRestTemplate.postForEntity("/user/update-stats", stats, Void.class);

        SendNotificationDto notification = new SendNotificationDto("org@email.com", "USER_LEFT", "Player left: " + userId, session.getOrganizerId());
        jmsTemplate.convertAndSend(sendNotificationDestination, messageHelper.createTextMessage(notification));
    }
}