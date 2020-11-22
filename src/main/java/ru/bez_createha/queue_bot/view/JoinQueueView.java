package ru.bez_createha.queue_bot.view;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bez_createha.queue_bot.Bot;
import ru.bez_createha.queue_bot.model.Queue;
import ru.bez_createha.queue_bot.model.QueueStatus;
import ru.bez_createha.queue_bot.model.User;
import ru.bez_createha.queue_bot.services.QueueService;
import ru.bez_createha.queue_bot.utils.InlineButton;

import java.util.Collections;
import java.util.function.Predicate;

@Component
public class JoinQueueView implements CallbackCommand {
    private final QueueService queueService;
    private final InlineButton telegramUtil;

    public JoinQueueView(QueueService queueService, InlineButton telegramUtil) {
        this.queueService = queueService;
        this.telegramUtil = telegramUtil;
    }

    @Override
    public Predicate<String> statePredicate() {
        return s -> true;
    }

    @Override
    public Predicate<CallbackQuery> callbackPredicate() {
        return callbackQuery -> callbackQuery.getData().split("::")[0].equals("join_queue");
    }

    @Override
    public void process(CallbackQuery callbackQuery, User user, Bot bot) throws TelegramApiException {

        String[] splitted = callbackQuery.getData().split("::");
        Long queue_id = Long.valueOf(splitted[1]);
        Long group_id = Long.valueOf(splitted[2]);
        Queue queue = queueService.getById(queue_id);
        if (queue.getGroupId().getId().equals(group_id) && queue.getStatus().equals(QueueStatus.ACTIVE)) {
            if (queue.getQueue_users().stream().noneMatch(
                    user1 -> user1.getUserId().equals(user.getUserId())
            )) {
                queueService.putUser(queue, user);
            } else {
                queueService.removeUser(queue, user);
            }
        }

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(callbackQuery.getMessage().getChatId().toString());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Очередь ");
        stringBuilder.append(queue.getTag());
        stringBuilder.append(" запущена!");
        if (!queue.getQueue_users().isEmpty()) {
            stringBuilder.append("\n\nОчередь:\n");
            for (User user_in_queue : queue.getQueue_users()) {
                stringBuilder.append("\n");
                stringBuilder.append(user_in_queue.getName());
            }
        }
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setText(stringBuilder.toString());
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(
                Collections.singletonList(
                        Collections.singletonList(
                                telegramUtil.createInlineKeyboardButton("Записаться/Отписаться",
                                        "join_queue::" +
                                                queue.getId() +
                                                "::" +
                                                queue.getGroupId().getId()
                                )
                        )
                )
        );
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);
        bot.execute(editMessageText);
    }
}
