package ru.bez_createha.queue_bot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;

@Entity
@Data
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

}
