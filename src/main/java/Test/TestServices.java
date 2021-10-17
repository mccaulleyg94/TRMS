package Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.granville.beans.*;
import dev.granville.service.*;
import org.junit.Test;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Random;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestServices {

    private final EmailService emailService = new EmailService();
    private final EmployeeService employeeService = new EmployeeService();
    private final ReimbursementService reimbursementService = new ReimbursementService();
    private final ReimbursementEventTypeService reimbursementEventTypeService = new ReimbursementEventTypeService();
    private final DepartmentService departmentService = new DepartmentService();

    @Test
    @Order(1)
    public void testAddEmployee() {
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            Employee e = new Employee();
            if (i == 0) {
                e.setReportsTo(null);
            } else {
                e.setReportsTo(employeeService.getRandom());
            }
            e.setAid((double) random.nextInt(1000));

            EmployeeRank er = new EmployeeRank();
            er.setRankId(random.nextInt(4));
            e.setRank(er);

            e.setFirstName("test");
            e.setLastName("test");
            e.setPassword("test");
            e.setUsername("user" + random.nextInt(1000000));
            e.setDepartment(departmentService.getById((random.nextInt(5) + 1)));
            e.setAddress("test");
            e.setEmployeeId(employeeService.add(e).getEmployeeId());
            Assertions.assertEquals(e.getEmployeeId(), employeeService.getById(e.getEmployeeId()).getEmployeeId());
        }
    }

    @Test
    @Order(2)
    public void testSelectEmployeeById() {
        Employee e = employeeService.getRandom();
        Assertions.assertEquals(e.getEmployeeId(), employeeService.getById(e.getEmployeeId()).getEmployeeId());
    }

    @Test
    @Order(3)
    public void testGetEmployeeByUsername() {
        Employee e = employeeService.getRandom();
        Employee e2 = employeeService.getByUsername(e.getUsername());
        Assertions.assertEquals(e2.getUsername(), e.getUsername());
    }

    @Test
    @Order(4)
    public void testUpdateEmployee() {
        Random random = new Random();
        List<Employee> employees = employeeService.getAll();
        int index = employees.get(0).getEmployeeId();
        for (int i = index; i < index + employees.size(); i++) {
            System.out.println(i + " | " + employees.size());
            Employee e = employeeService.getById(i);
            String oldLogin = e.getUsername() + "-" + e.getPassword();
            System.out.println(oldLogin);
            e.setUsername("update" + random.nextInt(100000));
            e.setPassword("update" + random.nextInt(100000));
            employeeService.update(e);
            Employee e2 = employeeService.getById(i);
            String newLogin = e2.getUsername() + "-" + e2.getPassword();
            System.out.println(newLogin);
            Assertions.assertNotEquals(oldLogin, newLogin);
        }
    }

    @Test
    @Order(5)
    public void testAddReimbursementRequests() {
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            Employee e = employeeService.getRandom();
            ReimbursementRequest rr = new ReimbursementRequest();
            rr.setReviewer(e.getReportsTo());
            rr.setReimbursementCost((double) random.nextInt(1000));
            rr.setNotes("Note");

            RequestStatus rs = new RequestStatus();
            rs.setStatusId(e.getRank().getRankId() + 1);
            rr.setReimbursementStatus(rs);

            rr.setEmployee(e);
            rr.setDepartment(e.getDepartment());

            String start;
            String end;
            if (i == 3) {
                start = "2021-03-12T09:10:30";
                end = "2022-03-12T09:10:30";
            } else {
                start = "2023-12-12T09:10:30";
                end = "2024-03-12T09:10:30";

            }
            LocalDateTime startTime = LocalDateTime.parse(start);
            LocalDateTime endTime = LocalDateTime.parse(end);
            rr.setTimeoffStart(startTime);
            rr.setTimeoffEnd(endTime);

            Urgency ugc = new Urgency();

            System.out.println(startTime.toEpochSecond(ZoneOffset.UTC) - LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));

            if ((startTime.toEpochSecond(ZoneOffset.UTC) - LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)) < 1500000) {
                ugc.setUrgencyId(2);
            } else {
                ugc.setUrgencyId(1);
            }
            rr.setUrgency(ugc);


            rr.setReimbursementTimeStamp(new Timestamp(System.currentTimeMillis()));
            rr.setReimbursementEventType(reimbursementEventTypeService.getRandom());
            rr.setReimbursementDate(LocalDate.from(LocalDateTime.now()));
            rr.setReimbursementLocation("Florida");

            rr.setReimbursementId(reimbursementService.add(rr).getReimbursementId());
            Assertions.assertEquals(rr.getReimbursementId(), reimbursementService.getById(rr.getReimbursementId()).getReimbursementId());
        }
    }

    @Test
    @Order(6)
    public void testGetReimbursementRequests() {
        List<ReimbursementRequest> reimbursementRequests = reimbursementService.getAll();
        Assertions.assertNotNull(reimbursementRequests);
        for (ReimbursementRequest rr: reimbursementRequests) {
            System.out.println(rr.toString());
        }
    }

    @Test
    @Order(7)
    public void testGetRRById() {
        ReimbursementRequest rr = reimbursementService.getRandom();
        Assertions.assertEquals(rr.getReimbursementId(),
                reimbursementService.getById(rr.getReimbursementId()).getReimbursementId());
    }

    @Test
    @Order(8)
    public void testRejectRR() {
        ReimbursementRequest rr = reimbursementService.getRandom();
        RequestStatus rs = new RequestStatus();
        rs.setStatusId(0);
        rr.setReimbursementStatus(rs);
        reimbursementService.update(rr);
        Assertions.assertEquals("Rejected",
                reimbursementService
                        .getById(rr.getReimbursementId())
                        .getReimbursementStatus().getStatusName());

    }

    @Test
    @Order(9)
    public void testSelectDepartmentByName() {
        Department d = departmentService.getByName("Management");
        Assertions.assertEquals("Management", d.getDepartmentName());
    }

    @Test
    @Order(10)
    public void testGetAllDepartments() {
        List<Department> departments = departmentService.getAll();
        Assertions.assertEquals(5, departments.size());
    }

    @Test
    @Order(11)
    public void testEmployeeToJSON() throws IOException {
        Employee e = employeeService.getRandom();
        ObjectMapper om = new ObjectMapper();
        om.writeValue(new File("target/employee.json"), e);
    }

    @Test
    @Order(12)
    public void testRRToJSON() throws IOException {
        ReimbursementRequest reimbursementRequest = reimbursementService.getRandom();
        ObjectMapper om = new ObjectMapper();
        om.writeValue(new File("target/reimbursementrequest.json"), reimbursementRequest);
    }

    @Test
    @Order(13)
    public void testProjectedValueToJSON() throws IOException {
        double d = 10.0;
        ObjectMapper om = new ObjectMapper();
        om.writeValue(new File("target/reimbursementrequest.json"), d);
    }

    @Test
    @Order(14)
    public void testEmailToJSON() throws IOException {
        Email email = new Email();
        email.setRecipient(employeeService.getRandom());
        email.setSentTime(LocalDateTime.now());
        email.setContext("Hello");
        email.setSender(employeeService.getRandom());
        email.setTitle("Hello");
        emailService.add(email);
        ObjectMapper om = new ObjectMapper();
        om.writeValue(new File("target/mail.json"), email);
    }

    @Test
    @Order(15)
    public void testDeleteAllEmployees() {
        Assertions.assertTrue(employeeService.deleteAll());
        List<Employee> employees = employeeService.getAll();
        Assertions.assertEquals(0, employees.size());
    }

    @Test
    @Order(16)
    public void testDeleteAllReimbursementRequests() {
        Assertions.assertTrue(reimbursementService.deleteAll());
        List<ReimbursementRequest> reimbursementRequests = reimbursementService.getAll();
        Assertions.assertEquals(0, reimbursementRequests.size());
    }

    @Test
    @Order(17)
    public void testDeleteAllEmails() {
        Assertions.assertTrue(emailService.deleteAll());
        List<Email> emails = emailService.getAll();
        Assertions.assertEquals(0, emails.size());
    }

}
