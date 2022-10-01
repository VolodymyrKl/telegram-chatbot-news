package com.finder.finder.handler.impl;

import com.finder.finder.handler.MenuHandler;
import com.finder.finder.helpers.impl.news.RisuNewsHelper;
import com.finder.finder.helpers.impl.news.RomfeaNewsHelper;
import com.finder.finder.helpers.impl.news.SpzhNewsHelper;
import com.finder.finder.helpers.impl.offchurch.*;
import com.finder.finder.service.menu.impl.DefaultKeyboardService;
import com.finder.finder.service.message.impl.DefaultSendMessageService;
import com.finder.finder.service.message.impl.DefaultSimpleSendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import static java.lang.Math.toIntExact;

@Component
public class DefaultMenuHandler implements MenuHandler {
    private DefaultSendMessageService defaultSendMessageService;
    private DefaultSimpleSendMessageService simpleSendMessageService;
    private DefaultKeyboardService keyboardService;

    private AlexandriaChurchHelper alexandriaChurchHelper;
    private AntiochChurchHelper antiochChurchHelper;
    private JerusalemChurchHelper jerusalemChurchHelper;
    private RussianChurchHelper russianChurchHelper;
    private SerbianChurchHelper serbianChurchHelper;
    private BulgarianChurchHelper bulgarianChurchHelper;
    private RomanianChurchHelper romanianChurchHelper;
    private GeorgianChurchHelper georgianChurchHelper;
    private CyprusChurchHelper cyprusChurchHelper;
    private GreeceChurchHelper greeceChurchHelper;
    private AlbanianChurchHelper albanianChurchHelper;
    private PolandChurchHelper polandChurchHelper;
    private CzechChurchHelper czechChurchHelper;
    private AmericaChurchHelper americaChurchHelper;
    private OCUChurchHelper ocuChurchHelper;
    private GOAAChurchHelper goaaChurchHelper;
    private UgccChurchHelper ugccChurchHelper;
    private VaticanChurchHelper vaticanChurchHelper;

    private RomfeaNewsHelper romfeaNewsHelper;
    private RisuNewsHelper risuNewsHelper;
    private SpzhNewsHelper spzhNewsHelper;

