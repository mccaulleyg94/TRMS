package dev.granville.dao;

import dev.granville.beans.Department;

public interface DepartmentDAOInterface extends GenericDao<Department> {
    Department getByName(String departmentName);
}
