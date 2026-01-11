package com.raf.gamingsessionservice.dto;

public class CheckUserEligibilityDto {
    private Boolean eligible;
    private String reason;

    public Boolean getEligible() {
        return eligible;
    }

    public String getReason() {
        return reason;
    }

    public void setEligible(Boolean eligible) {
        this.eligible = eligible;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}