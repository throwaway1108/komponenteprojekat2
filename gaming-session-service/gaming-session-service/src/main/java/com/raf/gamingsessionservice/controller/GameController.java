package com.raf.gamingsessionservice.controller;

import com.raf.gamingsessionservice.dto.GameCreateDto;
import com.raf.gamingsessionservice.dto.GameDto;
import com.raf.gamingsessionservice.secutiry.CheckSecurity;
import com.raf.gamingsessionservice.service.GameService;
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
@RequestMapping("/game")
public class GameController {

    private GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @ApiOperation(value = "Get all games")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query")})

    @GetMapping
    @CheckSecurity(roles = {"ROLE_PLAYER", "ROLE_ADMIN"})
    public ResponseEntity<Page<GameDto>> findAll(@RequestHeader("Authorization") String authorization,
                                                 @ApiIgnore Pageable pageable) {
        return new ResponseEntity<>(gameService.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @CheckSecurity(roles = {"ROLE_PLAYER", "ROLE_ADMIN"})
    public ResponseEntity<GameDto> findById(@RequestHeader("Authorization") String authorization,
                                            @PathVariable("id") Long id) {
        return new ResponseEntity<>(gameService.findById(id), HttpStatus.OK);
    }

    @PostMapping
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<GameDto> add(@RequestHeader("Authorization") String authorization,
                                       @RequestBody @Valid GameCreateDto gameCreateDto) {
        return new ResponseEntity<>(gameService.add(gameCreateDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<GameDto> update(@RequestHeader("Authorization") String authorization,
                                          @PathVariable("id") Long id,
                                          @RequestBody @Valid GameCreateDto gameCreateDto) {
        return new ResponseEntity<>(gameService.update(id, gameCreateDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String authorization,
                                    @PathVariable("id") Long id) {
        gameService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}