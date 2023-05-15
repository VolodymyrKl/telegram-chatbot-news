package com.finder.finder.handler.impl;

import com.finder.finder.handler.MenuHandler;
import com.finder.finder.model.Item;
import com.finder.finder.service.ItemsService;
import com.finder.finder.service.menu.impl.DefaultKeyboardService;
import com.finder.finder.service.message.impl.DefaultSendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toIntExact;

@Component
public class DefaultMenuHandler implements MenuHandler {
    private DefaultSendMessageService defaultSendMessageService;
    private DefaultKeyboardService keyboardService;
    private ItemsService itemsService;

    @Override
    public EditMessageText handle(Update update) {
        long messageId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        EditMessageText message = getEditMessage(messageId, chatId);

        defaultSendMessageService.sendNews(message, itemsService.getItems());

        return message;
    }


    private EditMessageText getEditMessage(long messageId, long chatId) {
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setMessageId(toIntExact(messageId));
        message.setText("Новини з джерел:");
        message.setReplyMarkup(keyboardService.getPPCKeyboard());
        return message;
    }

    @Override
    public SendMessage getDefaultMenu(Long id) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(id));
        message.setText("Зробити вибір:");
        message.setReplyMarkup(keyboardService.getMenuKeyboard());
        return message;
    }

    @Autowired
    public void setDefaultSendMessageService(DefaultSendMessageService defaultSendMessageService) {
        this.defaultSendMessageService = defaultSendMessageService;
    }

    @Autowired
    public void setKeyboardService(DefaultKeyboardService keyboardService) {
        this.keyboardService = keyboardService;
    }

    @Autowired
    public void setItemsService(ItemsService itemsService) {
        this.itemsService = itemsService;
    }
}
