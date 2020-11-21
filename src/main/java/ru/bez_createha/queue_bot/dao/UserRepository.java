package ru.bez_createha.queue_bot.dao;

import ru.bez_createha.queue_bot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, String> {
    User findByUserId(Integer userId);
}
