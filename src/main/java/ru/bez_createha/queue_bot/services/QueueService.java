package ru.bez_createha.queue_bot.services;

import ru.bez_createha.queue_bot.model.Group;
import ru.bez_createha.queue_bot.model.Queue;

import java.util.List;

public interface QueueService {
    List<Queue> findByGroupId(Group groupId);
}
