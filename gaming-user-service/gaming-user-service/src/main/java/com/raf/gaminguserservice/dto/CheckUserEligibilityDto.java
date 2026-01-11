package com.raf.gaminguserservice.dto;

public class CheckUserEligibilityDto {
    private Boolean eligible;
    private String reason;

    public CheckUserEligibilityDto() {}

    public CheckUserEligibilityDto(Boolean eligible, String reason) {
        this.eligible = eligible;
        this.reason = reason;
    }

    public Boolean getEligible() {
        return eligible;
    }

    public void setEligible(Boolean eligible) {
        this.eligible = eligible;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}