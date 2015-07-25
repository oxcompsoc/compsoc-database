package net.compsoc.ox.database.impl.sql.events;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.compsoc.ox.database.iface.events.Tag;

public class SQLTag implements Tag {
    
    private final Connection connection;
    
    private final int key;
    
    private String name;
    private String slug;
    
    public SQLTag(Connection connection, ResultSet currentSet)
        throws SQLException {
        this.connection = connection;
        
        this.key = currentSet.getInt("tag_id");
        this.name = currentSet.getString("tag_name");
        this.slug = currentSet.getString("tag_slug");
    }
    
    public int key(){
        return key;
    }
    
    @Override
    public synchronized String name() {
        return name;
    }
    
    @Override
    public synchronized String slug() {
        return slug;
    }
    
}
