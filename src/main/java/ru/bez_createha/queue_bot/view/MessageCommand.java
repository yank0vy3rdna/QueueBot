package ru.bez_createha.queue_bot.view;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.bez_createha.queue_bot.model.User;

import java.io.Serializable;
import java.util.List;
import java.util.function.Predicate;

public interface MessageCommand {
    Predicate<String> statePredicate();
    Predicate<Message> messagePredicate();
    List<BotApiMethod<? extends Serializable>> process(Message message, User user) ;
}
