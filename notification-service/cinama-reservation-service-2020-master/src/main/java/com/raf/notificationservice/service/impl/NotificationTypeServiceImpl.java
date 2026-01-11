package com.raf.notificationservice.service.impl;

import com.raf.notificationservice.domain.NotificationType;
import com.raf.notificationservice.dto.NotificationTypeCreateDto;
import com.raf.notificationservice.dto.NotificationTypeDto;
import com.raf.notificationservice.exception.NotFoundException;
import com.raf.notificationservice.repository.NotificationTypeRepository;
import com.raf.notificationservice.service.NotificationTypeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NotificationTypeServiceImpl implements NotificationTypeService {

    private NotificationTypeRepository notificationTypeRepository;

    public NotificationTypeServiceImpl(NotificationTypeRepository notificationTypeRepository) {
        this.notificationTypeRepository = notificationTypeRepository;
    }

    @Override
    public Page<NotificationTypeDto> findAll(Pageable pageable) {
        return notificationTypeRepository.findAll(pageable)
                .map(type -> {
                    NotificationTypeDto dto = new NotificationTypeDto();
                    dto.setId(type.getId());
                    dto.setTypeName(type.getTypeName());
                    dto.setTemplate(type.getTemplate());
                    return dto;
                });
    }

    @Override
    public NotificationTypeDto create(NotificationTypeCreateDto dto) {
        NotificationType type = new NotificationType();
        type.setTypeName(dto.getTypeName());
        type.setTemplate(dto.getTemplate());
        notificationTypeRepository.save(type);

        NotificationTypeDto result = new NotificationTypeDto();
        result.setId(type.getId());
        result.setTypeName(type.getTypeName());
        result.setTemplate(type.getTemplate());
        return result;
    }

    @Override
    public NotificationTypeDto update(Long id, NotificationTypeCreateDto dto) {
        NotificationType type = notificationTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Notification type not found"));

        type.setTypeName(dto.getTypeName());
        type.setTemplate(dto.getTemplate());
        notificationTypeRepository.save(type);

        NotificationTypeDto result = new NotificationTypeDto();
        result.setId(type.getId());
        result.setTypeName(type.getTypeName());
        result.setTemplate(type.getTemplate());
        return result;
    }

    @Override
    public void deleteById(Long id) {
        notificationTypeRepository.deleteById(id);
    }
}