package com.raf.gamingsessionservice.mapper;

import com.raf.gamingsessionservice.domain.Game;
import com.raf.gamingsessionservice.dto.GameCreateDto;
import com.raf.gamingsessionservice.dto.GameDto;
import org.springframework.stereotype.Component;

@Component
public class GameMapper {

    public GameDto gameToGameDto(Game game) {
        GameDto dto = new GameDto();
        dto.setId(game.getId());
        dto.setName(game.getName());
        dto.setDescription(game.getDescription());
        dto.setGenre(game.getGenre());
        return dto;
    }

    public Game gameCreateDtoToGame(GameCreateDto dto) {
        Game game = new Game();
        game.setName(dto.getName());
        game.setDescription(dto.getDescription());
        game.setGenre(dto.getGenre());
        return game;
    }
}