package com.raf.notificationservice.domain;

import javax.persistence.*;

@Entity
public class NotificationType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String typeName;

    private String subject;

    @Column(length = 1000)
    private String template;

    public NotificationType() {}

    public NotificationType(String typeName, String subject, String template) {
        this.typeName = typeName;
        this.subject = subject;
        this.template = template;
    }

    public Long getId() {
        return id;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getSubject() {
        return subject;
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

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}