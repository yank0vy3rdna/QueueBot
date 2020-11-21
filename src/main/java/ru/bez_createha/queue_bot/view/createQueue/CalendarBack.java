package ru.bez_createha.queue_bot.view.createQueue;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.bez_createha.queue_bot.context.UserContext;
import ru.bez_createha.queue_bot.model.State;
import ru.bez_createha.queue_bot.model.User;
import ru.bez_createha.queue_bot.view.CallbackCommand;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Component
public class CalendarBack implements CallbackCommand {

    private final UserContext userContext;

    public CalendarBack(UserContext userContext) {
        this.userContext = userContext;
    }

    @Override
    public Predicate<String> statePredicate() {
        return s -> s.equals(State.ENTER_QUEUE_DATE.toString());
    }

    @Override
    public Predicate<CallbackQuery> callbackPredicate() {
        return callbackQuery -> callbackQuery.getData().split("::")[0].equals("calendar_back");
    }

    @Override
    public List<BotApiMethod<? extends Serializable>> process(CallbackQuery callbackQuery, User user) {

        SimpleCalendar simpleCalendar = userContext.getUserStaff(user.getUserId()).getSimpleCalendar();

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        simpleCalendar.decreaseMonthNum();
        inlineKeyboardMarkup.setKeyboard(simpleCalendar.createCalendar(callbackQuery.getData().split("::")[1]));

        List<BotApiMethod<? extends Serializable>> methods = new ArrayList<>();

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setMessageId(user.getMessageId());
        editMessageText.setChatId(callbackQuery.getMessage().getChatId().toString());
        editMessageText.setText("Вы успешно создали очередь с именем: "+callbackQuery.getData().split("::")[1]+"\nТеперь нужно выбрать дату и время начала очереди");
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);

        methods.add(editMessageText);
        return methods;
    }
}
