package net.compsoc.ox.database.iface.events;

import java.util.Date;
import java.util.Set;

public interface Event {
    
    // Key
    public String keyString();
    
    // Getters
    
    // Unique Triplet
    public int year();
    public Term term();
    public String slug();
    
    public String title();
    public String description();
    public String facebookEventID();
    public Venue venue();
    public Set<Tag> tags();
    
    public Date startTimestamp();
    public Date endTimestamp();
    public Integer getTermWeek();
    
    // Setters
    public void setPrimary(int year, Term term, String slug);
    
    public void setTitle(String title);
    public void setDescription(String description);
    public void setFacebookEventID(String id);
    public void setVenue(Venue venue);
    public void setTags(Set<Tag> tags);
    
    public void setStartTimestamp(Date start);
    public void setEndTimestamp(Date end);
    
}
