package ru.bez_createha.queue_bot.view.createQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bez_createha.queue_bot.utils.InlineButton;
import java.util.*;

@Component
public class SimpleCalendar {


    private final InlineButton telegramUtil;

    private List<String> monthsInRussian = Arrays.asList(
            "",
            "Январь", "Февраль", "Март",
            "Апрель", "Май", "Июнь",
            "Июль", "Авгус", "Сентябрь",
            "Октябрь", "Ноябрь", "Декабрь"
    );
    private String[] dayInRussian = new String[] {"Пн","Вт","Ср","Чт","Пт","Сб","ВС"};
    private List<Integer> numberOfDaysInEveryMonth = Arrays.asList(0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
    private int tempMonth;
    private int year;


    public SimpleCalendar(InlineButton inlineButton) {

        tempMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        year = Calendar.getInstance().get(Calendar.YEAR);
        this.telegramUtil = inlineButton;
    }

    // if received calendar_forward
    public void increaseMonthNum() {
        tempMonth++;
        if (tempMonth > 12) {
            tempMonth = 1;
            year++;
        }
    }

    // if received calendar_back
    public void decreaseMonthNum() {
        tempMonth--;
        if (tempMonth < 1) {
            tempMonth = 12;
            year--;
        }
    }

    public List<List<InlineKeyboardButton>> createCalendar() {
        List<List<InlineKeyboardButton>> keyboardButtons = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        int numberOfDaysInTempMonth = numberOfDaysInEveryMonth.get(tempMonth);

        //adding disabled month button
        keyboardButtons.add(Collections.singletonList(telegramUtil.createInlineKeyboardButton(monthsInRussian.get(tempMonth)+" "+year+"=)","nope")));

        //creating row of disabled days buttons
        for (String day: dayInRussian) {
            row.add(telegramUtil.createInlineKeyboardButton(day,"nope"));
        }
        keyboardButtons.add(row);

        //clear it before creating day buttons
        row = new ArrayList<>();

        int startingDay = dayShift(tempMonth, 0, year);
        //creating action buttons of days
        for (int j = 1; j < startingDay + 1; j++) {
            row.add(telegramUtil.createInlineKeyboardButton(" ", "nope"));
            if (j % 7 == 0) {
                keyboardButtons.add(row);
                row = new ArrayList<>();
            }
        }

        for (int i = 1 ; i <= numberOfDaysInTempMonth; i++) {
            row.add(telegramUtil.createInlineKeyboardButton(String.valueOf(i), "save_date::"+ i+"/"+tempMonth+"/"+year));
            if ((i + startingDay) % 7 == 0) {
                keyboardButtons.add(row);
                row = new ArrayList<>();
            }
        }

        if (row.size() < 7) {
            int lastRowSize = row.size();
            for (int i = 7; i > lastRowSize; i-- ) {
                row.add(telegramUtil.createInlineKeyboardButton(" ", "nope"));
            }
            keyboardButtons.add(row);
        }

        row = new ArrayList<>();

        row.add(telegramUtil.createInlineKeyboardButton("<", "calendar_back"));
        row.add(telegramUtil.createInlineKeyboardButton(" ", "nope"));
        row.add(telegramUtil.createInlineKeyboardButton(">", "calendar_forward"));
        keyboardButtons.add(row);

        row = new ArrayList<>();

        row.add(telegramUtil.createInlineKeyboardButton("Назад", "back"));
        keyboardButtons.add(row);

        return keyboardButtons;
    }

    public int dayShift(int M, int D, int Y) {
        int y = Y - (14 - M) / 12;
        int x = y + y/4 - y/100 + y/400;
        int m = M + 12 * ((14 - M) / 12) - 2;
        int d = (D + x + (31*m)/12) % 7;
        return d;
    }
}