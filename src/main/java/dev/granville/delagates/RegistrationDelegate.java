package dev.granville.delagates;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.granville.beans.Employee;
import dev.granville.beans.EmployeeRank;
import dev.granville.service.DepartmentService;
import dev.granville.service.EmployeeService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegistrationDelegate implements FrontControllerDelegate {
    private final ObjectMapper om = new ObjectMapper();
    private final EmployeeService employeeService = new EmployeeService();
    private final DepartmentService departmentService = new DepartmentService();

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = (String) req.getAttribute("path");
        if (path.contains("register")) {
            if ("POST".equals(req.getMethod())) {
                Employee e = registerEmployee(req, resp);
                if (e == null) {
                    resp.sendError(404, "There was an error creating your account.");
                } else {
                    resp.sendError(200);
                    resp.getWriter().write(om.writeValueAsString(e));
                }
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public Employee registerEmployee(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Employee e = new Employee();
            e.setAddress(req.getParameter("address"));
            e.setUsername(req.getParameter("username"));
            e.setPassword(req.getParameter("password"));
            e.setFirstName(req.getParameter("firstname"));
            e.setLastName(req.getParameter("lastname"));

            EmployeeRank er = new EmployeeRank();
            er.setRankId(Integer.parseInt(req.getParameter("rank")));
            e.setRank(er);

            e.setDepartment(departmentService.getById(Integer.valueOf(req.getParameter("department"))));
            if (req.getParameter("reportsto").equals("null")) {
                e.setReportsTo(null);
            } else {
                e.setReportsTo(employeeService.getById(Integer.valueOf(req.getParameter("reportsto"))));
            }
            return employeeService.add(e);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
