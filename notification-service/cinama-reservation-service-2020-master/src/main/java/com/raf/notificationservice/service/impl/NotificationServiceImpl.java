package com.raf.notificationservice.service.impl;

import com.raf.notificationservice.domain.Notification;
import com.raf.notificationservice.domain.NotificationType;
import com.raf.notificationservice.dto.NotificationDto;
import com.raf.notificationservice.dto.NotificationTypeDto;
import com.raf.notificationservice.dto.SendNotificationDto;
import com.raf.notificationservice.repository.NotificationRepository;
import com.raf.notificationservice.repository.NotificationTypeRepository;
import com.raf.notificationservice.service.EmailService;
import com.raf.notificationservice.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private NotificationRepository notificationRepository;
    private NotificationTypeRepository notificationTypeRepository;
    private EmailService emailService;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   NotificationTypeRepository notificationTypeRepository,
                                   EmailService emailService) {
        this.notificationRepository = notificationRepository;
        this.notificationTypeRepository = notificationTypeRepository;
        this.emailService = emailService;
    }

    @Override
    public Page<NotificationDto> findAll(Pageable pageable) {
        return notificationRepository.findAll(pageable)
                .map(this::mapToDto);
    }

    @Override
    public Page<NotificationDto> findByUserId(Long userId, Pageable pageable) {
        return notificationRepository.findByUserId(userId, pageable)
                .map(this::mapToDto);
    }

    @Override
    public void processNotification(SendNotificationDto dto) {
        NotificationType type = notificationTypeRepository.findByTypeName(dto.getNotificationType())
                .orElseThrow(() -> new RuntimeException("Tip nije pronađen"));

        String finalBody = String.format(type.getTemplate(), dto.getParameters());
        emailService.sendSimpleMessage(dto.getEmail(), type.getSubject(), finalBody);

        Notification archive = new Notification(dto.getEmail(), type, finalBody, dto.getUserId());
        notificationRepository.save(archive);
    }

    private NotificationDto mapToDto(Notification notification) {
        NotificationDto dto = new NotificationDto();
        dto.setId(notification.getId());
        dto.setRecipientEmail(notification.getRecipientEmail());
        dto.setContent(notification.getContent());
        dto.setSentAt(notification.getSentAt());
        dto.setUserId(notification.getUserId());

        NotificationTypeDto typeDto = new NotificationTypeDto();
        typeDto.setId(notification.getNotificationType().getId());
        typeDto.setTypeName(notification.getNotificationType().getTypeName());
        typeDto.setTemplate(notification.getNotificationType().getTemplate());
        dto.setNotificationTypeDto(typeDto);

        return dto;
    }
}