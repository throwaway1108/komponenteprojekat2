package com.raf.gamingsessionservice.repository;

import com.raf.gamingsessionservice.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}
