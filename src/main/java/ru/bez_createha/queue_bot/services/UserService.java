package ru.bez_createha.queue_bot.services;

import ru.bez_createha.queue_bot.model.User;

public interface UserService {
    User findByUserId(org.telegram.telegrambots.meta.api.objects.User userId);
    User createUser (Integer userId, String name);
    void saveUser (User user);
}
