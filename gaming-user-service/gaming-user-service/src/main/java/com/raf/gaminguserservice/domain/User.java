package com.raf.gaminguserservice.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(indexes = {@Index(columnList = "username", unique = true), @Index(columnList = "email", unique = true)})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private LocalDate dateOfBirth;
    @ManyToOne(optional = false)
    private Role role;
    private Boolean isActive = false;
    private Boolean isBlocked = false;

    private Integer totalRegisteredSessions = 0;
    private Integer totalAttendedSessions = 0;
    private Integer totalAbandonedSessions = 0;
    private Double attendancePercentage = 100.0;
    private Integer successfullyOrganizedSessions = 0;
    private String organizerTitle = "Nema titule";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    public Integer getTotalAbandonedSessions() {
        return totalAbandonedSessions;
    }

    public Integer getTotalAttendedSessions() {
        return totalAttendedSessions;
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

    public void setOrganizerTitle(String organizerTitle) {
        this.organizerTitle = organizerTitle;
    }

    public void setSuccessfullyOrganizedSessions(Integer successfullyOrganizedSessions) {
        this.successfullyOrganizedSessions = successfullyOrganizedSessions;
    }

}
