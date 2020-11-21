package ru.bez_createha.queue_bot.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "queues")
public class Queue extends IdBaseEntity {

    @Column(name = "tag")
    private String tag;
    @Column(name = "start_time")
    private Date startTime;
    @Column(name = "end_time")
    private Date endTime;
    @Column(name = "status")

    @Enumerated(EnumType.STRING)
    private QueueStatus status;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group groupId;


}

