package com.raf.notificationservice.dto;

import javax.validation.constraints.NotBlank;

public class NotificationTypeCreateDto {
    @NotBlank
    private String typeName;

    @NotBlank
    private String template;

    public @NotBlank String getTypeName() {
        return typeName;
    }

    public @NotBlank String getTemplate() {
        return template;
    }

    public void setTypeName(@NotBlank String typeName) {
        this.typeName = typeName;
    }

    public void setTemplate(@NotBlank String template) {
        this.template = template;
    }
}