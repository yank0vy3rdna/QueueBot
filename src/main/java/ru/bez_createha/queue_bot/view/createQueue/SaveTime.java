package ru.bez_createha.queue_bot.view.createQueue;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
import ru.bez_createha.queue_bot.view.backs.BackQueueCreated;
import ru.bez_createha.queue_bot.view.backs.BackResultOfQueue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;

@Component
public class SaveTime implements MessageCommand {
    private final UserContext userContext;
    private final QueueService queueService;
    private final InlineButton telegramUtil;
    private final QueueScheduler queueScheduler;
    private final BackQueueCreated backQueueCreated;


    public SaveTime(UserContext userContext, QueueService queueService, InlineButton telegramUtil, QueueScheduler queueScheduler, BackResultOfQueue backResultOfQueue, BackQueueCreated backQueueCreated) {
        this.userContext = userContext;
        this.queueService = queueService;
        this.telegramUtil = telegramUtil;
        this.queueScheduler = queueScheduler;
        this.backQueueCreated = backQueueCreated;
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
        String[] splitted = message.getText().split(":");

        Integer chosen_hour = 0;
        Integer chosen_minets = 0;

        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(message.getChatId().toString());
        deleteMessage.setMessageId(message.getMessageId());
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setMessageId(user.getMessageId());
        editMessageText.setChatId(message.getChatId().toString());

        try {
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            keyboard.add(Collections.singletonList(telegramUtil.createInlineKeyboardButton(
                    "Назад",
                    "back"
            )));
            inlineKeyboardMarkup.setKeyboard(keyboard);
            editMessageText.setReplyMarkup(inlineKeyboardMarkup);

            chosen_hour = Integer.valueOf(splitted[0]);
            chosen_minets = Integer.valueOf(splitted[1]);

            Integer day_start = userContext.getUserStaff(user.getUserId()).getRawQueue().getDay_start();
            Integer month_start = userContext.getUserStaff(user.getUserId()).getRawQueue().getMonth_start();
            Integer year_start = userContext.getUserStaff(user.getUserId()).getRawQueue().getYear_start();

            if (checkIfDeprecatedTime(year_start, month_start, day_start, chosen_hour, chosen_minets)) {

                editMessageText.setText("Указанное время должно быть с запасов как минимум 15 минут.\nПопробуйте ввести время в формате HH:mm снова или вернитесь назад.");

                bot.execute(editMessageText);
            }else {
                user.setBotState(State.GROUP_MENU.toString());

                simpleDateFormat.parse(message.getText());

                RawQueue rawQueue = userContext.getUserStaff(user.getUserId()).getRawQueue();
                rawQueue.setHrs_start(chosen_hour);
                rawQueue.setMin_start(chosen_minets);
                Date date = rawQueue.buildDate();
                Queue queue = new Queue();
                queue.setGroupId(userContext.getUserStaff(user.getUserId()).getGroup());
                queue.setStatus(QueueStatus.NOT_STARTED);
                queue.setStartTime(date);
                queue.setTag(userContext.getUserStaff(user.getUserId()).getRawQueue().getName());
                queueService.save(queue);

                editMessageText.setReplyMarkup(inlineKeyboardMarkup);

                queueScheduler.createJob(queue, bot);
                editMessageText.setText("Очередь создана");


                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(queue.getGroupId().getChatId()));
                sendMessage.setText("Очередь \"" + queue.getTag() + "\" создана.\nНачало: " + date.toString());
                bot.execute(sendMessage);
                bot.execute(editMessageText);
            }
        }catch (ParseException | NumberFormatException exception) {
            editMessageText.setText("Неверный формат. Введи время в формате HH:mm");
            bot.execute(editMessageText);
        }
        bot.execute(deleteMessage);
    }

    private boolean checkIfDeprecatedTime(Integer year, Integer month, Integer day, Integer hours, Integer minutes) {
        SimpleDateFormat time_formate = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Integer current_day = Calendar.getInstance().get(Calendar.DATE);
        Integer current_month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        Integer current_year = Calendar.getInstance().get(Calendar.YEAR);
        Integer current_hour = Calendar.getInstance().get(Calendar.HOUR);
        Integer current_minute = Calendar.getInstance().get(Calendar.MINUTE) + 15;

        Date chosenHourseAndMinutes = new Date();
        Date tempHourseAndMinutes = new Date();

        try {
            chosenHourseAndMinutes = time_formate.parse(year + "-" + month + "-" + day + " " + hours+":"+minutes);
            tempHourseAndMinutes = time_formate.parse(current_year + "-" + current_month + "-" + current_day + " " + current_hour + ":" + current_minute);
        }catch (ParseException ex) {/*NOPE*/}

        return chosenHourseAndMinutes.before(tempHourseAndMinutes);
    }
}
