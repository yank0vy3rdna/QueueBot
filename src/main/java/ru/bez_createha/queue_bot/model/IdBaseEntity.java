package ru.bez_createha.queue_bot.model;

import lombok.Data;

import javax.persistence.*;

@MappedSuperclass
@Data
public abstract class IdBaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}
