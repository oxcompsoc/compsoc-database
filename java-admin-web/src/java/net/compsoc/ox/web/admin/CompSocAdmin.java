package net.compsoc.ox.web.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import net.compsoc.ox.database.Database;
import net.compsoc.ox.database.util.exceptions.ConfigurationException;
import net.compsoc.ox.database.util.exceptions.DatabaseInitialisationException;
import net.compsoc.ox.web.admin.config.CompSocAdminConfig;
import net.compsoc.ox.web.admin.config.CompSocYAMLAdminConfig;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class CompSocAdmin {
    
    public static void main(String[] args) throws Exception {
        
        CompSocAdminConfig config;
        Database db;
        
        try {
            config = getConfig(args);
            db = getDatabase(config);
        } catch (ConfigurationException e) {
            System.out.print("Could not start, configuration error: ");
            System.out.println(e.getMessage());
            return;
        } catch (DatabaseInitialisationException e) {
            System.out.print("Error during database initialisation: ");
            System.out.println(e.getMessage());
            return;
        }
        
        System.out.println("Starting server on port " + config.port());
        Server server = new Server(config.port());
        
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath(config.rootPath());
        server.setHandler(context);
        
        // Dynamic Pages
        context.addServlet(new ServletHolder(new AdminServlet(db)), "/*");
        context.addServlet(new ServletHolder(new StaticServlet()), "/static/*");

        System.out.println("READY...");
        
        server.start();
        server.join();
    }
    
    private static CompSocAdminConfig getConfig(String[] args) throws ConfigurationException {
        
        File configFile = null;
        if (args.length == 1) {
            configFile = new File(args[0]);
        } else {
            throw new ConfigurationException("No configuration file path specified in command line");
        }
        
        System.out.println("Using Configuration File: " + configFile.getAbsolutePath());
        try (InputStream is = new FileInputStream(configFile)) {
            return CompSocYAMLAdminConfig.load(is);
        } catch (FileNotFoundException e) {
            throw new ConfigurationException("config file does not exist: " + configFile);
        } catch (IOException e) {
            throw new ConfigurationException("Error reading config file: " + configFile);
        }
    }
    
    private static Database getDatabase(CompSocAdminConfig config)
        throws DatabaseInitialisationException, ConfigurationException {
        
        if (config.database() == null)
            throw new ConfigurationException("No database options specified in config");
        
        return Database.fromConfig(config.database());
        
    }
}
