package net.compsoc.ox.database.iface.events;

import java.util.List;

import net.compsoc.ox.database.iface.core.KeyFactory;
import net.compsoc.ox.database.iface.core.NotFoundException;

public interface Events<EKey> {
    
    public Terms terms();
    
    public Venues venues();
    
    public List<Event<EKey>> getEvents();
    
    public void getEvent(EKey key) throws NotFoundException;
    
    public Event<EKey> addEvent(int year, Term term, String slug);
    
    public KeyFactory<EKey> eventKeyFactory();
    
}
