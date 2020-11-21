package ru.bez_createha.queue_bot.services;

import ru.bez_createha.queue_bot.model.Group;
import ru.bez_createha.queue_bot.model.Queue;
import ru.bez_createha.queue_bot.model.QueueStatus;

import java.util.List;

public interface QueueService {
    List<Queue> findByGroupId(Group groupId);
    List<Queue> findAllByStatus(QueueStatus status);
    void save(Queue queue);
}
