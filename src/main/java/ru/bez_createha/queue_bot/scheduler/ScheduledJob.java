package ru.bez_createha.queue_bot.scheduler;

import lombok.Data;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bez_createha.queue_bot.Bot;
import ru.bez_createha.queue_bot.model.Queue;
import ru.bez_createha.queue_bot.utils.InlineButton;

import java.util.Collections;
import java.util.TimerTask;

@Data
public class ScheduledJob extends TimerTask {

    private final Bot bot;
    private final InlineButton telegramUtil;
    private Queue queue;

    public ScheduledJob(Bot bot, InlineButton telegramUtil) {
        this.bot = bot;
        this.telegramUtil = telegramUtil;
    }

    @Override
    public void run() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(queue.getGroupId().getChatId().toString());
        sendMessage.setText("Очередь запущена!");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(
                Collections.singletonList(
                        Collections.singletonList(
                                telegramUtil.createInlineKeyboardButton("Записаться",
                                        "join_queue::" +
                                                queue.getId() +
                                                "::" +
                                                queue.getGroupId().getId()
                                        )
                        )
                )
        );
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException ignored) {
        }
    }

}
