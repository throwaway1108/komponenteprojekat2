package com.raf.gamingsessionservice.dto;

import javax.validation.constraints.NotBlank;

public class GameCreateDto {
    @NotBlank
    private String name;
    private String description;
    private String genre;

    public @NotBlank String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getGenre() {
        return genre;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}