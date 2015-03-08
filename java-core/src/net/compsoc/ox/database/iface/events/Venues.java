package net.compsoc.ox.database.iface.events;

import java.util.List;

import net.compsoc.ox.database.iface.core.KeyFactory;

public interface Venues<Key> {
    
    public KeyFactory<Key> getKeyFactory();
    
    public Venue<Key> getVenueByKey(Key key);
    
    //public Venue<Key> getVenueBySlug(String slug);
    
    public List<Venue<Key>> getVenues();
    
}
