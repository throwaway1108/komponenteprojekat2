package com.raf.gaminguserservice.listener;

import com.raf.gaminguserservice.dto.UpdateUserStatsDto;
import com.raf.gaminguserservice.listener.helper.MessageHelper;
import com.raf.gaminguserservice.service.UserService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;

@Component
public class UpdateUserStatsListener {

    private MessageHelper messageHelper;
    private UserService userService;

    public UpdateUserStatsListener(MessageHelper messageHelper, UserService userService) {
        this.messageHelper = messageHelper;
        this.userService = userService;
    }

    @JmsListener(destination = "${destination.updateUserStats}", concurrency = "5-10")
    public void updateUserStats(Message message) throws JMSException {
        UpdateUserStatsDto dto = messageHelper.getMessage(message, UpdateUserStatsDto.class);
        userService.updateUserStats(dto);
    }
}