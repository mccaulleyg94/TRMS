package dev.granville.beans;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Table
@Entity(name="department")
@Getter
@Setter
public class Department {

    @Id
    @GeneratedValue()
    @Column(name="department_id")
    private Integer departmentId;
    @Column(name="department_name")
    private String departmentName;
}
