package com.raf.gaminggui.model;

import java.time.LocalDateTime;
import java.util.Set;

public class GamingSession {
    private Long id;
    private String sessionName;
    private Game game;
    private Integer maxPlayers;
    private String sessionType;
    private LocalDateTime startDateTime;
    private String description;
    private String status;
    private Long organizerId;
    private Set<Long> participantIds;
    private Integer currentPlayerCount;

    public Long getId() {
        return id;
    }

    public String getSessionName() {
        return sessionName;
    }

    public Game getGame() {
        return game;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public String getSessionType() {
        return sessionType;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public Long getOrganizerId() {
        return organizerId;
    }

    public Set<Long> getParticipantIds() {
        return participantIds;
    }

    public Integer getCurrentPlayerCount() {
        return currentPlayerCount;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOrganizerId(Long organizerId) {
        this.organizerId = organizerId;
    }

    public void setParticipantIds(Set<Long> participantIds) {
        this.participantIds = participantIds;
    }

    public void setCurrentPlayerCount(Integer currentPlayerCount) {
        this.currentPlayerCount = currentPlayerCount;
    }
}