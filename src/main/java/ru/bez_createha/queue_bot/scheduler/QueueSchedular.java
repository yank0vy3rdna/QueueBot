package ru.bez_createha.queue_bot.scheduler;

import org.springframework.stereotype.Component;
import ru.bez_createha.queue_bot.Bot;
import ru.bez_createha.queue_bot.model.Queue;
import ru.bez_createha.queue_bot.services.QueueService;

import java.time.Clock;
import java.util.Timer;

@Component
public class QueueSchedular {

    private final Bot bot;
    private final QueueService queueService;

    public QueueSchedular(Bot bot, QueueService queueService) {
        this.bot = bot;
        this.queueService = queueService;
    }

    public void createJob(Queue queue) {
        ScheduledJob scheduledJob = new ScheduledJob(bot);
        long initialDelay = queue.getStartTime().getTime() - Clock.systemDefaultZone().millis();

        scheduledJob.setQueue(queue);


        Timer timer = new Timer();
        timer.schedule(scheduledJob, initialDelay);
    }
}
