package ru.bez_createha.queue_bot.view.createQueue;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bez_createha.queue_bot.Bot;
import ru.bez_createha.queue_bot.context.UserContext;
import ru.bez_createha.queue_bot.model.State;
import ru.bez_createha.queue_bot.model.User;
import ru.bez_createha.queue_bot.view.CallbackCommand;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

@Component
public class SaveDate implements CallbackCommand{

    private final UserContext userContext;

    public SaveDate(UserContext userContext) {
        this.userContext = userContext;
    }


    @Override
    public Predicate<String> statePredicate() {
        return s -> s.equals(State.ENTER_QUEUE_DATE.toString());
    }

    @Override
    public Predicate<CallbackQuery> callbackPredicate() {
        return callbackQuery -> callbackQuery.getData().split("::")[0].equals("save_date");
    }

    @Override
    public void process(CallbackQuery callbackQuery, User user, Bot bot) throws TelegramApiException {
        user.setBotState(State.ENTER_QUEUE_TIME.toString());
        user.setMessageId(callbackQuery.getMessage().getMessageId());

        userContext.getUserStaff(user.getUserId()).getRawQueue().setDay_start(Integer.valueOf(callbackQuery.getData().split("::")[1].split("/")[0]));
        userContext.getUserStaff(user.getUserId()).getRawQueue().setMonth_start(Integer.valueOf(callbackQuery.getData().split("::")[1].split("/")[1]));
        userContext.getUserStaff(user.getUserId()).getRawQueue().setYear_start(Integer.valueOf(callbackQuery.getData().split("::")[1].split("/")[2]));


        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setMessageId(user.getMessageId());
        editMessageText.setChatId(callbackQuery.getMessage().getChatId().toString());
        editMessageText.setText("Выберите время начала.\n Для этого напишите в чат время в формате HH:mm");
        bot.execute(editMessageText);
    }
}
