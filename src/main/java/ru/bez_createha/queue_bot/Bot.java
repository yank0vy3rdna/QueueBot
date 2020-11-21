package ru.bez_createha.queue_bot;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bez_createha.queue_bot.controller.TelegramController;
import ru.bez_createha.queue_bot.scheduler.QueueScheduler;
import ru.bez_createha.queue_bot.view.HandlersRegistrator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class Bot extends TelegramLongPollingBot {
    private final TelegramController telegramController;
    private final QueueScheduler queueScheduler;
    @Value("${bot.name}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;

    public Bot(TelegramController telegramController, HandlersRegistrator handlersRegistrator, QueueScheduler queueScheduler) {
        this.telegramController = telegramController;
        this.queueScheduler = queueScheduler;

        handlersRegistrator.registerAllHandlers(this.telegramController);
        this.queueScheduler.initFromDb(this);
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
        try {
            if (update.hasCallbackQuery()) {
                telegramController.onCallbackQuery(update.getCallbackQuery(), this);
            } else if (update.hasMessage()) {
                telegramController.onMessage(update.getMessage(), this);
            }
        }catch (TelegramApiException ignored){

        }
    }
}
