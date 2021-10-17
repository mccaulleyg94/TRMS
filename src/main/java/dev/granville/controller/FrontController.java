package dev.granville.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dev.granville.delagates.FrontControllerDelegate;
import org.apache.catalina.servlets.DefaultServlet;
import org.apache.log4j.Logger;

public class FrontController extends DefaultServlet {
    private final RequestHandler rh = new RequestHandler();
    private final Logger log = Logger.getLogger(FrontController.class);

    private void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FrontControllerDelegate fcd = rh.handle(req, resp);
        if (req.getRequestURI().substring(req.getContextPath().length()).startsWith("/static")) {
            super.doGet(req,resp);
        } else {
            if (fcd != null) {
                try {
                    fcd.process(req, resp);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.debug(e.getMessage());
                }
            }
            else
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        process(req, resp);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        process(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }
}
