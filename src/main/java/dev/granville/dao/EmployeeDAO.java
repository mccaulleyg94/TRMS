package dev.granville.dao;

import dev.granville.beans.Employee;
import dev.granville.util.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Random;

public class EmployeeDAO implements EmployeeDAOInterface {
    private final HibernateUtil hu = HibernateUtil.getHibernateUtil();
    private final Logger log = Logger.getLogger(EmployeeDAO.class);

    @Override
    public Employee update(Employee e) {
        try (Session s = hu.getSession()) {
            Transaction tx = null;
            tx = s.beginTransaction();
            s.update(e);
            tx.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(ex.getMessage());
            return null;
        }
        return e;
    }

    @Override
    public Employee add(Employee e) {
        try (Session s = hu.getSession()) {
            Transaction tx = null;
            tx = s.beginTransaction();
            int id = (int) s.save(e);
            e.setEmployeeId(id);
            tx.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(ex.getMessage());
            return null;
        }
        return e;
    }


    @Override
    public Employee getById(Integer id) {
        try {
            Session s = hu.getSession();
            Employee e = s.get(Employee.class, id);
            s.close();
            return e;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(ex.getMessage());
            return null;
        }
    }

    @Override
    public Employee delete(Employee e) {
        try (Session s = hu.getSession()) {
            Transaction tx = null;
            tx = s.beginTransaction();
            s.delete(e);
            tx.commit();
            return e;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(ex.getMessage());
            return null;
        }
    }

    @Override
    public Employee getByUsername(String username) {
        try {
            Session s = hu.getSession();
            String query = "FROM Employee where username = :username";
            Query<Employee> q = s.createQuery(query, Employee.class);
            q.setParameter("username", username);
            List<Employee> e = q.getResultList();
            s.close();
            return (e.isEmpty() ? null : e.get(0));
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(ex.getMessage());
            return null;
        }
    }

    @Override
    public List<Employee> getByDepartmentName(String departmentName) {
        try {
            Session s = hu.getSession();
            String query = "FROM Employee where department.departmentName = :departmentName";
            Query q = s.createQuery(query);
            q.setParameter("departmentName", departmentName);
            List<Employee> e = q.list();
            s.close();
            return e;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(ex.getMessage());
            return null;
        }
    }


    @Override
    public List<Employee> getAll() {
        try (Session s = hu.getSession()) {
            String query = "FROM Employee";
            Query<Employee> q = s.createQuery(query, Employee.class);
            return q.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(ex.getMessage());
            return null;
        }
    }

    @Override
    public Employee getRandom() {
        try (Session s = hu.getSession()) {
            Random rand = new Random();
            String query = "FROM Employee";
            Query<Employee> q = s.createQuery(query, Employee.class);
            List<Employee> employeeList = q.getResultList();
            return employeeList.get(rand.nextInt(employeeList.size()));
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
            String query = "FROM Employee";
            Query<Employee> q = s.createQuery(query, Employee.class);
            List<Employee> e = q.getResultList();
            tx = s.beginTransaction();
            for (Employee employee: e) {
                s.delete(employee);
            }
            tx.commit();
            s.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(ex.getMessage());
        }
    }


}
