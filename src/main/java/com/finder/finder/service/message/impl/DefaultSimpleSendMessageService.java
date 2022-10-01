package com.finder.finder.service.message.impl;

import com.finder.finder.message.MessageSender;
import com.finder.finder.service.message.SimpleSendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Service
public class DefaultSimpleSendMessageService implements SimpleSendMessageService {
    private MessageSender messageSender;

    @Override
    public void send(EditMessageText message) {
        messageSender.sendMessage(getSendMessage(message, "T"));
    }

    private SendMessage getSendMessage(EditMessageText message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        return sendMessage;
    }

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }
}
