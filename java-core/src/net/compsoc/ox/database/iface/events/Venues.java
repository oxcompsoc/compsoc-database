package net.compsoc.ox.database.iface.events;

import java.util.List;

import net.compsoc.ox.database.iface.core.KeyFactory;

public interface Venues<Key> {
    
    public KeyFactory<Key> getKeyFactory();
    
    /**
     * @param key
     * @return the {@link Venue} or null if none exists with that {@link Key}.
     */
    public Venue<Key> getVenueByKey(Key key);
    
    public List<Venue<Key>> getVenues();
    
    public void addVenue(String slug, String name);
    
}
