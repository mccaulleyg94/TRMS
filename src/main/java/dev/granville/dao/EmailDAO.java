package dev.granville.dao;

import dev.granville.beans.Email;
import dev.granville.beans.Employee;
import dev.granville.util.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class EmailDAO implements EmailDAOInterface {

    private final HibernateUtil hu = HibernateUtil.getHibernateUtil();
    private final Logger log = Logger.getLogger(EmployeeDAO.class);

    @Override
    public List<Email> getUserEmails(Employee e) {
        try (Session s = hu.getSession()) {
            String query = "FROM Email where Recipient.employeeId = :id";
            Query<Email> q = s.createQuery(query, Email.class);
            q.setParameter("id", e.getEmployeeId());
            List<Email> emails = q.getResultList();
            Collections.sort(emails);
            return emails;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(ex.getMessage());
            return null;
        }
    }

    @Override
    public Email add(Email email) {
        try (Session s = hu.getSession()) {
            Transaction tx = null;
            tx = s.beginTransaction();
            int id = (int) s.save(email);
            email.setEmailId(id);
            tx.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(ex.getMessage());
            return null;
        }
        return email;
    }

    @Override
    public Email delete(Email email) {
        try (Session s = hu.getSession()) {
            Transaction tx = null;
            tx = s.beginTransaction();
            s.delete(email);
            tx.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(ex.getMessage());
            return null;
        }
        return email;
    }

    @Override
    public Email update(Email email) {
        try (Session s = hu.getSession()) {
            Transaction tx = null;
            tx = s.beginTransaction();
            s.update(email);
            tx.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(ex.getMessage());
            return null;
        }
        return email;
    }

    @Override
    public Email getById(Integer i) {
        Session s = hu.getSession();
        Email email = s.get(Email.class, i);
        s.close();
        return email;
    }

    @Override
    public Email getRandom() {
        try (Session s = hu.getSession()) {
            Random rand = new Random();
            String query = "FROM Email";
            Query<Email> q = s.createQuery(query, Email.class);
            List<Email> emailList = q.getResultList();
            return emailList.get(rand.nextInt(emailList.size() + 1));
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(ex.getMessage());
            return null;
        }
    }

    @Override
    public List<Email> getAll() {
        try (Session s = hu.getSession()) {
            String query = "FROM Email";
            Query<Email> q = s.createQuery(query, Email.class);
            List<Email> emails = q.getResultList();
            Collections.sort(emails);
            return emails;
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
            String query = "FROM Email";
            Query<Email> q = s.createQuery(query, Email.class);
            List<Email> e = q.getResultList();
            tx = s.beginTransaction();
            for (Email email: e) {
                s.delete(email);
            }
            tx.commit();
            s.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(ex.getMessage());
        }
    }
}
