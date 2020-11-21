package ru.bez_createha.queue_bot.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
public abstract class TgBaseEntity extends IdBaseEntity{
    @Column(name = "chat_id")
    private Long chatId;

}
