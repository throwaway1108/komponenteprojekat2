package com.raf.gamingsessionservice.dto;

public class SendNotificationDto {
    private String email;
    private String type;
    private String parameters;
    private Long userId;

    public SendNotificationDto() {
    }

    public SendNotificationDto(String email, String type, String parameters, Long userId) {
        this.email = email;
        this.type = type;
        this.parameters = parameters;
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // Dobra praksa je imati toString kada se radi sa MessageHelper-om radi lakšeg debug-a
    @Override
    public String toString() {
        return "SendNotificationDto{" +
                "email='" + email + '\'' +
                ", type='" + type + '\'' +
                ", parameters='" + parameters + '\'' +
                ", userId=" + userId +
                '}';
    }
}