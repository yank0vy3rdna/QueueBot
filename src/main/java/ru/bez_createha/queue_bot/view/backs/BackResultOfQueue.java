package ru.bez_createha.queue_bot.view.backs;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bez_createha.queue_bot.Bot;
import ru.bez_createha.queue_bot.context.UserContext;
import ru.bez_createha.queue_bot.model.Queue;
import ru.bez_createha.queue_bot.model.State;
import ru.bez_createha.queue_bot.model.User;
import ru.bez_createha.queue_bot.view.QueueAdminMenu;

import java.util.function.Predicate;

@Component
public class BackResultOfQueue implements Back {
    private final QueueAdminMenu queueAdminMenu;
    private final UserContext userContext;

    public BackResultOfQueue(QueueAdminMenu queueAdminMenu, UserContext userContext) {
        this.queueAdminMenu = queueAdminMenu;
        this.userContext = userContext;
    }

    @Override
    public Predicate<String> statePredicate() {
        return s -> s.equals(State.QUEUE_MENU_RESULT.toString());
    }

    @Override
    public void process(CallbackQuery callbackQuery, User user, Bot bot) throws TelegramApiException {
        Queue queue = userContext.getUserStaff(user.getUserId()).getQueue();
        callbackQuery.setData("queue::"+queue.getId());
        queueAdminMenu.process(callbackQuery, user, bot);
    }
}
