package com.raf.gamingsessionservice.dto;

import javax.validation.constraints.NotNull;
import java.util.Set;

public class CompleteSessionDto {
    @NotNull
    private Long organizerId;

    @NotNull
    private Set<Long> attendedUserIds;

    public @NotNull Long getOrganizerId() {
        return organizerId;
    }

    public @NotNull Set<Long> getAttendedUserIds() {
        return attendedUserIds;
    }

    public void setOrganizerId(@NotNull Long organizerId) {
        this.organizerId = organizerId;
    }

    public void setAttendedUserIds(@NotNull Set<Long> attendedUserIds) {
        this.attendedUserIds = attendedUserIds;
    }
}