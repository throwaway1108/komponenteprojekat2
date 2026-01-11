package com.raf.notificationservice.dto;

public class SendNotificationDto {
    private String email;
    private String notificationType;
    private String parameters;
    private Long userId;

    public SendNotificationDto() {}

    public SendNotificationDto(String email, String notificationType, String parameters, Long userId) {
        this.email = email;
        this.notificationType = notificationType;
        this.parameters = parameters;
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public String getParameters() {
        return parameters;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }
}