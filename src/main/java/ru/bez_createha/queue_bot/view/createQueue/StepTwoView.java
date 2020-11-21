package ru.bez_createha.queue_bot.view.createQueue;

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

    @Override
    public Predicate<String> statePredicate() {
        return s -> s.equals(State.ENTER_QUEUE_NAME.toString());
    }

    @Override
    public Predicate<Message> messagePredicate() {
        return message -> true;
    }

    public List<BotApiMethod<? extends Serializable>> process(Message message, User user) {

        user.setBotState(State.ENTER_QUEUE_DATE.toString());
        List<BotApiMethod<? extends Serializable>> methods = new ArrayList<>();

        String QUEUE_NAME = message.getText();


        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setMessageId(user.getMessageId());
        editMessageText.setChatId(message.getChatId().toString());
        editMessageText.setText("Вы успешно создали очередь с именем: "+QUEUE_NAME+"\nТеперь нужно выбрать дату и время начала очереди");

        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(message.getChatId().toString());
        deleteMessage.setMessageId(message.getMessageId());
        methods.add(deleteMessage);
        methods.add(editMessageText);
        return methods;
    }
}
