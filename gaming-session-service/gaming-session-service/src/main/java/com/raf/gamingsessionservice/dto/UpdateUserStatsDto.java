package com.raf.gamingsessionservice.dto;

public class UpdateUserStatsDto {
    private Long userId;
    private Boolean attended;

    public UpdateUserStatsDto() {}

    public UpdateUserStatsDto(Long userId, Boolean attended) {
        this.userId = userId;
        this.attended = attended;
    }

    public Long getUserId() {
        return userId;
    }

    public Boolean getAttended() {
        return attended;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setAttended(Boolean attended) {
        this.attended = attended;
    }
}