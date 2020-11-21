package ru.bez_createha.queue_bot.controller;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bez_createha.queue_bot.Bot;
import ru.bez_createha.queue_bot.model.User;
import ru.bez_createha.queue_bot.view.MessageCommand;
import ru.bez_createha.queue_bot.view.GroupView;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MessageInvoker {
    private final List<MessageCommand> commands;
    private final GroupView groupView;

    public MessageInvoker(GroupView groupView) {
        this.groupView = groupView;
        commands = new ArrayList<>();
    }

    public void register(MessageCommand command) {
        commands.add(command);
    }

    public void process(Message message, User user, Bot bot) throws TelegramApiException {
        for (MessageCommand command : commands) {
            if(command.statePredicate().test(user.getBotState()) && command.messagePredicate().test(message)){
                command.process(message, user, bot);
            }
        }
        groupView.process(message, user, bot);
    }
}
