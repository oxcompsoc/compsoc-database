package net.compsoc.ox.database.iface.events;

import java.util.Date;

public interface Event {
    
    // Unique
    public int year();
    public Term term();
    public String slug();
    
    public String title();
    public String description();
    public String facebookEventID();
    public Venue venue();
    
    public Date startTimestamp();
    public Date endTimestamp();
    
}
