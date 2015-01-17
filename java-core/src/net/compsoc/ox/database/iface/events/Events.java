package net.compsoc.ox.database.iface.events;

import java.util.List;

public interface Events {
    
    public Terms terms();
    
    public Venues venues();
    
    public List<Event> getEvents();
    
    public Event addEvent(int year, Term term, String slug);
    
}
