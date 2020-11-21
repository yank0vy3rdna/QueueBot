package ru.bez_createha.queue_bot.view.createQueue;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bez_createha.queue_bot.Bot;
import ru.bez_createha.queue_bot.context.UserContext;
import ru.bez_createha.queue_bot.model.State;
import ru.bez_createha.queue_bot.model.User;
import ru.bez_createha.queue_bot.view.CallbackCommand;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Component
public class CalendarForward implements CallbackCommand {

    private final UserContext userContext;

    public CalendarForward(UserContext userContext) {
        this.userContext = userContext;
    }


    @Override
    public Predicate<String> statePredicate() {
        return s -> s.equals(State.ENTER_QUEUE_DATE.toString());
    }

    @Override
    public Predicate<CallbackQuery> callbackPredicate() {
        return callbackQuery -> callbackQuery.getData().split("::")[0].equals("calendar_forward");
    }

    @Override
    public void process(CallbackQuery callbackQuery, User user, Bot bot) throws TelegramApiException {

        SimpleCalendar simpleCalendar = userContext.getUserStaff(user.getUserId()).getSimpleCalendar();


        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        simpleCalendar.increaseMonthNum();
        inlineKeyboardMarkup.setKeyboard(simpleCalendar.createCalendar());

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setMessageId(user.getMessageId());
        editMessageText.setChatId(callbackQuery.getMessage().getChatId().toString());
        editMessageText.setText("Вы успешно создали очередь с именем: " + userContext.getUserStaff(user.getUserId()).getRawQueue().getName() + "\nТеперь нужно выбрать дату и время начала очереди");
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);
        bot.execute(editMessageText);
    }
}
