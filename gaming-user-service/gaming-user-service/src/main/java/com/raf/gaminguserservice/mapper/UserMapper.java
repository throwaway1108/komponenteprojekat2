package com.raf.gaminguserservice.mapper;

import com.raf.gaminguserservice.domain.User;
import com.raf.gaminguserservice.dto.UserCreateDto;
import com.raf.gaminguserservice.dto.UserDto;
import com.raf.gaminguserservice.repository.RoleRepository;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private RoleRepository roleRepository;

    public UserMapper(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public UserDto userToUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setUsername(user.getUsername());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setActive(user.getActive());
        dto.setBlocked(user.getBlocked());
        dto.setTotalRegisteredSessions(user.getTotalRegisteredSessions());
        dto.setTotalAttendedSessions(user.getTotalAttendedSessions());
        dto.setTotalAbandonedSessions(user.getTotalAbandonedSessions());
        dto.setAttendancePercentage(user.getAttendancePercentage());
        dto.setSuccessfullyOrganizedSessions(user.getSuccessfullyOrganizedSessions());
        dto.setOrganizerTitle(user.getOrganizerTitle());
        return dto;
    }

    public User userCreateDtoToUser(UserCreateDto dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setDateOfBirth(dto.getDateOfBirth());

        user.setRole(roleRepository.findRoleByName("ROLE_PLAYER")
                .orElseThrow(() -> new RuntimeException("Role ROLE_PLAYER not found in database! Check TestDataRunner.")));

        user.setActive(false);
        user.setBlocked(false);
        return user;
    }
}