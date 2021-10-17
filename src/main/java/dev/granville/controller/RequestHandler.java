package dev.granville.controller;

import dev.granville.delagates.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestHandler {
    private final Map<String, FrontControllerDelegate> delegateMap;
    private final Logger log = Logger.getLogger(RequestHandler.class);

    {
        delegateMap = new HashMap<>();
        delegateMap.put("employee", new EmployeeDelegate());
        delegateMap.put("login", new LoginDelegate());
        delegateMap.put("register", new RegistrationDelegate());
        delegateMap.put("reimbursementrequest", new ReimbursementRequestDelegate());
        delegateMap.put("reimbursement", new ReimbursementDelegate());
        delegateMap.put("email", new EmailDelegate());
    }

    public FrontControllerDelegate handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("OPTIONS".equals(req.getMethod())) {
            return (r1, r2) -> {};
        }
        StringBuilder uriString = new StringBuilder(req.getRequestURI());;
        uriString.replace(0, req.getContextPath().length()+1, "");
        if (uriString.indexOf("/") != -1) {
            req.setAttribute("path", uriString.substring(uriString.indexOf("/")+1));
            uriString.replace(uriString.indexOf("/"), uriString.length(), "");
        }
        log.debug(LocalDateTime.now() + " | " + uriString);
        return delegateMap.get(uriString.toString());
    }
}
