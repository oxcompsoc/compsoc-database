package net.compsoc.ox.database.impl.sql.events;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.compsoc.ox.database.iface.events.Venue;

public class SQLVenue implements Venue<String> {
    
    private final Connection connection;

    private final String slug;
    
    private String name;
    
    public SQLVenue(Connection connection, ResultSet currentSet)
        throws SQLException {
        this.connection = connection;

        this.slug = currentSet.getString("venue_slug");
        this.name = currentSet.getString("venue_name");
    }

    @Override
    public String key() {
        return slug;
    }

    @Override
    public synchronized String name() {
        return name;
    }

    @Override
    public String slug() {
        return slug;
    }

    @Override
    public synchronized void setSlugAndName(String slug, String name) {
        
    }
    
    
}
