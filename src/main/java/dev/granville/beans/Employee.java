package dev.granville.beans;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="employee")
@Getter
@Setter
public class Employee implements Comparable<Employee> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="employee_id")
	private Integer employeeId;

	@ManyToOne
	@JoinColumn(name="employee_rank")
	private EmployeeRank rank;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="employee_reportsto")
	private Employee reportsTo;

	@Column(name="firstname")
	private String firstName;

	@Column(name="lastname")
	private String lastName;

	@Column(name="address")
	private String address;

	@Column(name="username")
	private String username;

	@Column(name="userpassword")
	private String password;

	@ManyToOne
	@JoinColumn(name="employee_department_id")
	private Department department;

	@Column(name="employee_funds")
	private Double aid = 0.0;

	public void addAid(Double aid) {
		double newAid = this.aid + aid;
		if (newAid > 1000.0) {
			newAid = 1000.0;
		} else if (newAid < this.aid) {
			newAid = this.aid;
		} else if (newAid < 0.0) {
			newAid = 0.0;
		}
		this.aid = newAid;
	}


	@Override
	public int compareTo(@org.jetbrains.annotations.NotNull Employee e) {
		if (this.getEmployeeId() < e.getEmployeeId()) {
			return -1;
		} else {
			return 1;
		}
	}
}
