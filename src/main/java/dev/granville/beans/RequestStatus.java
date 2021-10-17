package dev.granville.beans;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "reimbursement_status")@Getter
@Setter
public class RequestStatus {
    @Id
    @Column(name = "status_id")
    private int statusId;
    @Column(name = "status_name")
    private String statusName;
}
