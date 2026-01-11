package com.raf.gaminggui.model;

import java.time.LocalDate;

public class User {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String username;
    private LocalDate dateOfBirth;
    private Boolean isActive;
    private Boolean isBlocked;
    private Integer totalRegisteredSessions;
    private Integer totalAttendedSessions;
    private Integer totalAbandonedSessions;
    private Double attendancePercentage;
    private Integer successfullyOrganizedSessions;
    private String organizerTitle;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Boolean getActive() {
        return isActive;
    }

    public Boolean getBlocked() {
        return isBlocked;
    }

    public Integer getTotalRegisteredSessions() {
        return totalRegisteredSessions;
    }

    public Integer getTotalAttendedSessions() {
        return totalAttendedSessions;
    }

    public Integer getTotalAbandonedSessions() {
        return totalAbandonedSessions;
    }

    public Double getAttendancePercentage() {
        return attendancePercentage;
    }

    public Integer getSuccessfullyOrganizedSessions() {
        return successfullyOrganizedSessions;
    }

    public String getOrganizerTitle() {
        return organizerTitle;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public void setBlocked(Boolean blocked) {
        isBlocked = blocked;
    }

    public void setTotalRegisteredSessions(Integer totalRegisteredSessions) {
        this.totalRegisteredSessions = totalRegisteredSessions;
    }

    public void setTotalAttendedSessions(Integer totalAttendedSessions) {
        this.totalAttendedSessions = totalAttendedSessions;
    }

    public void setTotalAbandonedSessions(Integer totalAbandonedSessions) {
        this.totalAbandonedSessions = totalAbandonedSessions;
    }

    public void setAttendancePercentage(Double attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }

    public void setSuccessfullyOrganizedSessions(Integer successfullyOrganizedSessions) {
        this.successfullyOrganizedSessions = successfullyOrganizedSessions;
    }

    public void setOrganizerTitle(String organizerTitle) {
        this.organizerTitle = organizerTitle;
    }
}