package com.finder.finder.service.message.impl;

import com.finder.finder.helpers.ItemsHandler;
import com.finder.finder.message.MessageSender;
import com.finder.finder.model.Item;
import com.finder.finder.service.message.MessageItemsConverterService;
import com.finder.finder.service.message.SendMessageService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.List;

@Service
public class DefaultSendMessageService implements SendMessageService {
    private static final String NO_NEWS_MESSAGE = "Новин немає, варто випити кави і відпочити.";

    private MessageSender messageSender;
    private MessageItemsConverterService messageItemsConverterService;

    @Override
    public void sendReligionNews(EditMessageText message, ItemsHandler itemsHandler) {
        List<Item> items = itemsHandler.getItems();
        if (!items.isEmpty()) {
            List<List<Item>> itemsParts = Lists.partition(items, 10);
            for (List<Item> itemPart : itemsParts) {
                messageSender.sendMessage(getSendMessage(message, messageItemsConverterService.mapItems(itemPart)));
            }
        } else {
            messageSender.sendMessage(getSendMessage(message, NO_NEWS_MESSAGE));
        }
    }

    @Override
    public void sendChurchNews(EditMessageText message, ItemsHandler itemsHandler) {
        List<Item> items = itemsHandler.getItems();
        if (!items.isEmpty()) {
            messageSender.sendMessage(getSendMessage(message, messageItemsConverterService.mapItems(items)));
        } else {
            messageSender.sendMessage(getSendMessage(message, NO_NEWS_MESSAGE));
        }
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

    @Autowired
    public void setMessageItemsConverterService(MessageItemsConverterService messageItemsConverterService) {
        this.messageItemsConverterService = messageItemsConverterService;
    }
}
