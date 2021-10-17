package dev.granville.service;


import dev.granville.beans.Employee;
import dev.granville.dao.EmployeeDAO;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EmployeeService {
	private final EmployeeDAO employeeDao = new EmployeeDAO();
	public Employee add(@NotNull Employee e) {
		if (employeeDao.getByUsername(e.getUsername()) != null) {
			return null;
		}
		return employeeDao.add(e);
	}
	public Employee delete(Employee e)  {
		return employeeDao.delete(e);
	}
	public Employee update(Employee e)  {
		return employeeDao.update(e);
	}
	public Employee getByUsername(String username) {
		return employeeDao.getByUsername(username);
	}
	public Employee getById(Integer id) {
		return employeeDao.getById(id);
	}
	public Employee getRandom() {
		return employeeDao.getRandom();
	}
	public List<Employee> getByDepartmentName (String departmentName) {
		return employeeDao.getByDepartmentName(departmentName);
	}
	public List<Employee> getAll() {
		return employeeDao.getAll();
	}
	public Boolean deleteAll() {
		try {
			employeeDao.deleteAll();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
