package ru.bez_createha.queue_bot.view.backs;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bez_createha.queue_bot.Bot;
import ru.bez_createha.queue_bot.model.State;
import ru.bez_createha.queue_bot.model.User;
import ru.bez_createha.queue_bot.view.GroupView;

import java.util.function.Predicate;

@Component
public class BackQueueCreated implements Back{
    private final GroupView groupView;

    public BackQueueCreated(GroupView groupView) {
        this.groupView = groupView;
    }

    @Override
    public Predicate<String> statePredicate() {
        return s -> s.equals(State.GROUP_MENU.toString());
    }

    @Override
    public void process(CallbackQuery callbackQuery, User user, Bot bot) throws TelegramApiException {
        groupView.process(callbackQuery.getMessage(), user, bot);
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(callbackQuery.getMessage().getChatId().toString());
        deleteMessage.setMessageId(callbackQuery.getMessage().getMessageId());
        bot.execute(deleteMessage);
    }
}
