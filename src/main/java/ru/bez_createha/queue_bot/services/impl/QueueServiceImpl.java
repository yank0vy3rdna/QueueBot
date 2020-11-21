package ru.bez_createha.queue_bot.services.impl;

import ru.bez_createha.queue_bot.dao.QueueRepository;
import ru.bez_createha.queue_bot.model.Group;
import ru.bez_createha.queue_bot.model.Queue;
import ru.bez_createha.queue_bot.model.QueueStatus;
import ru.bez_createha.queue_bot.services.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueueServiceImpl implements QueueService {
    private final QueueRepository queueRepository;

    @Autowired
    public QueueServiceImpl(QueueRepository queueRepository) {
        this.queueRepository = queueRepository;
    }

    @Override
    public List<Queue> findByGroupId(Group groupId) {
        return queueRepository.findAllByGroupId(groupId);
    }

    @Override
    public List<Queue> findAllByStatus(QueueStatus status) {
        return queueRepository.findAllByStatus(status);
    }
}
