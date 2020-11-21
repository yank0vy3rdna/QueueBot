package ru.bez_createha.queue_bot.context;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.bez_createha.queue_bot.utils.InlineButton;
import ru.bez_createha.queue_bot.view.createQueue.SimpleCalendar;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserContext {

    private final InlineButton inlineButton;
    private final Map<Integer, UserStaff> userMap = new HashMap<>();


    public UserContext(InlineButton inlineButton) {
        this.inlineButton = inlineButton;
    }

    public Map<Integer, UserStaff> initUser(Integer userId){
        if (userMap.get(userId) == null){
            userMap.put(userId, new UserStaff(new RawQueue(), new SimpleCalendar(inlineButton)));
        }
        return userMap;
    }

    public void clearContext(Integer userId){
        userMap.remove(userId);
    }

    public UserStaff getUserStaff(Integer userId){
        return userMap.get(userId);
    }

}
