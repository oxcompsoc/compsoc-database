package net.compsoc.ox.database.iface.events;

import java.util.List;

import net.compsoc.ox.database.iface.core.InvalidKeyException;
import net.compsoc.ox.database.iface.core.NotFoundException;

public interface Events{
    
    public Terms terms();
    
    public Venues venues();
    
    public List<Event> getEvents();
    
    public Event getEvent(String key) throws NotFoundException, InvalidKeyException;
    
    public Event addEvent(int year, Term term, String slug);
    
}
