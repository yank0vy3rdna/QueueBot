package ru.bez_createha.queue_bot.view.createQueue;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bez_createha.queue_bot.Bot;
import ru.bez_createha.queue_bot.model.State;
import ru.bez_createha.queue_bot.model.User;
import ru.bez_createha.queue_bot.utils.InlineButton;
import ru.bez_createha.queue_bot.view.CallbackCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Component
public class StepOneView implements CallbackCommand {
    private final InlineButton telegramUtil;

    public StepOneView(InlineButton telegramUtil) {
        this.telegramUtil = telegramUtil;
    }

    @Override
    public Predicate<String> statePredicate() {
        return s -> s.equals(State.QUEUE_MENU.toString());
    }

    @Override
    public Predicate<CallbackQuery> callbackPredicate() {
        return callbackQuery -> callbackQuery.getData().split("::")[0].equals("create_queue");
    }

    public void process(CallbackQuery callbackQuery, User user, Bot bot) throws TelegramApiException {
        user.setBotState(State.ENTER_QUEUE_NAME.toString());
        user.setMessageId(callbackQuery.getMessage().getMessageId());
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(callbackQuery.getMessage().getChatId().toString());
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setText("Введи название для очереди");

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(Collections.singletonList( telegramUtil.createInlineKeyboardButton(
                "Назад",
                "back"
        )));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);
        bot.execute(editMessageText);
    }
}
