package com.raf.notificationservice.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipientEmail;

    @ManyToOne(optional = false)
    private NotificationType notificationType;

    @Column(length = 2000)
    private String content;

    private LocalDateTime sentAt;

    private Long userId;

    public Notification() {}

    public Notification(String recipientEmail, NotificationType notificationType,
                        String content, Long userId) {
        this.recipientEmail = recipientEmail;
        this.notificationType = notificationType;
        this.content = content;
        this.userId = userId;
        this.sentAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public NotificationType getNotificationType() {
        return notificationType;
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

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
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