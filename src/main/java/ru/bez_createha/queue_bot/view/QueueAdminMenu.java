package ru.bez_createha.queue_bot.view;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bez_createha.queue_bot.Bot;
import ru.bez_createha.queue_bot.model.State;
import ru.bez_createha.queue_bot.model.User;
import ru.bez_createha.queue_bot.utils.InlineButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Component
public class QueueAdminMenu implements CallbackCommand {
    private final InlineButton telegramUtil;

    public QueueAdminMenu(InlineButton telegramUtil) {
        this.telegramUtil = telegramUtil;
    }

    @Override
    public Predicate<String> statePredicate() {
        return s -> s.equals(State.QUEUE_MENU.toString());
    }

    @Override
    public Predicate<CallbackQuery> callbackPredicate() {
        return callbackQuery -> callbackQuery.getData().split("::")[0].equals("queue");
    }

    @Override
    public void process(CallbackQuery callbackQuery, User user, Bot bot) throws TelegramApiException {

        user.setBotState(State.QUEUE_MENU_PANEL.toString());
        List<InlineKeyboardButton> keyboardButtonsRaw = new ArrayList<>();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        keyboardButtonsRaw.add(telegramUtil.createInlineKeyboardButton(
                "Посмотреть результат",
                "get_queues::"+callbackQuery.getData().split("::")[1]+"::"+callbackQuery.getData().split("::")[2]
        ));

        keyboard.add(keyboardButtonsRaw);
        keyboardButtonsRaw = new ArrayList<>();

        keyboardButtonsRaw.add(telegramUtil.createInlineKeyboardButton(
                "Остановить",
                "stop::"+callbackQuery.getData().split("::")[1]
        ));
        keyboardButtonsRaw.add(telegramUtil.createInlineKeyboardButton(
                "Назад",
                "back"
        ));
        keyboard.add(keyboardButtonsRaw);

        inlineKeyboardMarkup.setKeyboard(keyboard);


        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setMessageId(user.getMessageId());
        editMessageText.setChatId(callbackQuery.getMessage().getChatId().toString());
        editMessageText.setText("Выбранная очередь - "+callbackQuery.getData().split("::")[2]);
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);
        bot.execute(editMessageText);
    }
}
