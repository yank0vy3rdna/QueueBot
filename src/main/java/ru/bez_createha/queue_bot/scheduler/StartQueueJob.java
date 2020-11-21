package ru.bez_createha.queue_bot.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bez_createha.queue_bot.Bot;
import ru.bez_createha.queue_bot.model.Queue;

public class StartQueueJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        Bot bot = (Bot) jobExecutionContext.getJobDetail().getJobDataMap().get("bot");
        Queue queue = (Queue) jobExecutionContext.getJobDetail().getJobDataMap().get("queue");
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(queue.getGroupId().getChatId().toString());
        sendMessage.setText("Соси хуй уебан блядь");
        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
