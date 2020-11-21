package ru.bez_createha.queue_bot.view.backs;

import ru.bez_createha.queue_bot.view.CallbackCommand;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.function.Predicate;

public interface Back extends CallbackCommand {
    @Override
    default Predicate<CallbackQuery> callbackPredicate(){
        return callbackQuery -> callbackQuery.getData().equals("back");
    }
}
