package ru.bez_createha.queue_bot.view.backs;

import ru.bez_createha.queue_bot.model.State;
import ru.bez_createha.queue_bot.model.User;
import ru.bez_createha.queue_bot.view.GroupView;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.io.Serializable;
import java.util.List;
import java.util.function.Predicate;

@Component
public class BackGroupView implements Back{
    private final GroupView groupView;

    public BackGroupView(GroupView groupView) {
        this.groupView = groupView;
    }

    @Override
    public Predicate<String> statePredicate() {
        return s -> s.equals(State.QUEUE_MENU.toString());
    }

    @Override
    public List<BotApiMethod<? extends Serializable>> process(CallbackQuery callbackQuery, User user) {
        List<BotApiMethod<? extends Serializable>> methods = groupView.process(callbackQuery.getMessage(), user);
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(callbackQuery.getMessage().getChatId().toString());
        deleteMessage.setMessageId(callbackQuery.getMessage().getMessageId());
        methods.add(deleteMessage);
        return methods;
    }
}
