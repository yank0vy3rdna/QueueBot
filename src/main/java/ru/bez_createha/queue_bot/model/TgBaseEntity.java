package ru.bez_createha.queue_bot.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
public abstract class TgBaseEntity extends IdBaseEntity{
    @Column(name = "chat_id")
    private Long chatId;

}
