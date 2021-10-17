package dev.granville.dao;

import dev.granville.beans.Email;
import dev.granville.beans.Employee;

import java.util.List;

public interface EmailDAOInterface extends GenericDao<Email> {
    List<Email> getUserEmails(Employee e);
}
