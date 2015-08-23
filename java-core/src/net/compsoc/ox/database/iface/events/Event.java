package net.compsoc.ox.database.iface.events;

import java.util.Date;
import java.util.Set;

import net.compsoc.ox.database.iface.core.IndexedItem;
import net.compsoc.ox.database.util.exceptions.DatabaseOperationException;

public interface Event<Key> extends IndexedItem<Key> {
    
    // Getters
    
    // Unique Triplet
    public int year();
    public Term term();
    public String slug();
    
    public String title();
    public String description();
    public String facebookEventID();
    public Venue venue();
    public Set<Tag> tags() throws DatabaseOperationException;
    
    public Date startTimestamp();
    public Date endTimestamp();
    public Integer getTermWeek();
    
}
