package ru.bez_createha.queue_bot.view;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bez_createha.queue_bot.Bot;
import ru.bez_createha.queue_bot.model.Group;
import ru.bez_createha.queue_bot.model.User;
import ru.bez_createha.queue_bot.services.GroupService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.groupadministration.LeaveChat;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Component
public class GroupCreated implements MessageCommand{
    private final GroupService groupService;

    @Value("${bot.name}")
    private String botUsername;

    public GroupCreated(GroupService groupService) {
        this.groupService = groupService;
    }

    @Override
    public Predicate<String> statePredicate() {
        return s -> true;
    }

    @Override
    public Predicate<Message> messagePredicate() {
        return message -> (message.getChat().getType().equals("group") || message.getChat().getType().equals("supergroup")) && message.getNewChatMembers().get(0).getFirstName().equals(botUsername);
    }

    public void process(Message message, User user, Bot bot) throws TelegramApiException {
        SendMessage response = new SendMessage();
        response.setChatId(String.valueOf(message.getChatId()));
        if (groupService.findAllByAdmin(user).size() < 4) {

            Group group = new Group();
            group.setAdmin(user);
            group.setName(message.getChat().getTitle());
            group.setChatId(message.getChatId());
            groupService.saveGroup(group);

            response.setText("Группа добавлена, Админ группы: " + user.getName() + "\n" + "Для связи перейдите @" + bot.getMe().getUserName());
            bot.execute(response);
        } else {
            response.setText(user.getName() + ", лимит групп для одного пользователя - 4. Вы можете удалить группы, созданные ранее");
            LeaveChat leaveChat = new LeaveChat();
            leaveChat.setChatId(String.valueOf(message.getChatId()));
            bot.execute(response);
            bot.execute(leaveChat);
        }
    }
}
