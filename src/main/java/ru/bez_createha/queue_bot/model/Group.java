package ru.bez_createha.queue_bot.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "GROUPS")
public class Group extends TgBaseEntity {

    @OneToMany (mappedBy="groupId", fetch = FetchType.EAGER)
    private List<Queue> queue;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "admin_id", referencedColumnName = "user_id")
    private User admin;
    private String name;
}
