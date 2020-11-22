package ru.bez_createha.queue_bot.view.createQueue;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bez_createha.queue_bot.Bot;
import ru.bez_createha.queue_bot.context.UserContext;
import ru.bez_createha.queue_bot.model.State;
import ru.bez_createha.queue_bot.model.User;
import ru.bez_createha.queue_bot.utils.InlineButton;
import ru.bez_createha.queue_bot.view.CallbackCommand;
import ru.bez_createha.queue_bot.view.backs.BackTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;

@Component
public class SaveDate implements CallbackCommand{

    private final BackTime backTime;
    private final UserContext userContext;
    private final InlineButton telegramUtil;
    public SaveDate(BackTime backTime, UserContext userContext, InlineButton telegramUtil) {
        this.backTime = backTime;
        this.userContext = userContext;
        this.telegramUtil = telegramUtil;
    }


    @Override
    public Predicate<String> statePredicate() {
        return s -> s.equals(State.ENTER_QUEUE_DATE.toString());
    }

    @Override
    public Predicate<CallbackQuery> callbackPredicate() {
        return callbackQuery -> callbackQuery.getData().split("::")[0].equals("save_date");
    }

    @Override
    public void process(CallbackQuery callbackQuery, User user, Bot bot) throws TelegramApiException {
        Integer day_start = Integer.valueOf(callbackQuery.getData().split("::")[1].split("/")[0]);
        Integer month_start = Integer.valueOf(callbackQuery.getData().split("::")[1].split("/")[1]);
        Integer year_start = Integer.valueOf(callbackQuery.getData().split("::")[1].split("/")[2]);

        if (checkIfDataInThePast(day_start, month_start, year_start)) {
            AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
            answerCallbackQuery.setShowAlert(true);
            answerCallbackQuery.setText("Выбрана устаревшая дата!");
            answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
            bot.execute(answerCallbackQuery);
            backTime.process(callbackQuery,user,bot);
        }else {

            user.setBotState(State.ENTER_QUEUE_TIME.toString());
            user.setMessageId(callbackQuery.getMessage().getMessageId());

            userContext.getUserStaff(user.getUserId()).getRawQueue().setDay_start(day_start);
            userContext.getUserStaff(user.getUserId()).getRawQueue().setMonth_start(month_start);
            userContext.getUserStaff(user.getUserId()).getRawQueue().setYear_start(year_start);

            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setMessageId(user.getMessageId());
            editMessageText.setChatId(callbackQuery.getMessage().getChatId().toString());
            editMessageText.setText("Выберите время начала.\n Для этого напишите в чат время в формате HH:mm");

            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            keyboard.add(Collections.singletonList(telegramUtil.createInlineKeyboardButton(
                    "Назад",
                    "back"
            )));
            inlineKeyboardMarkup.setKeyboard(keyboard);
            editMessageText.setReplyMarkup(inlineKeyboardMarkup);
            bot.execute(editMessageText);
        }
    }

    //dont punish please
    protected boolean checkIfDataInThePast(Integer day,Integer month,Integer year){
        Integer current_day = Calendar.getInstance().get(Calendar.DATE);
        Integer current_month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        Integer current_year = Calendar.getInstance().get(Calendar.YEAR);

        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");

        Date tempData = new Date();
        Date chosenData = new Date();
        try {
             chosenData = date.parse(year + "-" + month + "-" + day);
             tempData = date.parse(current_year + "-" + current_month + "-" + current_day);
        }catch (ParseException ex) {/*NOPE*/}

        return tempData.after(chosenData);
    }
}
