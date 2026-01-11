package com.raf.gaminguserservice.listener;

import com.raf.gaminguserservice.dto.IncrementOrganizedSessionsDto;
import com.raf.gaminguserservice.listener.helper.MessageHelper;
import com.raf.gaminguserservice.service.UserService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;

@Component
public class IncrementOrganizedSessionsListener {

    private MessageHelper messageHelper;
    private UserService userService;

    public IncrementOrganizedSessionsListener(MessageHelper messageHelper, UserService userService) {
        this.messageHelper = messageHelper;
        this.userService = userService;
    }

    @JmsListener(destination = "${destination.incrementOrganizedSessions}", concurrency = "5-10")
    public void incrementOrganizedSessions(Message message) throws JMSException {
        IncrementOrganizedSessionsDto dto = messageHelper.getMessage(message, IncrementOrganizedSessionsDto.class);
        userService.incrementOrganizedSessions(dto);
    }
}