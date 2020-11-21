package ru.bez_createha.queue_bot.controller;

import ru.bez_createha.queue_bot.model.User;
import ru.bez_createha.queue_bot.view.CallbackCommand;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CallbackInvoker {

    private final List<CallbackCommand> commands;

    public CallbackInvoker() {
        this.commands = new ArrayList<>();
    }
    public void register(CallbackCommand command){
        commands.add(command);
    }

    public List<BotApiMethod<? extends Serializable>> process(CallbackQuery callbackQuery, User user) {
        for (CallbackCommand command : commands) {
            if(command.statePredicate().test(user.getBotState()) && command.callbackPredicate().test(callbackQuery)){
                return command.process(callbackQuery, user);
            }
        }
        return null;
    }
}
