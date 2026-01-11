package com.raf.gamingsessionservice.service.impl;

import com.raf.gamingsessionservice.domain.Game;
import com.raf.gamingsessionservice.dto.GameCreateDto;
import com.raf.gamingsessionservice.dto.GameDto;
import com.raf.gamingsessionservice.exception.NotFoundException;
import com.raf.gamingsessionservice.mapper.GameMapper;
import com.raf.gamingsessionservice.repository.GameRepository;
import com.raf.gamingsessionservice.service.GameService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {

    private GameRepository gameRepository;
    private GameMapper gameMapper;

    public GameServiceImpl(GameRepository gameRepository, GameMapper gameMapper) {
        this.gameRepository = gameRepository;
        this.gameMapper = gameMapper;
    }

    @Override
    public Page<GameDto> findAll(Pageable pageable) {
        return gameRepository.findAll(pageable)
                .map(gameMapper::gameToGameDto);
    }

    @Override
    public GameDto findById(Long id) {
        return gameRepository.findById(id)
                .map(gameMapper::gameToGameDto)
                .orElseThrow(() -> new NotFoundException("Game not found"));
    }

    @Override
    public GameDto add(GameCreateDto gameCreateDto) {
        Game game = gameMapper.gameCreateDtoToGame(gameCreateDto);
        gameRepository.save(game);
        return gameMapper.gameToGameDto(game);
    }

    @Override
    public GameDto update(Long id, GameCreateDto gameCreateDto) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Game not found"));
        game.setName(gameCreateDto.getName());
        game.setDescription(gameCreateDto.getDescription());
        game.setGenre(gameCreateDto.getGenre());
        gameRepository.save(game);
        return gameMapper.gameToGameDto(game);
    }

    @Override
    public void deleteById(Long id) {
        gameRepository.deleteById(id);
    }
}