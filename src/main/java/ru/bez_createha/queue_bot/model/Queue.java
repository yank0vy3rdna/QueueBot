package ru.bez_createha.queue_bot.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "queues")
public class Queue extends IdBaseEntity {

    @Column(name = "tag")
    private String tag;
    @Column(name = "start_time")
    private Date startTime;
    @Column(name = "status")

    @Enumerated(EnumType.STRING)
    private QueueStatus status;


    @OneToMany(mappedBy = "queue",cascade = {CascadeType.ALL})
    private List<QueueUser> queue_users;

    @ManyToOne(cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group groupId;


}

