package ru.bez_createha.queue_bot.view;


import ru.bez_createha.queue_bot.controller.TelegramController;
import ru.bez_createha.queue_bot.view.backs.BackGroupView;
import ru.bez_createha.queue_bot.view.createQueue.CalendarBack;
import ru.bez_createha.queue_bot.view.createQueue.CalendarForward;
import ru.bez_createha.queue_bot.view.createQueue.StepOneView;
import org.springframework.stereotype.Service;
import ru.bez_createha.queue_bot.view.createQueue.StepTwoView;

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

    public HandlersRegistrator(GroupView groupView, GroupCreated groupCreated, QueueView queueView, BackGroupView backGroupView, StepOneView stepOneView, StepTwoView stepTwoView, CalendarBack calendarBack, CalendarForward calendarForward) {
        this.groupView = groupView;
        this.stepTwoView = stepTwoView;
        this.groupCreated = groupCreated;
        this.queueView = queueView;
        this.backGroupView = backGroupView;
        this.stepOneView = stepOneView;
        this.calendarBack = calendarBack;
        this.calendarForward = calendarForward;
    }

    public void registerAllHandlers(TelegramController telegramController){
        telegramController.registerMessageHandler(groupView);
        telegramController.registerMessageHandler(groupCreated);
        telegramController.registerMessageHandler(stepTwoView);
        telegramController.registerCallbackHandler(queueView);
        telegramController.registerCallbackHandler(backGroupView);
        telegramController.registerCallbackHandler(stepOneView);
        telegramController.registerCallbackHandler(calendarBack);
        telegramController.registerCallbackHandler(calendarForward);
    }
}
