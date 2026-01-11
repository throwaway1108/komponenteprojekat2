package com.raf.gamingsessionservice.dto;

import javax.validation.constraints.NotNull;

public class InviteToSessionDto {
    @NotNull
    private Long userId;

    public @NotNull Long getUserId() {
        return userId;
    }
    public void setUserId(@NotNull Long userId) {
        this.userId = userId;
    }
}