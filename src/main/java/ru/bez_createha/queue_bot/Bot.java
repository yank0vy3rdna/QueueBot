package ru.bez_createha.queue_bot;

import ru.bez_createha.queue_bot.controller.TelegramController;
import ru.bez_createha.queue_bot.view.HandlersRegistrator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.util.List;

@Component
public class Bot extends TelegramLongPollingBot {
    private final TelegramController telegramController;

    @Value("${bot.name}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;

    public Bot(TelegramController telegramController, HandlersRegistrator handlersRegistrator) {
        this.telegramController = telegramController;

        handlersRegistrator.registerAllHandlers(this.telegramController);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }


    @Override
    public void onUpdateReceived(Update update) {
        List<BotApiMethod<? extends Serializable>> methods = null;
        if (update.hasCallbackQuery()) {
            methods = telegramController.onCallbackQuery(update.getCallbackQuery());
        } else if (update.hasMessage()) {
            methods = telegramController.onMessage(update.getMessage());
        }
        if (methods != null) {
            try {
                for (BotApiMethod<? extends Serializable> method : methods) {
                    execute(method);
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
