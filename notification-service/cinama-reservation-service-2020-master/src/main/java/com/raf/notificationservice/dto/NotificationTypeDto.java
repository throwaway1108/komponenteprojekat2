package com.raf.notificationservice.dto;

public class NotificationTypeDto {
    private Long id;
    private String typeName;
    private String template;

    public Long getId() {
        return id;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getTemplate() {
        return template;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}