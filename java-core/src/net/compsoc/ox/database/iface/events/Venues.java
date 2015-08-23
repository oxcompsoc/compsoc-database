package net.compsoc.ox.database.iface.events;

import java.util.Collection;

import net.compsoc.ox.database.util.exceptions.DatabaseOperationException;

public interface Venues {
    
    /**
     * @return the {@link Venue} or null if none exists with that slug.
     */
    public Venue getVenueBySlug(String slug);
    
    public Collection<? extends Venue> getVenues();
    
    public void addVenue(String slug, String name) throws DatabaseOperationException;
    
    public void setVenueSlugAndName(String currentSlug, String slug, String name)
        throws DatabaseOperationException;
    
}
