package net.compsoc.ox.web.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.compsoc.ox.database.Database;
import net.compsoc.ox.database.config.CompSocConfig;
import net.compsoc.ox.database.config.CompSocYAMLConfig;
import net.compsoc.ox.database.util.exceptions.ConfigurationException;
import net.compsoc.ox.database.util.exceptions.DatabaseInitialisationException;
import net.compsoc.ox.web.admin.sections.RootSection;
import net.compsoc.ox.web.admin.sections.Section;
import net.compsoc.ox.web.admin.util.PageBuilder;
import net.compsoc.ox.web.admin.util.PageRenderer;
import net.compsoc.ox.web.admin.util.PathInfo;
import net.compsoc.ox.web.admin.util.RedirectException;
import net.compsoc.ox.web.admin.util.StatusException;

public class AdminServlet extends HttpServlet {
    
    private final Database database;
    private final PageRenderer pageRenderer = new PageRenderer();
    private final Section rootSection = new RootSection();
    
    public AdminServlet() {
        // Create Database
        Database database = null;
        try {
            database = Database.fromConfig(loadConfig());
        } catch (ConfigurationException e) {
            System.out.print("Could not start, configuration error: ");
            System.out.println(e.getMessage());
            System.exit(1);
        } catch (DatabaseInitialisationException e) {
            e.printStackTrace();
            System.exit(1);
        }
        this.database = database;
    }
    
    private CompSocConfig loadConfig() throws ConfigurationException {
        return new CompSocYAMLConfig(null);
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
        
        try {
            rootSection.handle(new PathInfo(request), new PageBuilder(pageRenderer, database,
                request, response));
        } catch (RedirectException e) {
            response.setStatus(e.code);
            response.setHeader("Location", e.location);
        } catch (StatusException e) {
            response.sendError(e.code);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        
    }
}
