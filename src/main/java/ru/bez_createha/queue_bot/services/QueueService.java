package ru.bez_createha.queue_bot.services;

import ru.bez_createha.queue_bot.model.Group;
import ru.bez_createha.queue_bot.model.Queue;
import ru.bez_createha.queue_bot.model.QueueStatus;
import ru.bez_createha.queue_bot.model.User;

import java.util.List;

public interface QueueService {
    List<Queue> findAllByGroupId(Group groupId);
    List<Queue> findAllByStatus(QueueStatus status);
    Queue getById(Long queue_id);
    void putUser(Queue queue, User user);
    void removeUser(Queue queue, User user);
    void save(Queue queue);
}
