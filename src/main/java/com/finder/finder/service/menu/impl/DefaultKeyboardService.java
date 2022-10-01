package com.finder.finder.service.menu.impl;

import com.finder.finder.service.menu.KeyboardService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultKeyboardService implements KeyboardService {

    @Override
    public InlineKeyboardMarkup getMenuKeyboard() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();

        rowInline1.add(getInlineKeyboardButton("Помісні православні церкви", "callback_ppc"));
        rowInline2.add(getInlineKeyboardButton("Обрати окрему Помісну церкву", "callback_single"));
        rowInline2.add(getInlineKeyboardButton("ПЦУ", "callback_ocu"));
        rowInline.add(getInlineKeyboardButton("Ромфеа", "callback_romfea"));
        rowInline.add(getInlineKeyboardButton("РІСУ", "callback_risu"));
        rowInline.add(getInlineKeyboardButton("СПЖ", "callback_spzh"));
        rowInline.add(getInlineKeyboardButton("УГКЦ", "callback_ugcc"));
        rowInline3.add(getInlineKeyboardButton("Ватикан", "callback_vat"));

        rowsInline.add(rowInline);
        rowsInline.add(rowInline1);
        rowsInline.add(rowInline2);
        rowsInline.add(rowInline3);
        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }

    private InlineKeyboardButton getInlineKeyboardButton(String text, String callBackData) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(text);
        inlineKeyboardButton.setCallbackData(callBackData);
        return inlineKeyboardButton;
    }

    @Override
    public InlineKeyboardMarkup getPPCKeyboard() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(getInlineKeyboardButton("Константинополь", "callback_okum"));
        rowInline.add(getInlineKeyboardButton("Александрія", "callback_alex"));

        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        rowInline1.add(getInlineKeyboardButton("Антіохія", "callback_ant"));
        rowInline1.add(getInlineKeyboardButton("Єрусалим", "callback_jer"));

        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        rowInline2.add(getInlineKeyboardButton("Росія", "callback_rus"));
        rowInline2.add(getInlineKeyboardButton("Сербія", "callback_ser"));

        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
        rowInline3.add(getInlineKeyboardButton("Болгарія", "callback_bul"));
        rowInline3.add(getInlineKeyboardButton("Румунія", "callback_rom"));

        List<InlineKeyboardButton> rowInline4 = new ArrayList<>();
        rowInline4.add(getInlineKeyboardButton("Грузія", "callback_geo"));
        rowInline4.add(getInlineKeyboardButton("Кіпр", "callback_cyp"));

        List<InlineKeyboardButton> rowInline5 = new ArrayList<>();
        rowInline5.add(getInlineKeyboardButton("Греція", "callback_gre"));
        rowInline5.add(getInlineKeyboardButton("Албанія", "callback_alb"));

        List<InlineKeyboardButton> rowInline6 = new ArrayList<>();
        rowInline6.add(getInlineKeyboardButton("Польща", "callback_pol"));
        rowInline6.add(getInlineKeyboardButton("Чехія", "callback_cze"));

        List<InlineKeyboardButton> rowInline7 = new ArrayList<>();
        rowInline7.add(getInlineKeyboardButton("Америка", "callback_ame"));
        rowInline7.add(getInlineKeyboardButton("Грецька архієпископія в США", "callback_goaa"));
        rowInline7.add(getInlineKeyboardButton("Повернутися назад", "callback_back"));

        rowsInline.add(rowInline);
        rowsInline.add(rowInline1);
        rowsInline.add(rowInline2);
        rowsInline.add(rowInline3);
        rowsInline.add(rowInline4);
        rowsInline.add(rowInline5);
        rowsInline.add(rowInline6);
        rowsInline.add(rowInline7);
        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }
}
