package net.compsoc.ox.database.iface.events;

import java.util.List;

import net.compsoc.ox.database.iface.core.InvalidKeyException;
import net.compsoc.ox.database.iface.core.KeyFactory;
import net.compsoc.ox.database.iface.core.NotFoundException;
import net.compsoc.ox.database.util.exceptions.DatabaseOperationException;

public interface Events<Key, VenueKey> {
    
    public KeyFactory<Key> getKeyFactory();
    
    public Terms terms();
    
    public Venues<VenueKey> venues();
    
    public Tags tags();
    
    public List<Event<Key, VenueKey>> getEvents() throws DatabaseOperationException;
    
    public Event<Key, VenueKey> getEvent(Key key) throws NotFoundException, InvalidKeyException,
        DatabaseOperationException;
    
    public Event<Key, VenueKey> addEvent(int year, Term term, String slug);
    
}
