package dev.granville.delagates;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface FrontControllerDelegate {
    void process(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException;
}