    @Override
    public EditMessageText handle(Update update) {
        long messageId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        EditMessageText message = getEditMessage(messageId, chatId);

        switch (update.getCallbackQuery().getData()) {
            case "callback_single": {
                return message;
            }
            case "callback_ppc": {
            }
            case "callback_ocu": {
                defaultSendMessageService.sendChurchNews(message, ocuChurchHelper);
                message.setReplyMarkup(keyboardService.getMenuKeyboard());
                break;
            }
            case "callback_romfea": {
                defaultSendMessageService.sendReligionNews(message, romfeaNewsHelper);
                message.setReplyMarkup(keyboardService.getMenuKeyboard());
                break;
            }
            case "callback_risu": {
                defaultSendMessageService.sendReligionNews(message, risuNewsHelper);
                message.setReplyMarkup(keyboardService.getMenuKeyboard());
                break;
            }
            case "callback_spzh": {
                defaultSendMessageService.sendReligionNews(message, spzhNewsHelper);
                message.setReplyMarkup(keyboardService.getMenuKeyboard());
                break;
            }
            case "callback_okum": {
                message.setText("Розробка ще не завершена, оберіть інший варіант.");
                message.setReplyMarkup(keyboardService.getMenuKeyboard());
                break;
            }
            case "callback_alex": {
                defaultSendMessageService.sendChurchNews(message, alexandriaChurchHelper);
                message.setReplyMarkup(keyboardService.getMenuKeyboard());
                break;
            }
            case "callback_ant": {
                defaultSendMessageService.sendChurchNews(message, antiochChurchHelper);
                message.setReplyMarkup(keyboardService.getMenuKeyboard());
                break;
            }
            case "callback_jer": {
                defaultSendMessageService.sendChurchNews(message, jerusalemChurchHelper);
                message.setReplyMarkup(keyboardService.getMenuKeyboard());
                break;
            }
            case "callback_rus": {
                defaultSendMessageService.sendChurchNews(message, russianChurchHelper);
                message.setReplyMarkup(keyboardService.getMenuKeyboard());
                break;
            }
//            case "callback_ser" : defaultSendMessageService.sendChurchNews(message, serbianChurchHelper); break;}
            case "callback_bul": {
                defaultSendMessageService.sendChurchNews(message, bulgarianChurchHelper);
                message.setReplyMarkup(keyboardService.getMenuKeyboard());
                break;
            }
            case "callback_rom": {
                defaultSendMessageService.sendChurchNews(message, romanianChurchHelper);
                message.setReplyMarkup(keyboardService.getMenuKeyboard());
                break;
            }
            case "callback_geo": {
                defaultSendMessageService.sendChurchNews(message, georgianChurchHelper);
                message.setReplyMarkup(keyboardService.getMenuKeyboard());
                break;
            }
            case "callback_cyp": {
                defaultSendMessageService.sendChurchNews(message, cyprusChurchHelper);
                message.setReplyMarkup(keyboardService.getMenuKeyboard());
                break;
            }
            case "callback_gre": {
                defaultSendMessageService.sendChurchNews(message, greeceChurchHelper);
                message.setReplyMarkup(keyboardService.getMenuKeyboard());
                break;
            }
            case "callback_alb": {
                defaultSendMessageService.sendChurchNews(message, albanianChurchHelper);
                message.setReplyMarkup(keyboardService.getMenuKeyboard());
                break;
            }
            case "callback_pol": {
                defaultSendMessageService.sendChurchNews(message, polandChurchHelper);
                message.setReplyMarkup(keyboardService.getMenuKeyboard());
                break;
            }
            case "callback_cze": {
                defaultSendMessageService.sendChurchNews(message, czechChurchHelper);
                message.setReplyMarkup(keyboardService.getMenuKeyboard());
                break;
            }
            case "callback_ame": {
                defaultSendMessageService.sendChurchNews(message, americaChurchHelper);
                message.setReplyMarkup(keyboardService.getMenuKeyboard());
                break;
            }
            case "callback_goaa": {
                defaultSendMessageService.sendChurchNews(message, goaaChurchHelper);
                message.setReplyMarkup(keyboardService.getMenuKeyboard());
                break;
            }
            case "callback_ugcc": {
                defaultSendMessageService.sendChurchNews(message, ugccChurchHelper);
                message.setReplyMarkup(keyboardService.getMenuKeyboard());
                break;
            }
            case "callback_vat": {
                defaultSendMessageService.sendChurchNews(message, vaticanChurchHelper);
                message.setReplyMarkup(keyboardService.getMenuKeyboard());
                break;
            }
            default: {
                message.setReplyMarkup(keyboardService.getMenuKeyboard());
            }
        }
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
    public void setSimpleSendMessageService(DefaultSimpleSendMessageService simpleSendMessageService) {
        this.simpleSendMessageService = simpleSendMessageService;
    }

    @Autowired
    public void setKeyboardService(DefaultKeyboardService keyboardService) {
        this.keyboardService = keyboardService;
    }

    @Autowired
    public void setAlexandriaChurchHelper(AlexandriaChurchHelper alexandriaChurchHelper) {
        this.alexandriaChurchHelper = alexandriaChurchHelper;
    }

    @Autowired
    public void setAntiochChurchHelper(AntiochChurchHelper antiochChurchHelper) {
        this.antiochChurchHelper = antiochChurchHelper;
    }

    @Autowired
    public void setJerusalemChurchHelper(JerusalemChurchHelper jerusalemChurchHelper) {
        this.jerusalemChurchHelper = jerusalemChurchHelper;
    }

    @Autowired
    public void setRussianChurchHelper(RussianChurchHelper russianChurchHelper) {
        this.russianChurchHelper = russianChurchHelper;
    }

    @Autowired
    public void setSerbianChurchHelper(SerbianChurchHelper serbianChurchHelper) {
        this.serbianChurchHelper = serbianChurchHelper;
    }

    @Autowired
    public void setBulgarianChurchHelper(BulgarianChurchHelper bulgarianChurchHelper) {
        this.bulgarianChurchHelper = bulgarianChurchHelper;
    }

    @Autowired
    public void setRomanianChurchHelper(RomanianChurchHelper romanianChurchHelper) {
        this.romanianChurchHelper = romanianChurchHelper;
    }

    @Autowired
    public void setGeorgianChurchHelper(GeorgianChurchHelper georgianChurchHelper) {
        this.georgianChurchHelper = georgianChurchHelper;
    }

    @Autowired
    public void setCyprusChurchHelper(CyprusChurchHelper cyprusChurchHelper) {
        this.cyprusChurchHelper = cyprusChurchHelper;
    }

    @Autowired
    public void setGreeceChurchHelper(GreeceChurchHelper greeceChurchHelper) {
        this.greeceChurchHelper = greeceChurchHelper;
    }

    @Autowired
    public void setAlbanianChurchHelper(AlbanianChurchHelper albanianChurchHelper) {
        this.albanianChurchHelper = albanianChurchHelper;
    }

    @Autowired
    public void setPolandChurchHelper(PolandChurchHelper polandChurchHelper) {
        this.polandChurchHelper = polandChurchHelper;
    }

    @Autowired
    public void setCzechChurchHelper(CzechChurchHelper czechChurchHelper) {
        this.czechChurchHelper = czechChurchHelper;
    }

    @Autowired
    public void setAmericaChurchHelper(AmericaChurchHelper americaChurchHelper) {
        this.americaChurchHelper = americaChurchHelper;
    }

    @Autowired
    public void setOcuChurchHelper(OCUChurchHelper ocuChurchHelper) {
        this.ocuChurchHelper = ocuChurchHelper;
    }

    @Autowired
    public void setGoaaChurchHelper(GOAAChurchHelper goaaChurchHelper) {
        this.goaaChurchHelper = goaaChurchHelper;
    }

    @Autowired
    public void setRomfeaNewsHelper(RomfeaNewsHelper romfeaNewsHelper) {
        this.romfeaNewsHelper = romfeaNewsHelper;
    }

    @Autowired
    public void setRisuNewsHelper(RisuNewsHelper risuNewsHelper) {
        this.risuNewsHelper = risuNewsHelper;
    }

    @Autowired
    public void setSpzhNewsHelper(SpzhNewsHelper spzhNewsHelper) {
        this.spzhNewsHelper = spzhNewsHelper;
    }

    @Autowired
    public void setUgccChurchHelper(UgccChurchHelper ugccChurchHelper) {
        this.ugccChurchHelper = ugccChurchHelper;
    }

    @Autowired
    public void setVaticanChurchHelper(VaticanChurchHelper vaticanChurchHelper) {
        this.vaticanChurchHelper = vaticanChurchHelper;
    }
}
