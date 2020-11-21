package ru.bez_createha.queue_bot.view;

import ru.bez_createha.queue_bot.context.UserContext;
import ru.bez_createha.queue_bot.model.Group;
import ru.bez_createha.queue_bot.model.State;
import ru.bez_createha.queue_bot.model.User;
import ru.bez_createha.queue_bot.services.GroupService;
import ru.bez_createha.queue_bot.utils.InlineButton;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


@Component
public class GroupView implements MessageCommand {

    private final InlineButton telegramUtil;
    private final GroupService groupService;
    private final UserContext userContext;

    public GroupView(InlineButton telegramUtil, GroupService groupService, UserContext userContext) {
        this.telegramUtil = telegramUtil;
        this.groupService = groupService;
        this.userContext = userContext;
    }

    @Override
    public Predicate<String> statePredicate() {
        return s -> s.equals(State.GROUP_MENU.toString());
    }

    @Override
    public Predicate<Message> messagePredicate() {
        return message -> true;
    }

    public List<BotApiMethod<? extends Serializable>> process(Message message, User user) {


        userContext.clearContext(user.getUserId());
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<Group> groups = groupService.findAllByAdmin(user);
        List<InlineKeyboardButton> inlineKeyboardButtonsRow = new ArrayList<>();
        int i;
        for (i = 0; i < groups.size(); i++) {
            InlineKeyboardButton button =
                    telegramUtil.createInlineKeyboardButton(
                            groups.get(i).getName(),
                            "group::" + groups.get(i).getChatId()
                    );
            inlineKeyboardButtonsRow.add(button);
            if (i % 2 == 1) {
                keyboard.add(inlineKeyboardButtonsRow);
                inlineKeyboardButtonsRow = new ArrayList<>();
            }

        }
        if (i % 2 == 1){
            keyboard.add(inlineKeyboardButtonsRow);
        }

        inlineKeyboardMarkup.setKeyboard(keyboard);

        user.setBotState(State.GROUP_MENU.toString());
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        if (groups.isEmpty()) {
            sendMessage.setText("Привет! Я бот для очередей. Добавь меня в чат твоей группы в телеграме, чтобы получить возможность запустить очередь\n\nby @yank0vy3rdna, @qaralle, @user38h23");
        } else {
            sendMessage.setText("Привет! Я бот для очередей. Ниже - список твоих групп. Нажми на них, чтобы открыть меню.\n\nby @yank0vy3rdna, @qaralle, @user38h23");
        }
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        List<BotApiMethod<? extends Serializable>> methods = new ArrayList<>();
        methods.add(sendMessage);
        return methods;
    }
}
