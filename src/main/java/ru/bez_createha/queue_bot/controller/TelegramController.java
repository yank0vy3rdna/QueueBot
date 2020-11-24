package ru.bez_createha.queue_bot.controller;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bez_createha.queue_bot.Bot;
import ru.bez_createha.queue_bot.context.UserContext;
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
    private final UserContext userContext;


    public TelegramController(UserService userService, CallbackInvoker callbackInvoker, MessageInvoker messageInvoker, UserContext userContext) {
        this.userService = userService;
        this.callbackInvoker = callbackInvoker;
        this.messageInvoker = messageInvoker;
        this.userContext = userContext;
    }

    public void registerCallbackHandler(CallbackCommand command){
        callbackInvoker.register(command);
    }
    public void registerMessageHandler(MessageCommand command){
        messageInvoker.register(command);
    }

    public void onCallbackQuery(CallbackQuery callbackQuery, Bot bot) throws TelegramApiException {
        User user = userService.findByUserId(callbackQuery.getFrom());
        if (user.getName() == null){
            if (callbackQuery.getFrom().getFirstName() !=null) {
                user.setName(callbackQuery.getFrom().getFirstName());
            }else {
                user.setName("Ник можно изменить в л/с бота");
            }
        }
        userContext.initUser(user.getUserId());
        callbackInvoker.process(callbackQuery, user, bot);
        userService.saveUser(user);
    }

    public void onMessage(Message message, Bot bot) throws TelegramApiException {
        User user = userService.findByUserId(message.getFrom());
        if (user.getName() == null){
            if (message.getFrom().getFirstName() !=null) {
                user.setName(message.getFrom().getFirstName());
            }else {
                user.setName("Ник можно изменить в л/с бота");
            }
        }
        messageInvoker.process(message, user, bot);
        userService.saveUser(user);
    }

}
