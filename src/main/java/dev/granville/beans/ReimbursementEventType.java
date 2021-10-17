package dev.granville.beans;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "reimbursement_eventtype")
@Getter
@Setter
public class ReimbursementEventType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eventid")
    private Integer eventId;
    @Column(name = "eventname")
    private String eventName;
}
