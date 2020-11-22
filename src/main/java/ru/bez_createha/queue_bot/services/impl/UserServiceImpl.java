package ru.bez_createha.queue_bot.services.impl;

import ru.bez_createha.queue_bot.dao.UserRepository;
import ru.bez_createha.queue_bot.model.State;
import ru.bez_createha.queue_bot.model.User;
import ru.bez_createha.queue_bot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByUserId(org.telegram.telegrambots.meta.api.objects.User userTg) {
        User user = userRepository.findByUserId(userTg.getId());
        if (user == null) {
            user = createUser(userTg.getId(), userTg.getUserName());
        }
        return user;
    }

    @Override
    public User createUser(Integer userId, String name) {
        User user = new User();
        user.setUserId(userId);
        user.setName(name);
        user.setBotState(State.GROUP_MENU.toString());
        userRepository.save(user);
        return user;
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

}
