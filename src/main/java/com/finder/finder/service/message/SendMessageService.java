package com.finder.finder.service.message;

import com.finder.finder.helpers.ItemsHandler;
import com.finder.finder.model.Item;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.List;

public interface SendMessageService {
    void sendNews(EditMessageText message, List<Item> items);

    void sendChurchNews(EditMessageText message, ItemsHandler itemsHandler);

    void sendReligionNews(EditMessageText message, ItemsHandler itemsHandler);
}
