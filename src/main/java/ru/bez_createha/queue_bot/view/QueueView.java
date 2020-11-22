package ru.bez_createha.queue_bot.view;

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
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Component
public class QueueView implements CallbackCommand {
    private final InlineButton telegramUtil;
    private final QueueService queueService;
    private final GroupService groupService;
    private final UserContext userContext;

    public QueueView(InlineButton telegramUtil, QueueService queueService, GroupService groupService, UserContext userContext) {
        this.telegramUtil = telegramUtil;
        this.queueService = queueService;
        this.groupService = groupService;
        this.userContext = userContext;
    }

    @Override
    public Predicate<String> statePredicate() {
        return s -> s.equals(State.GROUP_MENU.toString());
    }

    @Override
    public Predicate<CallbackQuery> callbackPredicate() {
        return callbackQuery -> callbackQuery.getData().split("::")[0].equals("group");
    }

    public void process(CallbackQuery callbackQuery, User user, Bot bot) throws TelegramApiException {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        Group group = groupService.findByChatId(Long.valueOf(callbackQuery.getData().split("::")[1]));
        userContext.getUserStaff(user.getUserId()).setGroup(group);

        user.setMessageId(callbackQuery.getMessage().getMessageId());
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<Queue> queues = queueService.findAllByGroupId(group);
        List<InlineKeyboardButton> inlineKeyboardButtonsRow = new ArrayList<>();

        int i;
        for (i = 0; i < queues.size(); i++) {
            InlineKeyboardButton button =
                    telegramUtil.createInlineKeyboardButton(
                            queues.get(i).getTag(),
                            "queue::" + queues.get(i).getId()+"::"+queues.get(i).getTag()
                    );
            inlineKeyboardButtonsRow.add(button);
            if (i % 2 == 1) {
                keyboard.add(inlineKeyboardButtonsRow);
                inlineKeyboardButtonsRow = new ArrayList<>();
            }
        }
        if (i % 2 == 1){
            keyboard.add(inlineKeyboardButtonsRow);
        }

        keyboard.add(Collections.singletonList(telegramUtil.createInlineKeyboardButton(
                "Создать очередь",
                "create_queue"
        )));
        keyboard.add(Collections.singletonList(telegramUtil.createInlineKeyboardButton(
                "Назад",
                "back"
        )));
        inlineKeyboardMarkup.setKeyboard(keyboard);

        user.setBotState(State.QUEUE_MENU.toString());
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setChatId(callbackQuery.getMessage().getChatId().toString());
        if (queues.isEmpty()) {
            editMessageText.setText("Очередей пока нет! Но Вы всегда можете их добавить");
        } else {
            editMessageText.setText("Это список очередей для вашей группы с названием: " + group.getName());
        }
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);
        bot.execute(editMessageText);
    }


}
