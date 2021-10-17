package dev.granville.delagates;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.granville.beans.Email;
import dev.granville.beans.Employee;
import dev.granville.beans.ReimbursementRequest;
import dev.granville.service.EmailService;
import dev.granville.service.EmployeeService;
import dev.granville.service.ReimbursementService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class ReimbursementDelegate implements FrontControllerDelegate {
    private final ObjectMapper om = new ObjectMapper();
    private final EmployeeService employeeService = new EmployeeService();
    private final ReimbursementService reimbursementService = new ReimbursementService();
    private final EmailService emailService = new EmailService();

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = (String) req.getAttribute("path");

        if  (path == null || path.equals("")) {
            if (req.getMethod().equals("GET")) {
                List<ReimbursementRequest> reimbursementRequestList = reimbursementService.getAll();
                resp.getWriter().write(om.writeValueAsString(reimbursementRequestList));
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else if (path.contains("accept")) {
            try {
                acceptRR(req, resp);
                resp.setStatus(200);
            } catch (Exception e) {
                e.printStackTrace();
                resp.sendError(409);
            }
        } else if (path.contains("deny")) {
            try {
                rejectRR(req, resp);
                resp.setStatus(200);
            } catch (Exception e) {
                e.printStackTrace();
                resp.sendError(409);
            }
        } else if (path.contains("employee")) {
            if (req.getMethod().equals("GET")) {
                List<ReimbursementRequest> reimbursementRequestList = getEligibleRequests(req, resp);
                Collections.sort(reimbursementRequestList);
                resp.getWriter().write(om.writeValueAsString(reimbursementRequestList));
                resp.setStatus(200);
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else if (path.contains("empRRS")) {
            if (req.getMethod().equals("GET")) {
                resp.getWriter().write(om.writeValueAsString(reimbursementService.getByEmployeeId(Integer.valueOf(req.getParameter("employeeId")))));
                resp.setStatus(200);
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else if (path.contains("alter")) {
            try {
                alterRR(req, resp);
                resp.setStatus(200);
            } catch (Exception e) {
                e.printStackTrace();
                resp.sendError(409);
            }
        } else if (path.contains("delete")) {
            try {
                deleteRR(req, resp);
                resp.setStatus(200);
            } catch (Exception e) {
                e.printStackTrace();
                resp.sendError(409);
            }
        } else if (path.contains("projected")) {
            resp.getWriter().write(om.writeValueAsString(sendProjectedReimbursementAmount(req, resp)));
        } else {
            resp.sendError(404);
        }
    }

    public double sendProjectedReimbursementAmount(HttpServletRequest req, HttpServletResponse resp) {
        double pending = 0.0;
        double cost = Double.parseDouble(req.getParameter("cost"));
        int pc = Integer.parseInt(req.getParameter("type"));
        switch (pc) {
            case 1:
                pending = (cost) * .8;
                break;
            case 2:
                pending = (cost) * .6;
                break;
            case 3:
                pending = (cost) * .75;
                break;
            case 4:
                pending = (cost);
                break;
            case 5:
                pending = (cost) * .9;
                break;
            default:
                pending = (cost) * .3;
                break;
        }
        if (pending < 0.0) {
            pending = 0.0;
        } else if (pending > 1000.0) {
            pending = 1000.0;
        }
        return pending;
    }

    public List<ReimbursementRequest> getEligibleRequests(HttpServletRequest req, HttpServletResponse resp) {
        Employee e = employeeService.getById(Integer.valueOf(req.getParameter("employeeId")));
        if (e.getRank().getRankId() < 3) {
            return reimbursementService.getAvailableForReviewWithRank(e);
        } else if (e.getRank().getRankId() >= 3) {
            return reimbursementService.getAvailableForBenCo(e);
        } else {
            return  null;
        }
    }

    public void deleteRR(HttpServletRequest req, HttpServletResponse resp) {
        ReimbursementRequest rr = reimbursementService.getById(Integer.valueOf(req.getParameter("rrId")));
        reimbursementService.delete(rr);
    }

    public void alterRR(HttpServletRequest req, HttpServletResponse resp) {
        ReimbursementRequest rr = reimbursementService.getById(Integer.valueOf(req.getParameter("rrId")));
        String notes = req.getParameter("comment");

        Double oldValue = rr.getReimbursementCost();
        Double newValue = Double.valueOf(req.getParameter("value"));
        Employee alterer = employeeService.getById(Integer.valueOf(req.getParameter("sender")));

        rr.setReimbursementCost(newValue);
        if (!notes.isEmpty()) {
            rr.setNotes(notes);
        }
        reimbursementService.update(rr);

        Email email = new Email();
        email.setTitle("A Reimbursement Offer was adjusted");
        email.setSender(alterer);
        email.setRecipient(rr.getEmployee());
        email.setContext("The amount you requested in your reimbursement form was changed from " + oldValue + " to "
                + newValue + " by " + alterer.getUsername());
        email.setSentTime(LocalDateTime.now());
        emailService.add(email);

    }

    public void acceptRR(HttpServletRequest req, HttpServletResponse resp) {
        Employee reviewer =  employeeService.getById(Integer.valueOf(req.getParameter("employeeId")));
        ReimbursementRequest rr = reimbursementService.getById(Integer.valueOf(req.getParameter("rrId")));
        Employee recipient = employeeService.getById(rr.getEmployee().getEmployeeId());
        rr.updateStatus(reviewer.getRank().getRankId() + 1);
        if (req.getParameter("reportsto") != null) {
            rr.setReviewer(employeeService.getById(Integer.valueOf(req.getParameter("reportsto"))));
        } else {
            rr.setReviewer(null);
        }
        if (rr.getReimbursementStatus().getStatusId() == 4) {
            recipient.addAid(rr.coveredCost());
            employeeService.update(reviewer);
        }
        String notes = req.getParameter("comment");
        if (!notes.isEmpty()) {
            rr.setNotes(notes);
        }
        reimbursementService.update(rr);
        Email email = new Email();
        email.setTitle("A Reimbursement Offer was approved");
        email.setSender(reviewer);
        email.setRecipient(rr.getEmployee());
        email.setContext(reviewer.getUsername() + " has approved your reimbursement request.\n" +
                (reviewer.getReportsTo() == null ? "" : " Your request will now be reviewed by " + reviewer.getReportsTo().getUsername()));
        email.setSentTime(LocalDateTime.now());
        emailService.add(email);
    }

    public void rejectRR(HttpServletRequest req, HttpServletResponse resp) {
        Employee e =  employeeService.getById(Integer.valueOf(req.getParameter("employeeId")));
        ReimbursementRequest rr = reimbursementService.getById(Integer.valueOf(req.getParameter("rrId")));
        rr.updateStatus(0);
        String notes = req.getParameter("comment");
        if (!notes.isEmpty()) {
            rr.setNotes(notes);
        }
        reimbursementService.update(rr);
        Email email = new Email();
        email.setTitle("A Reimbursement Offer was rejected");
        email.setSender(e);
        email.setRecipient(rr.getEmployee());
        email.setContext(e.getUsername() + " has rejected your reimbursement request." + "\nComment: " + notes);
        email.setSentTime(LocalDateTime.now());
        emailService.add(email);
    }
}
