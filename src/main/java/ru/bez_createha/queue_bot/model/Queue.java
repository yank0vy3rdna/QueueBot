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


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "queue_users",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> queue_users;

    @PreRemove
    public void removePositions() {
        queue_users.forEach(user -> user.getQueues().remove(this));
    }

    @ManyToOne(cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group groupId;


}

