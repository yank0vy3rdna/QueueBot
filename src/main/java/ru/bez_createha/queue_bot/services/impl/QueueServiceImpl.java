package ru.bez_createha.queue_bot.services.impl;

import ru.bez_createha.queue_bot.dao.QueueRepository;
import ru.bez_createha.queue_bot.model.Group;
import ru.bez_createha.queue_bot.model.Queue;
import ru.bez_createha.queue_bot.model.QueueStatus;
import ru.bez_createha.queue_bot.model.User;
import ru.bez_createha.queue_bot.services.GroupService;
import ru.bez_createha.queue_bot.services.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QueueServiceImpl implements QueueService {
    private final QueueRepository queueRepository;
    private final GroupService groupService;

    @Autowired
    public QueueServiceImpl(QueueRepository queueRepository, GroupService groupService) {
        this.queueRepository = queueRepository;
        this.groupService = groupService;
    }

    @Override
    public List<Queue> findByGroupId(Group groupId) {
        return queueRepository.findAllByGroupId(groupId);
    }

    @Override
    public List<Queue> findAllByStatus(QueueStatus status) {
        return queueRepository.findAllByStatus(status);
    }

    @Override
    public Queue getById(Long queue_id) {
        return queueRepository.getOne(queue_id);
    }

    @Override
    public void putUser(Queue queue, User user) {
        if (queue.getQueue_users().stream().noneMatch(
                user1 -> user1.getUserId().equals(user.getUserId())
        )){
            queue.getQueue_users().add(user);
        }
        save(queue);
    }

    @Override
    public void removeUser(Queue queue, User user) {
        queue.setQueue_users(queue.getQueue_users().stream().filter(
                user1 -> !user1.getUserId().equals(user.getUserId())
        ).collect(Collectors.toList()));
        save(queue);
    }

    @Override
    public void save(Queue queue) {
        Group group = groupService.findByChatId(queue.getGroupId().getChatId());
        if (group == null) {
            groupService.saveGroup(queue.getGroupId());
        }
        queueRepository.save(queue);
    }
}
