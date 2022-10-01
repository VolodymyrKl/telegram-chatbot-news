package com.finder.finder.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.Method;

public interface MenuHandler {

    EditMessageText handle(Update update);

    SendMessage getDefaultMenu(SendMessage message, Update update);
}
