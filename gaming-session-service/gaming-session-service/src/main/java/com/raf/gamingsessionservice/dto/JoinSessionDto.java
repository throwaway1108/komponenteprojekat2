package com.raf.gamingsessionservice.dto;

import javax.validation.constraints.NotNull;

public class JoinSessionDto {
    @NotNull
    private Long userId;

    private String invitationToken;

    public @NotNull Long getUserId() {
        return userId;
    }

    public String getInvitationToken() {
        return invitationToken;
    }

    public void setUserId(@NotNull Long userId) {
        this.userId = userId;
    }

    public void setInvitationToken(String invitationToken) {
        this.invitationToken = invitationToken;
    }
}