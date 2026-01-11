package com.raf.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class NotificationDto {
    private Long id;
    private String recipientEmail;

    @JsonProperty("notificationType")
    private NotificationTypeDto notificationTypeDto;

    private String content;
    private LocalDateTime sentAt;
    private Long userId;

    public Long getId() {
        return id;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public NotificationTypeDto getNotificationTypeDto() {
        return notificationTypeDto;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public void setNotificationTypeDto(NotificationTypeDto notificationTypeDto) {
        this.notificationTypeDto = notificationTypeDto;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}