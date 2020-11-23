package ru.bez_createha.queue_bot.view.changeName;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bez_createha.queue_bot.Bot;
import ru.bez_createha.queue_bot.context.UserContext;
import ru.bez_createha.queue_bot.model.Group;
import ru.bez_createha.queue_bot.model.Queue;
import ru.bez_createha.queue_bot.model.State;
import ru.bez_createha.queue_bot.model.User;
import ru.bez_createha.queue_bot.services.GroupService;
import ru.bez_createha.queue_bot.services.QueueService;
import ru.bez_createha.queue_bot.utils.InlineButton;
import ru.bez_createha.queue_bot.view.CallbackCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Component
public class ChangeNameController implements CallbackCommand {
    private final InlineButton telegramUtil;

    public ChangeNameController(InlineButton telegramUtil, QueueService queueService, GroupService groupService, UserContext userContext) {
        this.telegramUtil = telegramUtil;
    }

    @Override
    public Predicate<String> statePredicate() {
        return s -> s.equals(State.GROUP_MENU.toString());
    }

    @Override
    public Predicate<CallbackQuery> callbackPredicate() {
        return callbackQuery -> callbackQuery.getData().equals("change_name");
    }

    @Override
    public void process(CallbackQuery callbackQuery, User user, Bot bot) throws TelegramApiException {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        user.setBotState(State.CHANGE_NAME.toString());
        user.setMessageId(callbackQuery.getMessage().getMessageId());

        user.setMessageId(callbackQuery.getMessage().getMessageId());
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();


        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setChatId(callbackQuery.getMessage().getChatId().toString());

        editMessageText.setReplyMarkup(inlineKeyboardMarkup);


        keyboard.add(Collections.singletonList(telegramUtil.createInlineKeyboardButton(
                "Назад",
                "back"
        )));
        editMessageText.setText("Введите новое имя");
        inlineKeyboardMarkup.setKeyboard(keyboard);

        bot.execute(editMessageText);
    }
}
