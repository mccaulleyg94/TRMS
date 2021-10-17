package dev.granville.dao;

import dev.granville.beans.Employee;
import dev.granville.beans.ReimbursementRequest;

import java.util.List;

public  interface ReimbursementDAOInterface extends GenericDao<ReimbursementRequest> {
    List<ReimbursementRequest> getByEmployeeId(Integer id);
    List<ReimbursementRequest> getAvailableForReview(Employee e);
    List<ReimbursementRequest> getAvailableForReviewWithRank(Employee e);
    List<ReimbursementRequest> getAvailableForReviewBenCo(Employee e);
}
