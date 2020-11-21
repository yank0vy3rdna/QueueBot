package ru.bez_createha.queue_bot.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "queues")
public class Queue extends IdBaseEntity {

    @Column(name = "tag")
    private String tag;
    @Column(name = "start_time")
    private Date startTime;
    @Column(name = "status")

    @Enumerated(EnumType.STRING)
    private QueueStatus status;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "queue_users",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> queue_users;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group groupId;


}

