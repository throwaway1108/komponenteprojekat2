package com.raf.gamingsessionservice.service;

import com.raf.gamingsessionservice.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GamingSessionService {

    Page<GamingSessionDto> findAll(Long gameId, String sessionType, Integer maxPlayers,
                                   String description, Long userId, Pageable pageable);

    GamingSessionDto findById(Long id);

    GamingSessionDto create(GamingSessionCreateDto dto);

    void joinSession(Long sessionId, JoinSessionDto dto);

    void inviteToSession(Long sessionId, InviteToSessionDto dto);

    void cancelSession(Long sessionId, CancelSessionDto dto);

    void completeSession(Long sessionId, CompleteSessionDto dto);

    void leaveSession(Long sessionId, Long userId);
}