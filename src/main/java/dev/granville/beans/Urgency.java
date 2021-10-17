package dev.granville.beans;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "reimbursement_urgency")
@Getter
@Setter
public class Urgency {

    @Id
    @Column(name="urgency_id")
    private Integer urgencyId;

    @Column(name = "urgency")
    private String urgencyName;

}
