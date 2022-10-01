package com.finder.finder.service.message;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public interface SimpleSendMessageService {
    void send(EditMessageText message);
}
