package ru.bez_createha.queue_bot.view;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bez_createha.queue_bot.Bot;
import ru.bez_createha.queue_bot.dao.GroupRepository;
import ru.bez_createha.queue_bot.model.User;
import ru.bez_createha.queue_bot.services.GroupService;
import ru.bez_createha.queue_bot.services.QueueService;

import java.util.List;
import java.util.function.Predicate;

@Component
public class GroupDeleted implements MessageCommand{

    private final GroupService groupService;
    private final GroupRepository groupRepository;
    private final QueueService queueService;
    @Value("${bot.name}")
    private String botUsername;

    public GroupDeleted(GroupService groupService, GroupRepository groupRepository, QueueService queueService) {
        this.groupService = groupService;
        this.groupRepository = groupRepository;
        this.queueService = queueService;
    }

    @Override
    public Predicate<String> statePredicate() {
        return s -> true;
    }

    @Override
    public Predicate<Message> messagePredicate() {
        return message -> { if (message.getLeftChatMember() != null) {
            return message.getChat().getType().equals("group") && message.getLeftChatMember().getFirstName().equals(botUsername);
        } return false;};
    }

    @Override
    public void process(Message message, User user, Bot bot) throws TelegramApiException {
        groupRepository.delete(groupService.findByChatId(message.getChatId()));
    }
}
