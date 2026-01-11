package com.raf.notificationservice.service;

import com.raf.notificationservice.dto.NotificationTypeCreateDto;
import com.raf.notificationservice.dto.NotificationTypeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationTypeService {
    Page<NotificationTypeDto> findAll(Pageable pageable);
    NotificationTypeDto create(NotificationTypeCreateDto dto);
    NotificationTypeDto update(Long id, NotificationTypeCreateDto dto);
    void deleteById(Long id);
}