package dev.granville.dao;

import dev.granville.beans.Employee;

import java.util.List;

public interface EmployeeDAOInterface extends GenericDao<Employee> {
    Employee getRandom();
    void deleteAll();
    Employee getByUsername(String username);
    List<Employee> getByDepartmentName(String departmentName);
}
