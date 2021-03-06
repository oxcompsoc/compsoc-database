package net.compsoc.ox.web.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.compsoc.ox.database.Database;
import net.compsoc.ox.web.admin.sections.RootSection;
import net.compsoc.ox.web.admin.sections.Section;
import net.compsoc.ox.web.admin.templating.Template;
import net.compsoc.ox.web.admin.util.PageBuilder;
import net.compsoc.ox.web.admin.util.PageRenderer;
import net.compsoc.ox.web.admin.util.PathInfo;
import net.compsoc.ox.web.admin.util.RedirectException;
import net.compsoc.ox.web.admin.util.StatusException;

public class AdminServlet extends HttpServlet {
    
    private final Database database;
    private final PageRenderer pageRenderer = new PageRenderer();
    private final Section rootSection = new RootSection();
    
    public AdminServlet(Database database) {
        this.database = database;
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        handle(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        handle(request, response);
    }
    
    private void handle(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        PageBuilder builder = new PageBuilder(pageRenderer, database, request, response);

        try {
            rootSection.handle(new PathInfo(request), builder);
        } catch (RedirectException e) {
            response.setStatus(e.code);
            response.setHeader("Location", e.location);
        } catch (StatusException e) {
            //response.sendError(e.code);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
            try {
                builder.render(Template.ERROR_500);
            } catch (Exception e2) {
                e2.printStackTrace();
                response.getWriter().write("Internal Error");
            }
        }
        
    }
}
