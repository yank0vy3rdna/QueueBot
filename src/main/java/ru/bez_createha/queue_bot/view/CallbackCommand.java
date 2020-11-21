package ru.bez_createha.queue_bot.view;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bez_createha.queue_bot.Bot;
import ru.bez_createha.queue_bot.model.User;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.io.Serializable;
import java.util.List;
import java.util.function.Predicate;

public interface CallbackCommand {
    Predicate<String> statePredicate();
    Predicate<CallbackQuery> callbackPredicate();
    void process(CallbackQuery callbackQuery, User user, Bot bot) throws TelegramApiException;
}
