package ru.bez_createha.queue_bot.scheduler;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Service;
import ru.bez_createha.queue_bot.Bot;
import ru.bez_createha.queue_bot.model.Queue;
import ru.bez_createha.queue_bot.model.QueueStatus;
import ru.bez_createha.queue_bot.services.QueueService;

import java.util.Date;
import java.util.List;

import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.TriggerKey.triggerKey;

@Service
public class SchedulerService {
    private final Scheduler scheduler;
    private final QueueService queueService;
    public SchedulerService(QueueService queueService) throws SchedulerException {
        this.queueService = queueService;

        SchedulerFactory sf = new StdSchedulerFactory();
        scheduler = sf.getScheduler();
        scheduler.start();
        initFromDb();
    }

    public void initFromDb() throws SchedulerException {
        List<Queue> queues = queueService.findAllByStatus(QueueStatus.NOT_STARTED);
        for (Queue queue : queues){
            registerJob(queue);
        }
    }

    public void registerJob(Queue queue) throws SchedulerException {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("queue", queue);
        jobDataMap.put("queue_service", queueService);
        JobDetail jobDetail = JobBuilder
                .newJob(StartQueueJob.class)
                .withIdentity(queue.getId().toString())
                .setJobData(jobDataMap)
                .build();
        Trigger trigger = newTrigger()
                .withIdentity(triggerKey(queue.getId().toString(), "queue_starts"))
                .startAt(queue.getStartTime())
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }
}
