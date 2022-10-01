package com.finder.finder.message.impl;

import com.finder.finder.bot.FinderPollingBot;
import com.finder.finder.message.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class DefaultMessageSender implements MessageSender {
    private FinderPollingBot finderPollingBot;

    @Override
    public void sendMessage(SendMessage sendMessage) {
        try {
            finderPollingBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    public void setFinderPollingBot(FinderPollingBot finderPollingBot) {
        this.finderPollingBot = finderPollingBot;
    }
}
