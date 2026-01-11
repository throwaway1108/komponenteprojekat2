package com.raf.gamingsessionservice.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class GamingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sessionName;

    @ManyToOne(optional = false)
    private Game game;

    private Integer maxPlayers;

    @Enumerated(EnumType.STRING)
    private SessionType sessionType;

    private LocalDateTime startDateTime;
    private String description;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    private Long organizerId;

    private Integer currentPlayerCount = 0;

    @ElementCollection
    @CollectionTable(name = "session_participants")
    private Set<Long> participantIds = new HashSet<>();

    public GamingSession() {}

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

    public SessionType getSessionType() {
        return sessionType;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public SessionStatus getStatus() {
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

    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(SessionStatus status) {
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