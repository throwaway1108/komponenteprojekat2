package com.raf.gaminguserservice.dto;

public class IncrementOrganizedSessionsDto {
    private Long userId;

    public IncrementOrganizedSessionsDto() {}

    public IncrementOrganizedSessionsDto(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}