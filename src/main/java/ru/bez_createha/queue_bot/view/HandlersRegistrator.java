package ru.bez_createha.queue_bot.view;


import ru.bez_createha.queue_bot.controller.TelegramController;
import ru.bez_createha.queue_bot.view.backs.BackGroupView;
import ru.bez_createha.queue_bot.view.createQueue.*;
import org.springframework.stereotype.Service;

@Service
public class HandlersRegistrator {
    private final GroupView groupView;
    private final GroupCreated groupCreated;
    private final QueueView queueView;
    private final BackGroupView backGroupView;
    private final StepOneView stepOneView;
    private final StepTwoView stepTwoView;
    private final CalendarBack calendarBack;
    private final CalendarForward calendarForward;
    private final SaveDate saveData;

    public HandlersRegistrator(GroupView groupView, GroupCreated groupCreated, QueueView queueView, BackGroupView backGroupView, StepOneView stepOneView, StepTwoView stepTwoView, CalendarBack calendarBack, CalendarForward calendarForward, SaveDate saveData) {
        this.groupView = groupView;
        this.stepTwoView = stepTwoView;
        this.groupCreated = groupCreated;
        this.queueView = queueView;
        this.backGroupView = backGroupView;
        this.stepOneView = stepOneView;
        this.calendarBack = calendarBack;
        this.calendarForward = calendarForward;
        this.saveData = saveData;
    }

    public void registerAllHandlers(TelegramController telegramController){
        telegramController.registerMessageHandler(groupCreated);
        telegramController.registerMessageHandler(stepTwoView);
        telegramController.registerCallbackHandler(queueView);
        telegramController.registerCallbackHandler(backGroupView);
        telegramController.registerCallbackHandler(stepOneView);
        telegramController.registerCallbackHandler(calendarBack);
        telegramController.registerCallbackHandler(calendarForward);
        telegramController.registerCallbackHandler(saveData);
        telegramController.registerMessageHandler(groupView);
    }
}
