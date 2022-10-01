package com.finder.finder.service.message;

import com.finder.finder.helpers.ItemsHandler;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public interface SendMessageService {
    void sendChurchNews(EditMessageText message, ItemsHandler itemsHandler);
    void sendReligionNews(EditMessageText message, ItemsHandler itemsHandler);
}
