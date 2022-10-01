package com.finder.finder.service.menu;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface KeyboardService {
    InlineKeyboardMarkup getMenuKeyboard();

    InlineKeyboardMarkup getPPCKeyboard();
}
