package ru.bez_createha.queue_bot.view.backs;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bez_createha.queue_bot.Bot;
import ru.bez_createha.queue_bot.model.State;
import ru.bez_createha.queue_bot.model.User;
import ru.bez_createha.queue_bot.view.createQueue.StepTwoView;

import java.util.function.Predicate;

@Component
public class BackTime implements Back {
    private final StepTwoView stepTwoView;

    public BackTime(StepTwoView stepTwoView) {
        this.stepTwoView = stepTwoView;
    }

    @Override
    public Predicate<String> statePredicate() {
        return s -> s.equals(State.ENTER_QUEUE_TIME.toString());
    }

    @Override
    public void process(CallbackQuery callbackQuery, User user, Bot bot) throws TelegramApiException {
        stepTwoView.process(callbackQuery, user, bot);
    }
}
