package net.compsoc.ox.database.impl.sql;

import java.sql.Connection;
import java.sql.SQLException;

import net.compsoc.ox.database.Database;

public abstract class SQLDatabase extends Database {
    
    private Connection connection;
    
    public SQLDatabase() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Shutting down any SQL Database Connections");
                if (connection != null)
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
            }
        });
    }
    
    protected void setupConnection(Connection connection) {
        if (connection != null)
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        this.connection = connection;
        
    }
    
}
