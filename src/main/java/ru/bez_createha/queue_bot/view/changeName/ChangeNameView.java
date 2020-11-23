package ru.bez_createha.queue_bot.view.changeName;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bez_createha.queue_bot.Bot;
import ru.bez_createha.queue_bot.context.RawQueue;
import ru.bez_createha.queue_bot.model.State;
import ru.bez_createha.queue_bot.model.User;
import ru.bez_createha.queue_bot.services.UserService;
import ru.bez_createha.queue_bot.utils.InlineButton;
import ru.bez_createha.queue_bot.view.MessageCommand;
import ru.bez_createha.queue_bot.view.createQueue.SimpleCalendar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Component
public class ChangeNameView implements MessageCommand {
    private final InlineButton telegramUtil;
    private final UserService userService;

    public ChangeNameView(InlineButton telegramUtil, UserService userService) {
        this.telegramUtil = telegramUtil;
        this.userService = userService;
    }

    public Predicate<String> statePredicate() {
        return s -> s.equals(State.CHANGE_NAME.toString());
    }

    @Override
    public Predicate<Message> messagePredicate() {
        return message -> true;
    }


    public void process(Message message, User user, Bot bot) throws TelegramApiException {
        String username = message.getText();


        user.setName(username);
        userService.saveUser(user);


        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        user.setBotState(State.CHANGE_NAME.toString());

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();


        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setMessageId(user.getMessageId());
        editMessageText.setChatId(message.getChatId().toString());

        editMessageText.setReplyMarkup(inlineKeyboardMarkup);


        keyboard.add(Collections.singletonList(telegramUtil.createInlineKeyboardButton(
                "Назад",
                "back"
        )));
        editMessageText.setText("Ваше имя изменено на: "+user.getName());
        inlineKeyboardMarkup.setKeyboard(keyboard);

        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(message.getChatId().toString());
        deleteMessage.setMessageId(message.getMessageId());
        bot.execute(deleteMessage);
        bot.execute(editMessageText);
    }
}
