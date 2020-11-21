package ru.bez_createha.queue_bot.view.createQueue;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bez_createha.queue_bot.Bot;
import ru.bez_createha.queue_bot.context.RawQueue;
import ru.bez_createha.queue_bot.context.UserContext;
import ru.bez_createha.queue_bot.model.Queue;
import ru.bez_createha.queue_bot.model.QueueStatus;
import ru.bez_createha.queue_bot.model.State;
import ru.bez_createha.queue_bot.model.User;
import ru.bez_createha.queue_bot.scheduler.QueueScheduler;
import ru.bez_createha.queue_bot.services.QueueService;
import ru.bez_createha.queue_bot.utils.InlineButton;
import ru.bez_createha.queue_bot.view.MessageCommand;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

@Component
public class SaveTime implements MessageCommand {
    private final UserContext userContext;
    private final QueueService queueService;
    private final InlineButton telegramUtil;
    private final QueueScheduler queueScheduler;


    public SaveTime(UserContext userContext, QueueService queueService, InlineButton telegramUtil, QueueScheduler queueScheduler) {
        this.userContext = userContext;
        this.queueService = queueService;
        this.telegramUtil = telegramUtil;
        this.queueScheduler = queueScheduler;
    }

    @Override
    public Predicate<String> statePredicate() {
        return s -> s.equals(State.ENTER_QUEUE_TIME.toString());
    }

    @Override
    public Predicate<Message> messagePredicate() {
        return message -> true;
    }

    @Override
    public void process(Message message, User user, Bot bot) throws TelegramApiException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setMessageId(user.getMessageId());
        editMessageText.setChatId(message.getChatId().toString());
        try {
            simpleDateFormat.parse(message.getText());
            String[] splitted = message.getText().split(":");

            RawQueue rawQueue = userContext.getUserStaff(user.getUserId()).getRawQueue();
            rawQueue.setHrs_start(Integer.valueOf(splitted[0]));
            rawQueue.setMin_start(Integer.valueOf(splitted[1]));
            Date date = rawQueue.buildDate();
            Queue queue = new Queue();
            queue.setGroupId(userContext.getUserStaff(user.getUserId()).getGroup());
            queue.setStatus(QueueStatus.NOT_STARTED);
            queue.setStartTime(date);
            queue.setTag(userContext.getUserStaff(user.getUserId()).getRawQueue().getName());
            queueService.save(queue);
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            keyboard.add(Collections.singletonList(telegramUtil.createInlineKeyboardButton(
                    "Назад",
                    "back"
            )));
            inlineKeyboardMarkup.setKeyboard(keyboard);
            editMessageText.setReplyMarkup(inlineKeyboardMarkup);
//            try {
            queueScheduler.createJob(queue, bot);
            editMessageText.setText("Очередь создана");
//            } catch (SchedulerException e) {
//                //
//                editMessageText.setText("Матовый");
//            }
        } catch (ParseException exception) {
            editMessageText.setText("Неверный формат. Введи время в формате HH:mm");
        }
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(message.getChatId().toString());
        deleteMessage.setMessageId(message.getMessageId());
        bot.execute(editMessageText);
        bot.execute(deleteMessage);
    }
}
