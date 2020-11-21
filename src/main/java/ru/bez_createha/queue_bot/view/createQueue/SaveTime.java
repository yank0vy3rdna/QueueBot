package ru.bez_createha.queue_bot.view.createQueue;

import org.quartz.SchedulerException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bez_createha.queue_bot.context.RawQueue;
import ru.bez_createha.queue_bot.context.UserContext;
import ru.bez_createha.queue_bot.model.Queue;
import ru.bez_createha.queue_bot.model.QueueStatus;
import ru.bez_createha.queue_bot.model.State;
import ru.bez_createha.queue_bot.model.User;
import ru.bez_createha.queue_bot.scheduler.SchedulerService;
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
    private final SchedulerService schedulerService;
    private final InlineButton telegramUtil;


    public SaveTime(UserContext userContext, QueueService queueService, SchedulerService schedulerService, InlineButton telegramUtil) {
        this.userContext = userContext;
        this.queueService = queueService;
        this.schedulerService = schedulerService;
        this.telegramUtil = telegramUtil;
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
    public List<BotApiMethod<? extends Serializable>> process(Message message, User user) {
        List<BotApiMethod<? extends Serializable>> methods = new ArrayList<>();
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
            keyboard.add(Collections.singletonList( telegramUtil.createInlineKeyboardButton(
                    "Назад",
                    "back"
            )));
            inlineKeyboardMarkup.setKeyboard(keyboard);
            editMessageText.setReplyMarkup(inlineKeyboardMarkup);
            try {
                schedulerService.registerJob(queue);
                editMessageText.setText("Очередь создана");
            } catch (SchedulerException e) {
                editMessageText.setText("Матовый");
            }
        } catch (ParseException exception) {
            editMessageText.setText("Неверный формат. Введи время в формате HH:mm");
        }
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(message.getChatId().toString());
        deleteMessage.setMessageId(message.getMessageId());
        methods.add(editMessageText);
        methods.add(deleteMessage);
        return methods;
    }
}
