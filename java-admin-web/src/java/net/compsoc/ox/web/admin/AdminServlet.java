package net.compsoc.ox.web.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.compsoc.ox.database.Database;
import net.compsoc.ox.database.impl.dummy.DummyDatabase;
import net.compsoc.ox.web.admin.sections.RootSection;
import net.compsoc.ox.web.admin.sections.Section;
import net.compsoc.ox.web.admin.util.PageBuilder;
import net.compsoc.ox.web.admin.util.PageRenderer;
import net.compsoc.ox.web.admin.util.PathInfo;
import net.compsoc.ox.web.admin.util.StatusException;

public class AdminServlet extends HttpServlet {
    
    private final Database database;
    private final PageRenderer pageRenderer = new PageRenderer();
    private final Section rootSection = new RootSection();
    
    public AdminServlet() {
        // Create Database
        database = new DummyDatabase();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        try {
            rootSection.handle(new PathInfo(request), new PageBuilder(pageRenderer, database,
                request, response));
        } catch (StatusException e) {
            response.sendError(e.code);
        }
        
    }
}
