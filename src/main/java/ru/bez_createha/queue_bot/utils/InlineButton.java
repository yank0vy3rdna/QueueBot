package ru.bez_createha.queue_bot.utils;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
public class InlineButton {

    public InlineKeyboardButton createInlineKeyboardButton(String text, String callbackData) {
        InlineKeyboardButton bttn = new InlineKeyboardButton();
        bttn.setText(text);
        bttn.setCallbackData(callbackData);
        return bttn;
    }
}
