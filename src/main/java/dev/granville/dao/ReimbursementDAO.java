package dev.granville.dao;

import dev.granville.beans.Email;
import dev.granville.beans.Employee;
import dev.granville.beans.ReimbursementRequest;
import dev.granville.util.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Random;

public class ReimbursementDAO implements ReimbursementDAOInterface {
    private final HibernateUtil hu = HibernateUtil.getHibernateUtil();
    private final Logger log = Logger.getLogger(EmployeeDAO.class);

    @Override
    public ReimbursementRequest delete(ReimbursementRequest rr) {
        try (Session s = hu.getSession()) {
            Transaction tx = null;
            tx = s.beginTransaction();
            s.delete(rr);
            tx.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(ex.getMessage());
            return null;
        }
        return rr;
    }

    @Override
    public List<ReimbursementRequest> getByEmployeeId(Integer id) {
        try {
            Session s = hu.getSession();
            String query = "FROM ReimbursementRequest where employee.employeeId = :id";
            Query<ReimbursementRequest> q = s.createQuery(query, ReimbursementRequest.class);
            q.setParameter("id", id);
            List<ReimbursementRequest> reimbursementRequests = q.getResultList();
            s.close();
            return reimbursementRequests;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(ex.getMessage());
            return null;
        }
    }

    @Override
    public ReimbursementRequest getById(Integer id) {
        try {
            Session s = hu.getSession();
            ReimbursementRequest rr = s.get(ReimbursementRequest.class, id);
            s.close();
            return rr;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(ex.getMessage());
            return null;
        }
    }

    @Override
    public ReimbursementRequest getRandom() {
        try (Session s = hu.getSession()) {
            Random rand = new Random();
            String query = "FROM ReimbursementRequest";
            Query<ReimbursementRequest> q = s.createQuery(query, ReimbursementRequest.class);
            List<ReimbursementRequest> reimbursementRequestList = q.getResultList();
            return reimbursementRequestList.get(rand.nextInt(reimbursementRequestList.size() + 1));
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(ex.getMessage());
            return null;
        }
    }

    @Override
    public ReimbursementRequest update(ReimbursementRequest rr) {
        try (Session s = hu.getSession()) {
            Transaction tx = null;
            tx = s.beginTransaction();
            s.update(rr);
            tx.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(ex.getMessage());
            return null;
        }
        return rr;
    }

    @Override
    public ReimbursementRequest add(ReimbursementRequest rr) {
        try (Session s = hu.getSession()) {
            Transaction tx = null;
            tx = s.beginTransaction();
            int id = (int) s.save(rr);
            rr.setReimbursementId(id);
            tx.commit();
            return rr;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(ex.getMessage());
            return null;
        }
    }

    @Override
    public List<ReimbursementRequest> getAvailableForReview(Employee e) {
        try {
            Session s = hu.getSession();
            String query = "FROM ReimbursementRequest where department.departmentId = :departmentId" ;
            Query<ReimbursementRequest> q = s.createQuery(query, ReimbursementRequest.class);
            q.setParameter("departmentId", e.getDepartment().getDepartmentId());
            List<ReimbursementRequest> eligibleReimbursements = q.getResultList();
            s.close();
            return eligibleReimbursements;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(ex.getMessage());
            return null;
        }
    }

    @Override
    public List<ReimbursementRequest> getAvailableForReviewWithRank(Employee e) {
        try {
            Session s = hu.getSession();
            String query = "FROM ReimbursementRequest where department.departmentId = :departmentId " +
                    " and reimbursementStatus.statusId = :employeeRank" +
                    " and reviewer.employeeId = :reviewerId";
            Query<ReimbursementRequest> q = s.createQuery(query, ReimbursementRequest.class);
            q.setParameter("departmentId", e.getDepartment().getDepartmentId());
            q.setParameter("employeeRank", e.getRank().getRankId());
            q.setParameter("reviewerId", e.getEmployeeId());
            List<ReimbursementRequest> eligibleReimbursements = q.getResultList();
            s.close();
            return eligibleReimbursements;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(ex.getMessage());
            return null;
        }
    }

    @Override
    public List<ReimbursementRequest> getAvailableForReviewBenCo(Employee e) {
        try {
            Session s = hu.getSession();
            String query = "FROM ReimbursementRequest where reimbursementStatus.statusId = :employeeRank";
            Query<ReimbursementRequest> q = s.createQuery(query, ReimbursementRequest.class);
            q.setParameter("employeeRank", e.getRank().getRankId());
            List<ReimbursementRequest> eligibleReimbursements = q.getResultList();
            s.close();
            return eligibleReimbursements;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(ex.getMessage());
            return null;
        }
    }

    @Override
    public List<ReimbursementRequest> getAll() {
        try {
            Session s = hu.getSession();
            List<ReimbursementRequest> reimbursementRequests = null;
            String query = "FROM ReimbursementRequest";
            Query<ReimbursementRequest> q = s.createQuery(query, ReimbursementRequest.class);
            reimbursementRequests = q.getResultList();
            s.close();
            return reimbursementRequests;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(ex.getMessage());
            return null;
        }
    }

    @Override
    public void deleteAll() {
        try (Session s = hu.getSession()) {
            Transaction tx = null;
            String query = "FROM ReimbursementRequest";
            Query<ReimbursementRequest> q = s.createQuery(query, ReimbursementRequest.class);
            List<ReimbursementRequest> reimbursementRequests = q.getResultList();
            tx = s.beginTransaction();
            for (ReimbursementRequest rr: reimbursementRequests) {
                s.delete(rr);
            }
            tx.commit();
            s.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(ex.getMessage());
        }
    }
}
