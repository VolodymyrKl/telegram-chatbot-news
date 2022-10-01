package com.finder.finder.bot;

import com.finder.finder.handler.impl.DefaultMenuHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class FinderPollingBot extends TelegramLongPollingBot {
    @Value("${telegram.bot.username}")
    private String username;
    @Value("${telegram.bot.token}")
    private String token;

    private DefaultMenuHandler menuHandler;

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update != null && update.hasMessage()) {
            try {
                execute(menuHandler.getDefaultMenu(update.getMessage().getChatId()));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        if (update != null && update.hasCallbackQuery()) {
            try {
                execute(menuHandler.handle(update));
                if (!update.getCallbackQuery().getData().equals("callback_single")) {
                    execute(menuHandler
                            .getDefaultMenu(update.getCallbackQuery().getMessage().getChatId()));
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Autowired
    public void setMenuHandler(DefaultMenuHandler menuHandler) {
        this.menuHandler = menuHandler;
    }
}
