package ru.bez_createha.queue_bot.view;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bez_createha.queue_bot.Bot;
import ru.bez_createha.queue_bot.model.User;

import java.util.function.Predicate;

public interface MessageCommand {
    Predicate<String> statePredicate();
    Predicate<Message> messagePredicate();
    void process(Message message, User user, Bot bot) throws TelegramApiException;
}
