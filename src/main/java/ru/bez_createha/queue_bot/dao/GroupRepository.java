package ru.bez_createha.queue_bot.dao;

import ru.bez_createha.queue_bot.model.Group;
import ru.bez_createha.queue_bot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group,Long> {
    List<Group> findAllByAdmin(User admin);
    Group findByChatId(Long chatId);
}
