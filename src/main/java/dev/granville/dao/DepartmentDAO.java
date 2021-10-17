package dev.granville.dao;

import dev.granville.beans.Department;
import dev.granville.beans.Employee;
import dev.granville.beans.ReimbursementRequest;
import dev.granville.util.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Random;

public class DepartmentDAO implements DepartmentDAOInterface {
    private final HibernateUtil hu = HibernateUtil.getHibernateUtil();
    private final Logger log = Logger.getLogger(DepartmentDAO.class);

    @Override
    public Department getByName(String departmentName) {
        try {
            Session s = hu.getSession();
            String query = "FROM department where departmentName = :departmentName";
            Query<Department> q = s.createQuery(query, Department.class);
            q.setParameter("departmentName", departmentName);
            List<Department> d = q.getResultList();
            s.close();
            return d.get(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(ex.getMessage());
            return null;
        }
    }

    @Override
    public Department getById(Integer id) {
        try {
            Session s = hu.getSession();
            Department d = s.get(Department.class, id);
            s.close();
            return d;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(ex.getMessage());
            return null;
        }
    }

    @Override
    public Department getRandom() {
        try (Session s = hu.getSession()) {
            Random rand = new Random();
            String query = "FROM department";
            Query<Department> q = s.createQuery(query, Department.class);
            List<Department> departmentList = q.getResultList();
            return departmentList.get(rand.nextInt(departmentList.size() + 1));
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(ex.getMessage());
            return null;
        }
    }

    @Override
    public List<Department> getAll() {
        try {
            Session s = hu.getSession();
            String query = "FROM department";
            Query<Department> q = s.createQuery(query, Department.class);
            List<Department> d = q.getResultList();
            s.close();
            return d;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(ex.getMessage());
            return null;
        }
    }

    @Override
    public void deleteAll() {
    }

    @Override
    public Department add(Department department) {
        return null;
    }

    @Override
    public Department delete(Department department) {
        return null;
    }

    @Override
    public Department update(Department department) {
        return null;
    }
}
