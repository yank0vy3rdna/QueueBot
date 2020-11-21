package ru.bez_createha.queue_bot.services;

import ru.bez_createha.queue_bot.model.Group;
import ru.bez_createha.queue_bot.model.User;

import java.util.List;

public interface GroupService {
    List<Group> findAllByAdmin(User admin);
    void saveGroup(Group group);
    Group findByChatId(Long chatId);
}
