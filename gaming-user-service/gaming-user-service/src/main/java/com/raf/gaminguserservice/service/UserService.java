package com.raf.gaminguserservice.service;

import com.raf.gaminguserservice.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserDto findById(Long id);
    Page<UserDto> findAll(Pageable pageable);
    UserDto add(UserCreateDto userCreateDto);
    TokenResponseDto login(TokenRequestDto tokenRequestDto);
    void activateAccount(ActivateAccountDto dto);
    void blockUser(BlockUserDto dto);
    UserDto updateUser(Long id, UserUpdateDto dto);
    CheckUserEligibilityDto checkEligibility(Long id);
    void updateUserStats(UpdateUserStatsDto dto);
    void incrementOrganizedSessions(IncrementOrganizedSessionsDto dto);
    void incrementRegisteredSessions(Long id);
    void blockUnblockUser(Long id);
}