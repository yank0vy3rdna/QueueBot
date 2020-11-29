package ru.bez_createha.queue_bot.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.bez_createha.queue_bot.model.Queue;
import ru.bez_createha.queue_bot.model.QueueUser;
import ru.bez_createha.queue_bot.model.User;

import java.util.List;

public interface QueueUserRepository extends JpaRepository<QueueUser, Long> {
    List<QueueUser> findAllByQueueOrderByDate(Queue queue);
    List<QueueUser> findAllByUser(User user);
}
