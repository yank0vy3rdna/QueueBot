package ru.bez_createha.queue_bot.view.createQueue;

import ru.bez_createha.queue_bot.model.State;
import ru.bez_createha.queue_bot.model.User;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
public class StepTwoView {

    public List<BotApiMethod<? extends Serializable>> process(Message message, User user) {
        user.setBotState(State.ENTER_QUEUE_DATE.toString());
        List<BotApiMethod<? extends Serializable>> methods = new ArrayList<>();
        user.setMessageId(message.getMessageId());
        String QUEUE_NAME = message.getText();
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(message.getChatId().toString());
        editMessageText.setMessageId(user.getMessageId());
        DeleteMessage deleteMessage = new DeleteMessage();
        methods.add(deleteMessage);
        return methods;
    }
}
