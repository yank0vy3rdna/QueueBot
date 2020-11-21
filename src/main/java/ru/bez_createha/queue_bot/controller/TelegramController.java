package ru.bez_createha.queue_bot.controller;

import ru.bez_createha.queue_bot.model.User;
import ru.bez_createha.queue_bot.services.UserService;
import ru.bez_createha.queue_bot.view.CallbackCommand;
import ru.bez_createha.queue_bot.view.MessageCommand;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.Serializable;
import java.util.List;

@Controller
public class TelegramController {
    private final UserService userService;
    private final CallbackInvoker callbackInvoker;
    private final MessageInvoker messageInvoker;


    public TelegramController(UserService userService, CallbackInvoker callbackInvoker, MessageInvoker messageInvoker) {
        this.userService = userService;
        this.callbackInvoker = callbackInvoker;
        this.messageInvoker = messageInvoker;
    }

    public void registerCallbackHandler(CallbackCommand command){
        callbackInvoker.register(command);
    }
    public void registerMessageHandler(MessageCommand command){
        messageInvoker.register(command);
    }

    public List<BotApiMethod<? extends Serializable>> onCallbackQuery(CallbackQuery callbackQuery) {
        User user = userService.findByUserId(callbackQuery.getFrom());
        List<BotApiMethod<? extends Serializable>> methods = callbackInvoker.process(callbackQuery, user);
        userService.saveUser(user);
        return methods;
    }

    public List<BotApiMethod<? extends Serializable>> onMessage(Message message) {
        User user = userService.findByUserId(message.getFrom());
        List<BotApiMethod<? extends Serializable>> methods = messageInvoker.process(message, user);
        userService.saveUser(user);
        return methods;
    }
}
