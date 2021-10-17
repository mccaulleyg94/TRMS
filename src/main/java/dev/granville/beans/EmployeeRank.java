package dev.granville.beans;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="employee_rank")
@Getter
@Setter
public class EmployeeRank {
    @Id
    @Column(name="rank_id")
    private int rankId;

    @Column(name="rank_name")
    private String rankName;
}
