package dev.granville.service;

import dev.granville.beans.Department;
import dev.granville.dao.DepartmentDAO;

import java.util.List;

public class DepartmentService {
    private final DepartmentDAO departmentDao = new DepartmentDAO();
    public Department add(Department d) {
        return departmentDao.add(d);
    }
    public Department delete(Department d)  {
        return departmentDao.delete(d);
    }
    public Department update(Department d) {
        return departmentDao.update(d);
    }
    public Department getById(Integer id) {
        return departmentDao.getById(id);
    }
    public Department getByName(String departmentName) {
        return departmentDao.getByName(departmentName);
    }
    public List<Department> getAll() {
        return departmentDao.getAll();
    }
    public Boolean deleteAll() {
        try {
            departmentDao.deleteAll();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
