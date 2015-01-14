package net.compsoc.ox.web.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.compsoc.ox.database.Database;
import net.compsoc.ox.database.impl.dummy.DummyDatabase;

public class AdminServlet extends HttpServlet {
    
    private final Database database;
    
    public AdminServlet(){
        // Create Database
        database = new DummyDatabase();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>Hello Servlet</h1>");
        response.getWriter().println("session=" + request.getSession(true).getId());
    }
}
