package com.raf.gamingsessionservice.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sessionId;
    private Long invitedUserId;
    private String invitationToken;
    private LocalDateTime createdAt;
    private Boolean used = false;

    public Invitation() {}

    public Invitation(Long sessionId, Long invitedUserId, String invitationToken) {
        this.sessionId = sessionId;
        this.invitedUserId = invitedUserId;
        this.invitationToken = invitationToken;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public Long getInvitedUserId() {
        return invitedUserId;
    }

    public String getInvitationToken() {
        return invitationToken;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Boolean getUsed() {
        return used;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public void setInvitedUserId(Long invitedUserId) {
        this.invitedUserId = invitedUserId;
    }

    public void setInvitationToken(String invitationToken) {
        this.invitationToken = invitationToken;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }
}