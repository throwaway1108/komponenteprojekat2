package com.raf.gamingsessionservice.mapper;

import com.raf.gamingsessionservice.domain.GamingSession;
import com.raf.gamingsessionservice.domain.SessionStatus;
import com.raf.gamingsessionservice.domain.SessionType;
import com.raf.gamingsessionservice.dto.GamingSessionCreateDto;
import com.raf.gamingsessionservice.dto.GamingSessionDto;
import com.raf.gamingsessionservice.exception.NotFoundException;
import com.raf.gamingsessionservice.repository.GameRepository;
import org.springframework.stereotype.Component;

@Component
public class GamingSessionMapper {

    private GameRepository gameRepository;
    private GameMapper gameMapper;

    public GamingSessionMapper(GameRepository gameRepository, GameMapper gameMapper) {
        this.gameRepository = gameRepository;
        this.gameMapper = gameMapper;
    }

    public GamingSessionDto gamingSessionToDto(GamingSession session) {
        GamingSessionDto dto = new GamingSessionDto();
        dto.setId(session.getId());
        dto.setSessionName(session.getSessionName());
        dto.setGameDto(gameMapper.gameToGameDto(session.getGame()));
        dto.setMaxPlayers(session.getMaxPlayers());
        dto.setSessionType(session.getSessionType().name());
        dto.setStartDateTime(session.getStartDateTime());
        dto.setDescription(session.getDescription());
        dto.setStatus(session.getStatus().name());
        dto.setOrganizerId(session.getOrganizerId());
        dto.setParticipantIds(session.getParticipantIds());
        dto.setCurrentPlayerCount(session.getParticipantIds().size());
        return dto;
    }

    public GamingSession gamingSessionCreateDtoToSession(GamingSessionCreateDto dto) {
        GamingSession session = new GamingSession();
        session.setSessionName(dto.getSessionName());
        session.setGame(gameRepository.findById(dto.getGameId())
                .orElseThrow(() -> new NotFoundException("Game not found")));
        session.setMaxPlayers(dto.getMaxPlayers());
        session.setSessionType(SessionType.valueOf(dto.getSessionType().toUpperCase()));
        session.setStartDateTime(dto.getStartDateTime());
        session.setDescription(dto.getDescription());
        session.setStatus(SessionStatus.SCHEDULED);
        session.setOrganizerId(dto.getOrganizerId());
        session.getParticipantIds().add(dto.getOrganizerId());
        return session;
    }
}