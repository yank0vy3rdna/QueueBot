package ru.bez_createha.queue_bot.view;

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

    public GroupCreated(GroupService groupService) {
        this.groupService = groupService;
    }

    @Override
    public Predicate<String> statePredicate() {
        return s -> true;
    }

    @Override
    public Predicate<Message> messagePredicate() {
        return message -> message.getChat().getType().equals("group") && message.getNewChatMembers().get(0).getFirstName().equals("SuckTestSuckBot");
    }

    public List<BotApiMethod<? extends Serializable>> process(Message message, User user) {
        List<BotApiMethod<? extends Serializable>> methods = new ArrayList<>();
        SendMessage response = new SendMessage();
        response.setChatId(String.valueOf(message.getChatId()));
        if (groupService.findAllByAdmin(user).size() < 4) {

            Group group = new Group();
            group.setAdmin(user);
            group.setName(message.getChat().getTitle());
            group.setChatId(message.getChatId());
            groupService.saveGroup(group);

            response.setText("Группа добавлена, Админ группы: " + user.getName() + "\n" + "Для связи перейдите @SuckTestSuckBot");
        } else {
            response.setText(user.getName() + ", лимит групп для одного пользователя - 4. Вы можете удалить группы, созданные ранее");
            LeaveChat leaveChat = new LeaveChat();
            leaveChat.setChatId(String.valueOf(message.getChatId()));
            methods.add(leaveChat);
        }
        methods.add(0, response);
        return methods;
    }
}
