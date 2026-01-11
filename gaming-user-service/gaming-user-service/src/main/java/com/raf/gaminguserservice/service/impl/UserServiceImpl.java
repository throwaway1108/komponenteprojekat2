package com.raf.gaminguserservice.service.impl;

import com.raf.gaminguserservice.domain.User;
import com.raf.gaminguserservice.dto.*;
import com.raf.gaminguserservice.exception.NotFoundException;
import com.raf.gaminguserservice.listener.helper.MessageHelper;
import com.raf.gaminguserservice.mapper.UserMapper;
import com.raf.gaminguserservice.repository.UserRepository;
import com.raf.gaminguserservice.security.service.TokenService;
import com.raf.gaminguserservice.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private TokenService tokenService;
    private UserRepository userRepository;
    private UserMapper userMapper;
    private JmsTemplate jmsTemplate;
    private MessageHelper messageHelper;

    @Value("${destination.sendNotification}")
    private String sendNotificationDestination;

    public UserServiceImpl(UserRepository userRepository, TokenService tokenService, UserMapper userMapper,
                           JmsTemplate jmsTemplate, MessageHelper messageHelper) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.userMapper = userMapper;
        this.jmsTemplate = jmsTemplate;
        this.messageHelper = messageHelper;
    }

    @Override
    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::userToUserDto)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::userToUserDto);
    }

    @Override
    public UserDto add(UserCreateDto userCreateDto) {
        User user = userMapper.userCreateDtoToUser(userCreateDto);

        user = userRepository.save(user);

        String activationParam = user.getEmail();

        SendNotificationDto notificationDto = new SendNotificationDto(
                user.getEmail(),
                "ACTIVATION_EMAIL",
                activationParam,
                user.getId()
        );

        jmsTemplate.convertAndSend(sendNotificationDestination,
                messageHelper.createTextMessage(notificationDto));

        return userMapper.userToUserDto(user);
    }

    @Override
    public TokenResponseDto login(TokenRequestDto tokenRequestDto) {
        User user = userRepository
                .findUserByUsername(tokenRequestDto.getUsername())
                .orElseThrow(() -> new NotFoundException(String
                        .format("User with username: %s not found.", tokenRequestDto.getUsername())));

        if (!user.getPassword().equals(tokenRequestDto.getPassword())) {
            throw new NotFoundException("Invalid password for user: " + tokenRequestDto.getUsername());
        }

        if (!user.getActive()) {
            throw new RuntimeException("Account not activated");
        }

        if (user.getBlocked()) {
            throw new RuntimeException("Account is blocked");
        }

        Claims claims = Jwts.claims();
        claims.put("id", user.getId());
        claims.put("role", user.getRole().getName());
        claims.put("username", user.getUsername());

        return new TokenResponseDto(tokenService.generate(claims));
    }

    @Override
    public void activateAccount(ActivateAccountDto dto) {
        User user = userRepository.findAll().stream()
                .filter(u -> u.getEmail().equals(dto.getEmail()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public void blockUser(BlockUserDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setBlocked(dto.getBlock());
        userRepository.save(user);
    }

    @Override
    public UserDto updateUser(Long id, UserUpdateDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());

        return userMapper.userToUserDto(userRepository.save(user));
    }

    @Override
    public CheckUserEligibilityDto checkEligibility(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (user.getBlocked()) {
            return new CheckUserEligibilityDto(false, "User is blocked");
        }

        if (user.getAttendancePercentage() < 90.0) {
            return new CheckUserEligibilityDto(false, "Attendance percentage below 90%");
        }

        return new CheckUserEligibilityDto(true, "User is eligible");
    }

    @Override
    public void updateUserStats(UpdateUserStatsDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (dto.getAttended()) {
            user.setTotalAttendedSessions(user.getTotalAttendedSessions() + 1);
        } else {
            user.setTotalAbandonedSessions(user.getTotalAbandonedSessions() + 1);
        }

        int total = user.getTotalAttendedSessions() + user.getTotalAbandonedSessions();
        if (total > 0) {
            double percentage = (user.getTotalAttendedSessions() * 100.0) / total;
            user.setAttendancePercentage(percentage);
        }

        userRepository.save(user);
    }

    @Override
    public void incrementOrganizedSessions(IncrementOrganizedSessionsDto dto) {
        userRepository.findById(dto.getUserId())
                .ifPresent(user -> {
                    user.setSuccessfullyOrganizedSessions(user.getSuccessfullyOrganizedSessions() + 1);

                    user.setOrganizerTitle(calculateTitle(user.getSuccessfullyOrganizedSessions()));

                    userRepository.save(user);
                });
    }

    @Override
    public void incrementRegisteredSessions(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setTotalRegisteredSessions(user.getTotalRegisteredSessions() + 1);
        userRepository.save(user);
    }

    public void blockUnblockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setBlocked(!user.getBlocked());
        userRepository.save(user);
        System.out.println("User " + id + " blocked status is now: " + user.getBlocked());
    }

    private String calculateTitle(Integer sessions) {
        if (sessions >= 100) return "Knez";
        if (sessions >= 50) return "Vojvoda";
        if (sessions >= 25) return "Hajduk";
        if (sessions >= 10) return "Barjaktar";
        return "Nema titule";
    }
}