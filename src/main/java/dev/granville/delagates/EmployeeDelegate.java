package dev.granville.delagates;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.granville.beans.Department;
import dev.granville.beans.Employee;
import dev.granville.service.DepartmentService;
import dev.granville.service.EmployeeService;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EmployeeDelegate implements FrontControllerDelegate {

    private final EmployeeService employeeService = new EmployeeService();
    private final DepartmentService departmentService = new DepartmentService();
    private final ObjectMapper om = new ObjectMapper();

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = (String) req.getAttribute("path");

        System.out.println(this.getClass().getSimpleName());

        final int id = Integer.parseInt((path.substring(path.indexOf("/") + 1)));
        if (path.contains("department")) {
            if ("GET".equals(req.getMethod())) {
                if (path.contains("/")) {
                    Department d = departmentService.getById(id);
                    if (d != null) {
                        resp.getWriter().write(om.writeValueAsString(d));
                        resp.setStatus(200);
                    } else {
                        resp.setStatus(404);
                    }
                } else {
                    List<Department> departments = departmentService.getAll();
                    resp.getWriter().write(om.writeValueAsString(departments));
                    resp.setStatus(200);
                }
            } else {
                resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            }
        } else if (path.contains("employee")) {
            switch (req.getMethod()) {
                case "GET":
                    if (path.contains("/")) {
                        Employee e = employeeService.getById(id);
                        if (e != null) {
                            resp.getWriter().write(om.writeValueAsString(e));
                            resp.setStatus(200);
                        } else {
                            resp.setStatus(404);
                        }
                        break;
                    } else {
                        List<Employee> employees = employeeService.getAll();
                        resp.getWriter().write(om.writeValueAsString(employees));
                    }
                    break;
                case "POST":
                    Employee e = om.readValue(req.getInputStream(), Employee.class);
                    employeeService.add(e);
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    break;
            }
        } else {
            resp.sendError(404);
        }
    }
}
