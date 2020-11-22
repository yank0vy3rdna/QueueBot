package ru.bez_createha.queue_bot.view.backs;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bez_createha.queue_bot.Bot;
import ru.bez_createha.queue_bot.model.State;
import ru.bez_createha.queue_bot.model.User;
import ru.bez_createha.queue_bot.view.createQueue.StepOneView;

import java.util.function.Predicate;

@Component
public class BackCalendar implements Back {
    private final StepOneView stepOneView;

    public BackCalendar(StepOneView stepOneView) {
        this.stepOneView = stepOneView;
    }

    @Override
    public Predicate<String> statePredicate() {
        return s -> s.equals(State.ENTER_QUEUE_DATE.toString());
    }

    @Override
    public void process(CallbackQuery callbackQuery, User user, Bot bot) throws TelegramApiException {
        stepOneView.process(callbackQuery, user, bot);
    }
}
