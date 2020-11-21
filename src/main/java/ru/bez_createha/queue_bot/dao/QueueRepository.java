package ru.bez_createha.queue_bot.dao;

import ru.bez_createha.queue_bot.model.Group;
import ru.bez_createha.queue_bot.model.Queue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface QueueRepository extends JpaRepository<Queue,Long> {
    List<Queue> findByGroupId(Group groupId);
}
