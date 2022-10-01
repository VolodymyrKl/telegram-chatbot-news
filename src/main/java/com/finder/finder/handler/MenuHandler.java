package com.finder.finder.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface MenuHandler {

    EditMessageText handle(Update update);

    SendMessage getDefaultMenu(Long update);
}
