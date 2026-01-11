package com.raf.gamingsessionservice.repository;

import com.raf.gamingsessionservice.domain.GamingSession;
import com.raf.gamingsessionservice.domain.SessionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GamingSessionRepository extends JpaRepository<GamingSession, Long> {

    @Query("SELECT s FROM GamingSession s WHERE s.status = :status AND s.startDateTime BETWEEN :start AND :end")
    List<GamingSession> findSessionsStartingSoon(@Param("status") SessionStatus status,
                                                 @Param("start") LocalDateTime start,
                                                 @Param("end") LocalDateTime end);

    @Query("SELECT s FROM GamingSession s WHERE s.organizerId = :userId OR :userId MEMBER OF s.participantIds")
    Page<GamingSession> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT s FROM GamingSession s WHERE " +
            "(:gameId IS NULL OR s.game.id = :gameId) AND " +
            "(:type IS NULL OR s.sessionType = :type) AND " +
            "(:description IS NULL OR LOWER(s.description) LIKE LOWER(CONCAT('%', :description, '%')))")
    Page<GamingSession> searchSessions(@Param("gameId") Long gameId,
                                       @Param("type") String type,
                                       @Param("description") String description,
                                       Pageable pageable);
}