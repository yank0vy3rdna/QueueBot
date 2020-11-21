package ru.bez_createha.queue_bot.view;


import ru.bez_createha.queue_bot.controller.TelegramController;
import ru.bez_createha.queue_bot.view.backs.BackGroupView;
import ru.bez_createha.queue_bot.view.createQueue.StepOneView;
import org.springframework.stereotype.Service;

@Service
public class HandlersRegistrator {
    private final GroupView groupView;
    private final GroupCreated groupCreated;
    private final QueueView queueView;
    private final BackGroupView backGroupView;
    private final StepOneView stepOneView;

    public HandlersRegistrator(GroupView groupView, GroupCreated groupCreated, QueueView queueView, BackGroupView backGroupView, StepOneView stepOneView) {
        this.groupView = groupView;
        this.groupCreated = groupCreated;
        this.queueView = queueView;
        this.backGroupView = backGroupView;
        this.stepOneView = stepOneView;
    }

    public void registerAllHandlers(TelegramController telegramController){
        telegramController.registerMessageHandler(groupView);
        telegramController.registerMessageHandler(groupCreated);
        telegramController.registerCallbackHandler(queueView);
        telegramController.registerCallbackHandler(backGroupView);
        telegramController.registerCallbackHandler(stepOneView);
    }
}
