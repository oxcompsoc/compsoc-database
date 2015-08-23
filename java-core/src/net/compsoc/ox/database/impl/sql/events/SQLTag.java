package net.compsoc.ox.database.impl.sql.events;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.compsoc.ox.database.iface.events.Tag;

public class SQLTag implements Tag {
    
    private final int key;
    
    private final String name;
    private final String slug;
    
    public SQLTag(ResultSet currentSet)
        throws SQLException {
        this.key = currentSet.getInt("tag_id");
        this.name = currentSet.getString("tag_name");
        this.slug = currentSet.getString("tag_slug");
    }
    
    public int key(){
        return key;
    }
    
    @Override
    public String name() {
        return name;
    }
    
    @Override
    public String slug() {
        return slug;
    }
    
}
