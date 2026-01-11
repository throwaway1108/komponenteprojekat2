package com.raf.gaminguserservice.controller;

import com.raf.gaminguserservice.dto.*;
import com.raf.gaminguserservice.security.CheckSecurity;
import com.raf.gaminguserservice.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Get all users")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "What page number you want", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "Number of items to return", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")})
    @GetMapping
    @CheckSecurity(roles = {"ROLE_ADMIN", "ROLE_PLAYER"})
    public ResponseEntity<Page<UserDto>> getAllUsers(@RequestHeader("Authorization") String authorization,
                                                     Pageable pageable) {
        return new ResponseEntity<>(userService.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @CheckSecurity(roles = {"ROLE_ADMIN", "ROLE_PLAYER"})
    public ResponseEntity<UserDto> findById(@RequestHeader("Authorization") String authorization,
                                            @PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Register user")
    @PostMapping
    public ResponseEntity<UserDto> saveUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        return new ResponseEntity<>(userService.add(userCreateDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Login")
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> loginUser(@RequestBody @Valid TokenRequestDto tokenRequestDto) {
        return new ResponseEntity<>(userService.login(tokenRequestDto), HttpStatus.OK);
    }

    @PostMapping("/activate")
    public ResponseEntity<Void> activateAccount(@RequestBody @Valid ActivateAccountDto dto) {
        userService.activateAccount(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/block")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<Void> blockUser(@RequestHeader("Authorization") String authorization,
                                          @RequestBody @Valid BlockUserDto dto) {
        userService.blockUser(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @CheckSecurity(roles = {"ROLE_ADMIN", "ROLE_PLAYER"})
    public ResponseEntity<UserDto> updateUser(@RequestHeader("Authorization") String authorization,
                                              @PathVariable Long id,
                                              @RequestBody @Valid UserUpdateDto dto) {
        return new ResponseEntity<>(userService.updateUser(id, dto), HttpStatus.OK);
    }

    @ApiOperation(value = "Check if user is eligible to join/create session")
    @GetMapping("/{id}/check-eligibility")
    public ResponseEntity<CheckUserEligibilityDto> checkEligibility(@PathVariable Long id) {
        return new ResponseEntity<>(userService.checkEligibility(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Update user attendance stats (Synchronous)")
    @PostMapping("/update-stats")
    public ResponseEntity<Void> updateUserStats(@RequestBody @Valid UpdateUserStatsDto dto) {
        userService.updateUserStats(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Increment organized sessions and update title (Synchronous)")
    @PostMapping("/increment-organized")
    public ResponseEntity<Void> incrementOrganized(@RequestBody @Valid IncrementOrganizedSessionsDto dto) {
        userService.incrementOrganizedSessions(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/increment-registered")
    public ResponseEntity<Void> incrementRegistered(@PathVariable Long id) {
        userService.incrementRegisteredSessions(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<Void> blockUnblockUser(@PathVariable Long id) {
        System.out.println("DEBUG: Primljen zahtev za block/unblock za ID: " + id);
        userService.blockUnblockUser(id);
        return ResponseEntity.ok().build();
    }
}