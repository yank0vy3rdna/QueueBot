package ru.bez_createha.queue_bot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "USERS_QUEUES")
@Getter
@Setter
public class QueueUser {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "queue_id")
    private Queue queue;

    @Column(name = "date")
    private Date date = new Date();

}
