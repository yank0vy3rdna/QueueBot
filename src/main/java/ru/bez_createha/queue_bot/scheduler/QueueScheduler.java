package ru.bez_createha.queue_bot.scheduler;

import org.springframework.stereotype.Component;
import ru.bez_createha.queue_bot.Bot;
import ru.bez_createha.queue_bot.model.Queue;
import ru.bez_createha.queue_bot.model.QueueStatus;
import ru.bez_createha.queue_bot.services.QueueService;
import ru.bez_createha.queue_bot.utils.InlineButton;

import java.time.Clock;
import java.util.List;
import java.util.Timer;

@Component
public class QueueScheduler {

    private final QueueService queueService;
    private final InlineButton telegramUtil;


    public QueueScheduler(QueueService queueService, InlineButton telegramUtil) {
        this.queueService = queueService;
        this.telegramUtil = telegramUtil;
    }
    public void initFromDb(Bot bot){
        List<Queue> queues = queueService.findAllByStatus(QueueStatus.NOT_STARTED);
        for (Queue queue : queues){
            createJob(queue, bot);
        }
    }

    public void createJob(Queue queue, Bot bot) {
        ScheduledJob scheduledJob = new ScheduledJob(bot, telegramUtil, queueService);
        long initialDelay = queue.getStartTime().getTime() - Clock.systemDefaultZone().millis();
        if (initialDelay < 0) {
            return;
        }
        scheduledJob.setQueue(queue);

        Timer timer = new Timer();
        timer.schedule(scheduledJob, initialDelay);
    }
}
