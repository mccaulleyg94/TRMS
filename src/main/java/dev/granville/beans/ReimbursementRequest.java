package dev.granville.beans;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="reimbursement_request")
@Getter
@Setter
public class ReimbursementRequest implements Comparable<ReimbursementRequest> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="reimbursement_Id")
	private Integer reimbursementId;

	@ManyToOne
	@JoinColumn(name="employee_id")
	private Employee employee;

	@ManyToOne
	@JoinColumn(name="reviewer_id")
	private Employee reviewer;

	@ManyToOne
	@JoinColumn(name="request_status")
	private RequestStatus reimbursementStatus;

	@ManyToOne
	@JoinColumn(name="request_eventid")
	private ReimbursementEventType reimbursementEventType;

	@Column(name="request_cost")
	private Double reimbursementCost;

	@Column(name="request_date")
	private LocalDate reimbursementDate;

	@Column(name="request_location")
	private String reimbursementLocation;

	@Column(name="request_timestamp")
	private Timestamp reimbursementTimeStamp;

	@ManyToOne
	@JoinColumn(name="department_id")
	private Department department;

	@Column(name="timeoff")
	private LocalDateTime timeoffStart;

	@Column(name="timeoff_ends")
	private LocalDateTime timeoffEnd;

	@ManyToOne
	@JoinColumn(name="urgencyId")
	private Urgency urgency;

	@Column(name="attachment")
	private File attachment;

	@Column(name="notes")
	private String notes;

	public double coveredCost() {
		double pending = 0.0;
		switch (reimbursementEventType.getEventId()) {
			case 1:
				pending = (reimbursementCost) * .8;
				break;
			case 2:
				pending = (reimbursementCost) * .6;
				break;
			case 3:
				pending = (reimbursementCost) * .75;
				break;
			case 4:
				pending = (reimbursementCost) * 1.0;
				break;
			case 5:
				pending = (reimbursementCost) * .9;
				break;
			default:
				pending = (reimbursementCost) * .3;
				break;
		}
		if (pending < 0.0) {
			pending = 0.0;
		} else if (pending > 1000.0) {
			pending = 1000.0;
		}
		return pending;
	}

	public boolean updateStatus(@NotNull Integer approval) {
		switch (approval) {
			case 0:
				if (reimbursementStatus.getStatusId() == 0) {
					return false;
				}
				reimbursementStatus.setStatusId(0);
				return true;
			case 1:
				if (reimbursementStatus.getStatusId() == 1) {
					return false;
				}
				reimbursementStatus.setStatusId(1);
				return true;
			case 2:
				if (reimbursementStatus.getStatusId() == 2) {
					return false;
				}
				reimbursementStatus.setStatusId(2);
				return true;
			case 3:
				if (reimbursementStatus.getStatusId() == 3) {
					return false;
				}
				reimbursementStatus.setStatusId(3);
				return true;
			case 4:
				if (reimbursementStatus.getStatusId() == 4) {
					return false;
				}
				reimbursementStatus.setStatusId(4);
				return true;
			default:
				reimbursementStatus.setStatusId(0);
				return false;
		}
	}

	@Override
	public int compareTo(@NotNull ReimbursementRequest o) {
		if (this.urgency.getUrgencyId() > o.urgency.getUrgencyId()) {
			return -1;
		} else {
			if (this.employee.getEmployeeId() < o.employee.getEmployeeId()) {
				return -1;
			}
			return 1;
		}
	}
}
