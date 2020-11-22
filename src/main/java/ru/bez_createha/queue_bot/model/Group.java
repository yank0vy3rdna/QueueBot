package ru.bez_createha.queue_bot.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "GROUPS")
public class Group extends TgBaseEntity {

    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    @OneToMany (mappedBy="groupId")
    private List<Queue> queue;

    @ManyToOne
    @JoinColumn(name = "admin_id", referencedColumnName = "user_id")
    private User admin;
    private String name;
}
