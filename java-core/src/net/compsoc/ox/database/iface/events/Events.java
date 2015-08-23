package net.compsoc.ox.database.iface.events;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import net.compsoc.ox.database.iface.core.InvalidKeyException;
import net.compsoc.ox.database.iface.core.KeyFactory;
import net.compsoc.ox.database.iface.core.NotFoundException;
import net.compsoc.ox.database.util.exceptions.DatabaseOperationException;

public interface Events<Key> {
    
    public KeyFactory<Key> getKeyFactory();
    
    public Terms terms();
    
    public Venues venues();
    
    public Tags tags();
    
    public Collection<? extends Event<Key>> getEvents() throws DatabaseOperationException;
    
    public Event<Key> getEvent(Key key) throws NotFoundException, InvalidKeyException,
        DatabaseOperationException;
    
    public Event<Key> addEvent(int year, Term term, String slug, String title,
        String description, String facebookEventId, Venue venue, Date start, Date end)
        throws DatabaseOperationException;
    
    public Event<Key> updateEvent(Key event, int year, Term term, String slug,
        String title, String description, String facebookEventId, Venue venue,
        Date start, Date end) throws NotFoundException, InvalidKeyException,
        DatabaseOperationException;
    
    public void setTags(Key event, Set<Tag> tags) throws NotFoundException, InvalidKeyException,
        DatabaseOperationException;
}
