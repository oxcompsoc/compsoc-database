package net.compsoc.ox.database.iface.events;

import java.util.Date;
import java.util.Set;

import net.compsoc.ox.database.iface.core.IndexedItem;
import net.compsoc.ox.database.util.exceptions.DatabaseOperationException;

public interface Event<Key, VenueKey> extends IndexedItem<Key> {
    
    // Getters
    
    // Unique Triplet
    public int year();
    public Term term();
    public String slug();
    
    public String title();
    public String description();
    public String facebookEventID();
    public Venue<VenueKey> venue();
    public Set<Tag> tags() throws DatabaseOperationException;
    
    public Date startTimestamp();
    public Date endTimestamp();
    public Integer getTermWeek();
    
    // Setters
    public void setPrimary(int year, Term term, String slug);
    
    public void setTitle(String title);
    public void setDescription(String description);
    public void setFacebookEventID(String id);
    public void setVenue(Venue<VenueKey> venue);
    public void setTags(Set<Tag> tags);
    
    public void setStartTimestamp(Date start);
    public void setEndTimestamp(Date end);
    
}
