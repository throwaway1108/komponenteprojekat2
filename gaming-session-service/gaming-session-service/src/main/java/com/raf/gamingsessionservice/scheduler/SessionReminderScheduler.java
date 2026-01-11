package com.raf.gamingsessionservice.scheduler;

import com.raf.gamingsessionservice.domain.GamingSession;
import com.raf.gamingsessionservice.domain.SessionStatus;
import com.raf.gamingsessionservice.dto.SendNotificationDto;
import com.raf.gamingsessionservice.dto.UserDto;
import com.raf.gamingsessionservice.repository.GamingSessionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import com.raf.gamingsessionservice.listener.helper.MessageHelper;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class SessionReminderScheduler {

    private final GamingSessionRepository sessionRepository;
    private final JmsTemplate jmsTemplate;
    private final RestTemplate userServiceRestTemplate;
    private final String sendNotificationDestination;
    private final MessageHelper messageHelper;

    public SessionReminderScheduler(GamingSessionRepository sessionRepository,
                                    JmsTemplate jmsTemplate,
                                    RestTemplate userServiceRestTemplate,
                                    @Value("${destination.sendNotification}") String sendNotificationDestination,
                                    MessageHelper messageHelper) {
        this.sessionRepository = sessionRepository;
        this.jmsTemplate = jmsTemplate;
        this.userServiceRestTemplate = userServiceRestTemplate;
        this.sendNotificationDestination = sendNotificationDestination;
        this.messageHelper = messageHelper;
    }

    @Scheduled(fixedRate = 300000) // Pokreće se svakih 5 minuta
    @Transactional
    public void sendReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneHourLater = now.plusHours(1);

        List<GamingSession> upcomingSessions = sessionRepository.findSessionsStartingSoon(
                SessionStatus.SCHEDULED,
                now,
                oneHourLater
        );

        for (GamingSession session : upcomingSessions) {
            for (Long participantId : session.getParticipantIds()) {
                try {
                    UserDto user = userServiceRestTemplate.getForObject("/user/" + participantId, UserDto.class);

                    if (user != null && user.getEmail() != null) {
                        SendNotificationDto notification = new SendNotificationDto(
                                user.getEmail(),
                                "SESSION_REMINDER",
                                session.getSessionName(),
                                user.getId()
                        );

                        jmsTemplate.convertAndSend(sendNotificationDestination, messageHelper.createTextMessage(notification));
                    }
                } catch (Exception e) {
                    System.err.println("Greška pri slanju podsetnika za korisnika " + participantId + ": " + e.getMessage());
                }
            }
        }
    }
}