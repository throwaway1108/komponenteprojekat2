package com.raf.gamingsessionservice.controller;

import com.raf.gamingsessionservice.dto.*;
import com.raf.gamingsessionservice.secutiry.CheckSecurity;
import com.raf.gamingsessionservice.service.GamingSessionService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@RequestMapping("/session")
public class GamingSessionController {

    private GamingSessionService gamingSessionService;

    public GamingSessionController(GamingSessionService gamingSessionService) {
        this.gamingSessionService = gamingSessionService;
    }

    @ApiOperation(value = "Get all sessions")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query")})
    @GetMapping
    @CheckSecurity(roles = {"ROLE_PLAYER", "ROLE_ADMIN"})
    public ResponseEntity<Page<GamingSessionDto>> findAll(
            @RequestHeader("Authorization") String authorization,
            @RequestParam(required = false) Long gameId,
            @RequestParam(required = false) String sessionType,
            @RequestParam(required = false) Integer maxPlayers,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Long userId,
            @ApiIgnore Pageable pageable) {
        return new ResponseEntity<>(gamingSessionService.findAll(gameId, sessionType, maxPlayers, description, userId, pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @CheckSecurity(roles = {"ROLE_PLAYER", "ROLE_ADMIN"})
    public ResponseEntity<GamingSessionDto> findById(@RequestHeader("Authorization") String authorization,
                                                     @PathVariable("id") Long id) {
        return new ResponseEntity<>(gamingSessionService.findById(id), HttpStatus.OK);
    }

    @PostMapping
    @CheckSecurity(roles = {"ROLE_PLAYER", "ROLE_ADMIN"})
    public ResponseEntity<GamingSessionDto> create(@RequestHeader("Authorization") String authorization,
                                                   @RequestBody @Valid GamingSessionCreateDto dto) {
        return new ResponseEntity<>(gamingSessionService.create(dto), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/join")
    @CheckSecurity(roles = {"ROLE_PLAYER", "ROLE_ADMIN"})
    public ResponseEntity<Void> joinSession(@RequestHeader("Authorization") String authorization,
                                            @PathVariable("id") Long sessionId,
                                            @RequestBody @Valid JoinSessionDto dto) {
        gamingSessionService.joinSession(sessionId, dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/invite")
    @CheckSecurity(roles = {"ROLE_PLAYER", "ROLE_ADMIN"})
    public ResponseEntity<Void> inviteToSession(@RequestHeader("Authorization") String authorization,
                                                @PathVariable("id") Long sessionId,
                                                @RequestBody @Valid InviteToSessionDto dto) {
        gamingSessionService.inviteToSession(sessionId, dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/cancel")
    @CheckSecurity(roles = {"ROLE_PLAYER", "ROLE_ADMIN"})
    public ResponseEntity<Void> cancelSession(@RequestHeader("Authorization") String authorization,
                                              @PathVariable("id") Long sessionId,
                                              @RequestBody @Valid CancelSessionDto dto) {
        gamingSessionService.cancelSession(sessionId, dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/complete")
    @CheckSecurity(roles = {"ROLE_PLAYER", "ROLE_ADMIN"})
    public ResponseEntity<Void> completeSession(@RequestHeader("Authorization") String authorization,
                                                @PathVariable("id") Long sessionId,
                                                @RequestBody @Valid CompleteSessionDto dto) {
        gamingSessionService.completeSession(sessionId, dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/leave/{userId}")
    public ResponseEntity<Void> leaveSession(@PathVariable("id") Long sessionId, @PathVariable("userId") Long userId) {
        gamingSessionService.leaveSession(sessionId, userId);
        return ResponseEntity.ok().build();
    }
}