package net.compsoc.ox.database.impl.sql;

import java.sql.Connection;
import java.sql.SQLException;

import net.compsoc.ox.database.Database;

public abstract class SQLDatabase extends Database {
    
    private Connection connection;
    
    public SQLDatabase() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Shutting Down SQL Database...");
                if (connection != null) {
                    System.out.println("   ...Closing Connection");
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("   ...No Connections to Close");
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
