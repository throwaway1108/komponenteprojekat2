package com.raf.notificationservice.listener;

import com.raf.notificationservice.dto.SendNotificationDto;
import com.raf.notificationservice.listener.helper.MessageHelper;
import com.raf.notificationservice.service.NotificationService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;

@Component
public class SendNotificationListener {

    private MessageHelper messageHelper;
    private NotificationService notificationService;

    public SendNotificationListener(MessageHelper messageHelper, NotificationService notificationService) {
        this.messageHelper = messageHelper;
        this.notificationService = notificationService;
    }

    @JmsListener(destination = "${destination.sendNotification}", concurrency = "5-10")
    public void sendNotification(Message message) throws JMSException {
        SendNotificationDto dto = messageHelper.getMessage(message, SendNotificationDto.class);
        notificationService.processNotification(dto);
    }
}