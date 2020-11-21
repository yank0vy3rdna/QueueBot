package ru.bez_createha.queue_bot.context;

import lombok.Data;
import ru.bez_createha.queue_bot.model.Group;
import ru.bez_createha.queue_bot.view.createQueue.SimpleCalendar;


@Data
public class UserStaff {
    private final RawQueue rawQueue;
    private final SimpleCalendar simpleCalendar;
    private Group group;

    public UserStaff(RawQueue rawQueue, SimpleCalendar simpleCalendar) {
        this.rawQueue = rawQueue;
        this.simpleCalendar = simpleCalendar;
    }
}
