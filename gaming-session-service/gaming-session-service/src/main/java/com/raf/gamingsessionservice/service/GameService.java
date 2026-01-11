package com.raf.gamingsessionservice.service;

import com.raf.gamingsessionservice.dto.GameCreateDto;
import com.raf.gamingsessionservice.dto.GameDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GameService {
    Page<GameDto> findAll(Pageable pageable);
    GameDto findById(Long id);
    GameDto add(GameCreateDto gameCreateDto);
    GameDto update(Long id, GameCreateDto gameCreateDto);
    void deleteById(Long id);
}