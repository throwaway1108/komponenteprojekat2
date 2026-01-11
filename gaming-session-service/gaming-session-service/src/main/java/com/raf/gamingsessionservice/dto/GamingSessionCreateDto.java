package com.raf.gamingsessionservice.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class GamingSessionCreateDto {
    @NotBlank
    private String sessionName;

    @NotNull
    private Long gameId;

    @NotNull
    private Integer maxPlayers;

    @NotNull
    private String sessionType;

    @NotNull
    private LocalDateTime startDateTime;

    private String description;

    @NotNull
    private Long organizerId;

    public @NotBlank String getSessionName() {
        return sessionName;
    }

    public @NotNull Long getGameId() {
        return gameId;
    }

    public @NotNull Integer getMaxPlayers() {
        return maxPlayers;
    }

    public @NotNull String getSessionType() {
        return sessionType;
    }

    public @NotNull LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public String getDescription() {
        return description;
    }

    public @NotNull Long getOrganizerId() {
        return organizerId;
    }

    public void setSessionName(@NotBlank String sessionName) {
        this.sessionName = sessionName;
    }

    public void setGameId(@NotNull Long gameId) {
        this.gameId = gameId;
    }

    public void setMaxPlayers(@NotNull Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public void setSessionType(@NotNull String sessionType) {
        this.sessionType = sessionType;
    }

    public void setStartDateTime(@NotNull LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOrganizerId(@NotNull Long organizerId) {
        this.organizerId = organizerId;
    }
}