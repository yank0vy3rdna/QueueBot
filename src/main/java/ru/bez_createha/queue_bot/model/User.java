package ru.bez_createha.queue_bot.model;

import lombok.*;


import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User extends TgUserBaseEntity{
    @Column(name = "name")
    private String name;
    @Column(name = "bot_state")
    private String botState;
    @Column(name = "message_id")
    private Integer messageId;

    @ManyToMany(mappedBy = "queue_users")
    List<Queue> queues;

}
