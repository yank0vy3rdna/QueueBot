package ru.bez_createha.queue_bot.view;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bez_createha.queue_bot.Bot;
import ru.bez_createha.queue_bot.model.Queue;
import ru.bez_createha.queue_bot.model.QueueStatus;
import ru.bez_createha.queue_bot.model.State;
import ru.bez_createha.queue_bot.model.User;
import ru.bez_createha.queue_bot.services.QueueService;
import ru.bez_createha.queue_bot.utils.InlineButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Component
public class StopMenu implements CallbackCommand{
    private final QueueService queueService;
    private final InlineButton telegramUtil;

    public StopMenu(QueueService queueService, InlineButton telegramUtil) {
        this.queueService = queueService;
        this.telegramUtil = telegramUtil;
    }

    @Override
    public Predicate<String> statePredicate() {
        return s -> s.equals(State.QUEUE_MENU_PANEL.toString());
    }

    @Override
    public Predicate<CallbackQuery> callbackPredicate() {
        return callbackQuery -> callbackQuery.getData().split("::")[0].equals("stop");
    }
    @Override
    public void process(CallbackQuery callbackQuery, User user, Bot bot) throws TelegramApiException {
        user.setBotState(State.QUEUE_MENU_DELETE.toString());
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        Queue queue = queueService.getById(Long.valueOf(callbackQuery.getData().split("::")[1]));

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(Collections.singletonList(telegramUtil.createInlineKeyboardButton(
                "Назад",
                "back"
        )));
        inlineKeyboardMarkup.setKeyboard(keyboard);

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setMessageId(user.getMessageId());
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);
        editMessageText.setChatId(callbackQuery.getMessage().getChatId().toString());

        if (queue.getStatus().equals(QueueStatus.FINISHED)){

            editMessageText.setText("Очередь уже остановлена");
        }else {
            queue.setStatus(QueueStatus.FINISHED);
            queueService.save(queue);

            editMessageText.setText("Очередь остановлена");
        }
        bot.execute(editMessageText);
    }
}
