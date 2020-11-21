package ru.bez_createha.queue_bot.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "GROUPS")
public class Group extends TgBaseEntity {

    @OneToMany (mappedBy="groupId", cascade = {CascadeType.ALL})
    private List<Queue> queue;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "admin_id", referencedColumnName = "user_id")
    private User admin;
    private String name;
}
