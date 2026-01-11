package com.raf.gamingsessionservice.domain;

import javax.persistence.*;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String genre;

    public Game() {}

    public Game(String name, String description, String genre) {
        this.name = name;
        this.description = description;
        this.genre = genre;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getGenre() {
        return genre;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}