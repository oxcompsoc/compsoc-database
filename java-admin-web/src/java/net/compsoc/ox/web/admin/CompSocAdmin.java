package net.compsoc.ox.web.admin;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class CompSocAdmin {
    
    public static void main(String[] args) throws Exception {
        
        
        Server server = new Server(8080);
 
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/admin/");
        server.setHandler(context);
 
        // Dynamic Pages
        context.addServlet(new ServletHolder(new AdminServlet()),"/*");

        // Static Content
        ServletHolder holderStatic = new ServletHolder("static-home", DefaultServlet.class);
        holderStatic.setInitParameter("resourceBase", "resources/static");
        holderStatic.setInitParameter("dirAllowed", "false");
        holderStatic.setInitParameter("pathInfoOnly","true");
        context.addServlet(holderStatic,"/static/*");

        server.start();
        server.join();
    }
}
