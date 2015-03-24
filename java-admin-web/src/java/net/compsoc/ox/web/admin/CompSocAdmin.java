package net.compsoc.ox.web.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.compsoc.ox.database.Database;
import net.compsoc.ox.database.config.CompSocConfig;
import net.compsoc.ox.database.config.CompSocYAMLConfig;
import net.compsoc.ox.database.util.exceptions.ConfigurationException;
import net.compsoc.ox.database.util.exceptions.DatabaseInitialisationException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class CompSocAdmin {
    
    public static void main(String[] args) throws Exception {
        
        Database db = getDatabase(args);
        if (db == null)
            return;
        
        Server server = new Server(8080);
        
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/admin/");
        server.setHandler(context);
        
        // Dynamic Pages
        context.addServlet(new ServletHolder(new AdminServlet(db)), "/*");
        context.addServlet(new ServletHolder(new StaticServlet()), "/static/*");
        
        server.start();
        server.join();
    }
    
    private static Database getDatabase(String[] args) {
        
        String configFileLocation = null;
        if(args.length == 1){
            configFileLocation = args[0];
        } else {
            System.out.println("No configuration file path specified in command line");
            return null;
        }
        
        try {
            return Database.fromConfig(loadConfig(configFileLocation));
        } catch (ConfigurationException e) {
            System.out.print("Could not start, configuration error: ");
            System.out.println(e.getMessage());
            return null;
        } catch (DatabaseInitialisationException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static CompSocConfig loadConfig(String filePath) throws ConfigurationException {
        File f = new File(filePath);
        System.out.println("Using Configuration File: " + f.getAbsolutePath());
        try (InputStream is = new FileInputStream(f)) {
            return new CompSocYAMLConfig(is);
        } catch (IOException e) {
            throw new ConfigurationException("Error reading config file: " + f);
        }
    }
}
