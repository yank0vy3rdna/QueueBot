package ru.bez_createha.queue_bot.view;


import ru.bez_createha.queue_bot.controller.TelegramController;
import ru.bez_createha.queue_bot.view.backs.BackCalendar;
import ru.bez_createha.queue_bot.view.backs.BackGroupView;
import ru.bez_createha.queue_bot.view.backs.BackQueueName;
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
    private final SaveTime saveTime;
    private final JoinQueueView joinQueueView;
    private final GroupDeleted groupDeleted;
    private final BackQueueName backQueueName;
    private final BackCalendar backCalendar;

    public HandlersRegistrator(GroupView groupView, GroupCreated groupCreated, QueueView queueView, BackGroupView backGroupView, StepOneView stepOneView, StepTwoView stepTwoView, CalendarBack calendarBack, CalendarForward calendarForward, SaveDate saveData, SaveTime saveTime, JoinQueueView joinQueueView, GroupDeleted groupDeleted, BackQueueName backQueueName, BackCalendar backCalendar) {
        this.groupView = groupView;
        this.stepTwoView = stepTwoView;
        this.groupCreated = groupCreated;
        this.queueView = queueView;
        this.backGroupView = backGroupView;
        this.stepOneView = stepOneView;
        this.calendarBack = calendarBack;
        this.calendarForward = calendarForward;
        this.saveData = saveData;
        this.saveTime = saveTime;
        this.joinQueueView = joinQueueView;
        this.groupDeleted = groupDeleted;
        this.backQueueName = backQueueName;
        this.backCalendar = backCalendar;
    }

    public void registerAllHandlers(TelegramController telegramController){
        telegramController.registerMessageHandler(groupDeleted);
        telegramController.registerMessageHandler(groupCreated);
        telegramController.registerMessageHandler(groupView);
        telegramController.registerMessageHandler(stepTwoView);
        telegramController.registerCallbackHandler(queueView);
        telegramController.registerCallbackHandler(backGroupView);
        telegramController.registerCallbackHandler(backQueueName);
        telegramController.registerCallbackHandler(backCalendar);
        telegramController.registerCallbackHandler(stepOneView);
        telegramController.registerCallbackHandler(calendarBack);
        telegramController.registerCallbackHandler(calendarForward);
        telegramController.registerCallbackHandler(saveData);
        telegramController.registerMessageHandler(saveTime);
        telegramController.registerCallbackHandler(joinQueueView);
    }
}
