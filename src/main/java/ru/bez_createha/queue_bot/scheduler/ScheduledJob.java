package ru.bez_createha.queue_bot.scheduler;

import lombok.Data;
import ru.bez_createha.queue_bot.Bot;
import ru.bez_createha.queue_bot.model.Queue;

import java.util.TimerTask;

@Data
public class ScheduledJob extends TimerTask {

    private final Bot bot;
    private Queue queue;

    public ScheduledJob(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void run() {
        System.out.println("suck");
    }

}
