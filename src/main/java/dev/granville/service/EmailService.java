package dev.granville.service;

import dev.granville.beans.Email;
import dev.granville.beans.Employee;
import dev.granville.beans.ReimbursementRequest;
import dev.granville.dao.EmailDAO;

import java.util.List;

public class EmailService {
    private final EmailDAO emailDao = new EmailDAO();
    public Email add(Email d) {
        return emailDao.add(d);
    }
    public Email delete(Email d)  {
        return emailDao.delete(d);
    }
    public Email update(Email d) {
        return emailDao.update(d);
    }
    public Email getById(Integer id) {
        return emailDao.getById(id);
    }
    public Email getRandom() {
        return emailDao.getRandom();
    }
    public List<Email> getAll() {
        return emailDao.getAll();
    }
    public List<Email> getUserEmails(Employee e) {
        return emailDao.getUserEmails(e);
    }
    public Boolean deleteAll() {
        try {
            emailDao.deleteAll();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
