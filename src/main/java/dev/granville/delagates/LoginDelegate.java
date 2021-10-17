package dev.granville.delagates;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.granville.beans.Employee;
import dev.granville.service.EmployeeService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginDelegate implements FrontControllerDelegate {
    private final ObjectMapper om = new ObjectMapper();
    private final EmployeeService employeeService = new EmployeeService();

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = (String) req.getAttribute("path");
        if (path.contains("login")) {
            if ("POST".equals(req.getMethod()))
                logIn(req, resp);
            else if ("DELETE".equals(req.getMethod()))
                req.getSession().invalidate();
            else
                resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        } else {
            userWithId(req, resp, Integer.valueOf(path));
        }
    }

    private void logIn(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        Employee e = employeeService.getByUsername(username);
        if (e != null) {
            if (e.getPassword().equals(password)) {
                req.getSession().setAttribute("employee", e.getEmployeeId());
                resp.getWriter().write(om.writeValueAsString(e));
            } else {
                resp.sendError(404, "Incorrect password.");
            }
        } else {
            resp.sendError(404, "No user found with that username.");
        }
    }

    private void userWithId(HttpServletRequest req, HttpServletResponse resp, Integer id) throws IOException {
        switch (req.getMethod()) {
            case "GET":
                Employee e = employeeService.getById(id);
                if (e != null) {
                    resp.getWriter().write(om.writeValueAsString(e));
                } else {
                    resp.sendError(404, "User not found.");
                }
                break;
            case "PUT":
                String password = req.getParameter("pass");
                Employee employee = (Employee) req.getSession().getAttribute("employee");
                if (employee != null) {
                    employee.setPassword(password);
                    employeeService.update(employee);
                } else
                    resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                break;
            case "DELETE":
                Employee user = om.readValue(req.getInputStream(), Employee.class);
                employeeService.delete(user);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                break;
        }
    }
}
