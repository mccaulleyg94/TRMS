package dev.granville.service;

import dev.granville.beans.Employee;
import dev.granville.beans.ReimbursementRequest;
import dev.granville.dao.ReimbursementDAO;

import java.util.List;


public class ReimbursementService {
	private final ReimbursementDAO reimbursementDAO = new ReimbursementDAO();
	public ReimbursementRequest add(ReimbursementRequest rr)  {
		return reimbursementDAO.add(rr);
	}
	public ReimbursementRequest delete(ReimbursementRequest rr)  {
		return reimbursementDAO.delete(rr);
	}
	public ReimbursementRequest update(ReimbursementRequest rr)  {
		return reimbursementDAO.update(rr);
	}
	public ReimbursementRequest getById(Integer id) {
		return reimbursementDAO.getById(id);
	}
	public ReimbursementRequest getRandom() {
		return reimbursementDAO.getRandom();
	}
	public List<ReimbursementRequest> getAvailableForReviewWithRank(Employee e) {
		return reimbursementDAO.getAvailableForReviewWithRank(e);
	}
	public List<ReimbursementRequest> getAvailableForBenCo(Employee e) {
		return reimbursementDAO.getAvailableForReviewBenCo(e);
	}
	public List<ReimbursementRequest> getByEmployeeId(Integer id) {
		return reimbursementDAO.getByEmployeeId(id);
	}
	public List<ReimbursementRequest> getAll() {
		return reimbursementDAO.getAll();
	}
	public Boolean deleteAll() {
		try {
			reimbursementDAO.deleteAll();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
