package ru.bez_createha.queue_bot.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
public abstract class TgUserBaseEntity  {
    @Id
    @Column(name = "user_id")
    private Integer userId;
}
