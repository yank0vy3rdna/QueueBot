package ru.bez_createha.queue_bot.dao;

import ru.bez_createha.queue_bot.model.Group;
import ru.bez_createha.queue_bot.model.Queue;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.bez_createha.queue_bot.model.QueueStatus;

import java.util.List;


public interface QueueRepository extends JpaRepository<Queue,Long> {
    List<Queue> findAllByGroupId(Group groupId);
    List<Queue> findAllByStatus(QueueStatus status);
}
