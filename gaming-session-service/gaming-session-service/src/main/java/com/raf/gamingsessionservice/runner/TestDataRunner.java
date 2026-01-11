package com.raf.gamingsessionservice.runner;

import com.raf.gamingsessionservice.domain.Game;
import com.raf.gamingsessionservice.repository.GameRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile({"default"})
@Component
public class TestDataRunner implements CommandLineRunner {

    private GameRepository gameRepository;

    public TestDataRunner(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        gameRepository.save(new Game("League of Legends", "MOBA game", "MOBA"));
        gameRepository.save(new Game("Counter Strike", "FPS game", "Shooter"));
        gameRepository.save(new Game("Valorant", "Tactical Shooter", "Shooter"));

        System.out.println("Baza je popunjena igrama preko Runner-a.");
    }
}