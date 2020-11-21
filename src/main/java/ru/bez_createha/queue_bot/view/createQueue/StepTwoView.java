package ru.bez_createha.queue_bot.view.createQueue;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bez_createha.queue_bot.Bot;
import ru.bez_createha.queue_bot.context.RawQueue;
import ru.bez_createha.queue_bot.context.UserContext;
import ru.bez_createha.queue_bot.model.State;
import ru.bez_createha.queue_bot.model.User;
import ru.bez_createha.queue_bot.view.MessageCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Component
public class StepTwoView implements MessageCommand {

    private final UserContext userContext;

    public StepTwoView(UserContext userContext) {
        this.userContext = userContext;
    }

    @Override
    public Predicate<String> statePredicate() {
        return s -> s.equals(State.ENTER_QUEUE_NAME.toString());
    }

    @Override
    public Predicate<Message> messagePredicate() {
        return message -> true;
    }

    public void process(Message message, User user, Bot bot) throws TelegramApiException {
        String QUEUE_NAME = message.getText();

        SimpleCalendar simpleCalendar = userContext.getUserStaff(user.getUserId()).getSimpleCalendar();
        RawQueue rawQueue = userContext.getUserStaff(user.getUserId()).getRawQueue();
        rawQueue.setName(QUEUE_NAME);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(simpleCalendar.createCalendar());
        user.setBotState(State.ENTER_QUEUE_DATE.toString());
        List<BotApiMethod<? extends Serializable>> methods = new ArrayList<>();


        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setMessageId(user.getMessageId());
        editMessageText.setChatId(message.getChatId().toString());
        editMessageText.setText("Вы успешно создали очередь с именем: " + QUEUE_NAME + "\nТеперь нужно выбрать дату и время начала очереди");
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);

        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(message.getChatId().toString());
        deleteMessage.setMessageId(message.getMessageId());
        bot.execute(editMessageText);
        bot.execute(deleteMessage);

    }
}
