package net.compsoc.ox.database.impl.sql;

import java.sql.Connection;
import java.sql.SQLException;

import net.compsoc.ox.database.Database;
import net.compsoc.ox.database.impl.sql.events.SQLEvents;

public abstract class SQLDatabase extends Database {
    
    private Connection connection;
    
    private SQLEvents events; // Lazily Instantiated
    
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
    
    protected void setupConnection(Connection connection) throws SQLException {
        this.connection = connection;
        this.events = new SQLEvents(connection);
        
    }
    
    private void requireConnection(){
        if(this.connection == null)
            throw new RuntimeException("Connection not yet setup");
    }
    
    @Override
    public SQLEvents events() {
        requireConnection();
        return events;
    }
    
}
