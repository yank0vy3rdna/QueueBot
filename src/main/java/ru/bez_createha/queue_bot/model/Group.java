package ru.bez_createha.queue_bot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "GROUPS")
public class Group extends TgBaseEntity {

    @OneToMany (mappedBy="groupId")
    private List<Queue> queue;

    @ManyToOne
    @JoinColumn(name = "admin_id", referencedColumnName = "user_id")
    private User admin;
    private String name;
}
