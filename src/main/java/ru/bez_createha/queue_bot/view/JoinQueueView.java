package ru.bez_createha.queue_bot.view;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bez_createha.queue_bot.Bot;
import ru.bez_createha.queue_bot.model.Queue;
import ru.bez_createha.queue_bot.model.User;
import ru.bez_createha.queue_bot.services.QueueService;

import java.util.function.Predicate;

@Component
public class JoinQueueView implements CallbackCommand {
    private final QueueService queueService;

    public JoinQueueView(QueueService queueService) {
        this.queueService = queueService;
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
        Long queue_id = Long.valueOf(callbackQuery.getData().split("::")[1]);
        Long group_id = Long.valueOf(callbackQuery.getData().split("::")[2]);
        Queue queue = queueService.getById(queue_id);
        if (queue.getGroupId().getId().equals(group_id)) {
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
        stringBuilder.append("Очередь запущена!");
        if (!queue.getQueue_users().isEmpty()) {
            stringBuilder.append("\n\nОчередь:\n");
            for (User user_in_queue : queue.getQueue_users()) {
                stringBuilder.append("\n");
                stringBuilder.append(user_in_queue.getName());
            }
        }
        editMessageText.setText(stringBuilder.toString());
        bot.execute(editMessageText);
    }
}
