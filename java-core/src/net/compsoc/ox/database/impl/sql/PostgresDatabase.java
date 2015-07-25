package net.compsoc.ox.database.impl.sql;

import java.sql.DriverManager;
import java.sql.SQLException;

import net.compsoc.ox.database.util.exceptions.DatabaseInitialisationException;

public class PostgresDatabase extends SQLDatabase {
    
    public PostgresDatabase(String host, int port, String database, String username, String password)
        throws DatabaseInitialisationException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new DatabaseInitialisationException(e);
        }
        
        // Construct URL
        String url;
        if (host == null)
            url = String.format("jdbc:postgresql:%s", database);
        else if (port == -1)
            url = String.format("jdbc:postgresql://%s/%s", host, database);
        else
            url = String.format("jdbc:postgresql://%s:%d/%s", host, port, database);
        
        System.out.println("Connecting to POSTGRESQL Server using URL: " + url);
        
        try {
            setupConnection(DriverManager.getConnection(url, username, password));
        } catch (SQLException e) {
            throw new DatabaseInitialisationException(e);
        }
    }
    
}
