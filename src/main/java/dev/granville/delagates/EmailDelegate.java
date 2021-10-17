package dev.granville.delagates;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.granville.beans.Email;
import dev.granville.service.EmailService;
import dev.granville.service.EmployeeService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class EmailDelegate implements FrontControllerDelegate {
    private final EmailService emailService = new EmailService();
    private final EmployeeService employeeService = new EmployeeService();
    private final ObjectMapper om = new ObjectMapper();

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = (String) req.getAttribute("path");

        if (path.contains("send")) {
            if (req.getMethod().equals("POST")) {
                try {
                    Email email = send(req, resp);
                    resp.getWriter().write(om.writeValueAsString(email));
                    resp.setStatus(200);
                } catch (Exception e) {
                    e.printStackTrace();
                    resp.sendError(HttpServletResponse.SC_CONFLICT);
                }
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else if (path.contains("check")) {
            if (req.getMethod().equals("GET")) {
                try {
                    List<Email> emails = check(req, resp);
                    resp.getWriter().write(om.writeValueAsString(emails));
                    resp.setStatus(200);
                } catch (Exception e) {
                    e.printStackTrace();
                    resp.sendError(HttpServletResponse.SC_CONFLICT);
                }
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else if (path.contains("open")) {
            if (req.getMethod().equals("GET")) {
                try {
                    Email email = open(req, resp);
                    resp.getWriter().write(om.writeValueAsString(email));
                    resp.setStatus(200);
                } catch (Exception e) {
                    e.printStackTrace();
                    resp.sendError(409);
                }
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            resp.sendError(404);
        }
    }

    public Email open (HttpServletRequest req, HttpServletResponse resp) {
        try {
            return emailService.getById(Integer.valueOf(req.getParameter("emailId")));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Email send(HttpServletRequest req, HttpServletResponse resp) {
        Email email = new Email();
        email.setRecipient(employeeService.getById(Integer.valueOf(req.getParameter("recipient"))));
        email.setSender(employeeService.getById(Integer.valueOf(req.getParameter("sender"))));
        email.setTitle(req.getParameter("title"));
        email.setContext(req.getParameter("context"));
        email.setSentTime(LocalDateTime.now());
        emailService.add(email);
        return email;
    }

    public List<Email> check(HttpServletRequest req, HttpServletResponse resp) {
        return emailService.getUserEmails(employeeService.getById(Integer.valueOf(req.getParameter("rId"))));
    }
}
