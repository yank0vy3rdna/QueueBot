package ru.bez_createha.queue_bot.view.backs;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bez_createha.queue_bot.Bot;
import ru.bez_createha.queue_bot.context.UserContext;
import ru.bez_createha.queue_bot.model.Group;
import ru.bez_createha.queue_bot.model.State;
import ru.bez_createha.queue_bot.model.User;
import ru.bez_createha.queue_bot.view.QueueView;

import java.util.function.Predicate;

@Component
public class BackDeleteQueue implements Back{
    private final QueueView queueView;
    private final UserContext userContext;

    public BackDeleteQueue(QueueView queueView, UserContext userContext) {
        this.queueView = queueView;
        this.userContext = userContext;
    }

    @Override
    public Predicate<String> statePredicate() {
        return s -> s.equals(State.QUEUE_MENU_DELETE.toString());
    }

    @Override
    public void process(CallbackQuery callbackQuery, User user, Bot bot) throws TelegramApiException {
        Group group = userContext.getUserStaff(user.getUserId()).getGroup();
        callbackQuery.setData("group::"+group.getChatId());
        queueView.process(callbackQuery, user, bot);
    }
}
