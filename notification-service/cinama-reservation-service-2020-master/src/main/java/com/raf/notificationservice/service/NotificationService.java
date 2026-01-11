package com.raf.notificationservice.service;

import com.raf.notificationservice.dto.NotificationDto;
import com.raf.notificationservice.dto.SendNotificationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    Page<NotificationDto> findAll(Pageable pageable);
    Page<NotificationDto> findByUserId(Long userId, Pageable pageable);
    void processNotification(SendNotificationDto dto);
}