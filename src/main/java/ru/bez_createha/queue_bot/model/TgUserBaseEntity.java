package ru.bez_createha.queue_bot.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
public abstract class TgUserBaseEntity  {
    @Id
    @Column(name = "user_id")
    private Integer userId;
}
