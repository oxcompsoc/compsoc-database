package net.compsoc.ox.database.iface.events;

import java.util.List;

public interface Venues {
    
    public Venue getVenueBySlug(String slug);
    
    public List<Venue> getVenues();
    
}
