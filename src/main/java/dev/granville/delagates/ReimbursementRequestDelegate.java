package dev.granville.delagates;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.granville.beans.*;
import dev.granville.service.DepartmentService;
import dev.granville.service.EmployeeService;
import dev.granville.service.ReimbursementEventTypeService;
import dev.granville.service.ReimbursementService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class ReimbursementRequestDelegate implements FrontControllerDelegate {
    private final ObjectMapper om = new ObjectMapper();
    private final EmployeeService employeeService = new EmployeeService();
    private final DepartmentService departmentService = new DepartmentService();
    private final ReimbursementService reimbursementService = new ReimbursementService();
    private final ReimbursementEventTypeService reimbursementEventTypeService = new ReimbursementEventTypeService();

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = (String) req.getAttribute("path");
        if (path.contains("request")) {
            if ("POST".equals(req.getMethod())) {
                ReimbursementRequest rr = registerRR(req, resp);
                if (rr == null) {
                    resp.sendError(404, "There was an error creating a request.");
                } else {
                    resp.sendError(200);
                    resp.getWriter().write(om.writeValueAsString(rr));
                }
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public ReimbursementRequest registerRR(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            ReimbursementRequest rr = new ReimbursementRequest();
            Employee e = employeeService.getById((Integer.valueOf(req.getParameter("employeeid"))));
            Employee er = employeeService.getById((Integer.valueOf(req.getParameter("reportsto"))));
            Department d = departmentService.getById(Integer.valueOf(req.getParameter("department")));
            ReimbursementEventType ret = reimbursementEventTypeService.getById(Integer.valueOf(req.getParameter("type")));
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            LocalDateTime start = LocalDateTime.parse(req.getParameter("startdate"));
            LocalDateTime end = LocalDateTime.parse(req.getParameter("enddate"));
            Urgency ugc = new Urgency();
            if ((start.toEpochSecond(ZoneOffset.UTC) - LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)) < 1500000) {
                ugc.setUrgencyId(2);
            } else {
                ugc.setUrgencyId(1);
            }
            rr.setReimbursementLocation(req.getParameter("location"));
            rr.setReimbursementCost(Double.valueOf(req.getParameter("cost")));
            rr.setReimbursementDate(LocalDate.from(LocalDateTime.now()));
            rr.setUrgency(ugc);
            rr.setTimeoffStart(start);
            rr.setTimeoffEnd(end);
            rr.setReimbursementTimeStamp(timestamp);
            rr.setDepartment(d);
            rr.setEmployee(e);
            rr.setReviewer(er);
            rr.setReimbursementEventType(ret);
            RequestStatus rs = new RequestStatus();
            rs.setStatusName("Pending");
            rs.setStatusId(e.getRank().getRankId() + 1);
            rr.setReimbursementStatus(rs);
            return reimbursementService.add(rr);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
