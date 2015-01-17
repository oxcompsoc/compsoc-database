package net.compsoc.ox.database.impl.dummy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.compsoc.ox.database.iface.events.Event;
import net.compsoc.ox.database.iface.events.Events;
import net.compsoc.ox.database.iface.events.Term;
import net.compsoc.ox.database.iface.events.Terms;
import net.compsoc.ox.database.iface.events.Venues;

public class DummyEvents extends DummyDatabaseObject implements Events {

    private List<Event> events = new ArrayList<>();
    
    private DummyVenues venues; // Lazily Instantiated
    private DummyTerms terms; // Lazily Instantiated
    
    protected DummyEvents(DummyDatabase db) {
        super(db);
    }
    
    {
        // Initialise Dummy Data
        events.add(new DummyEvent(db, 2014, terms().getTermBySlug("michaelmas"), "something"));
        events.add(new DummyEvent(db, 2015, terms().getTermBySlug("michaelmas"), "something_else"));
        
    }

    public List<Event> getEvents() {
        return Collections.unmodifiableList(events);
    }

    @Override
    public Venues venues() {
        if(venues == null)
            venues = new DummyVenues();
        return venues;
    }

    @Override
    public DummyEvent addEvent(int year, Term term, String slug) {
        DummyEvent e = new DummyEvent(db, year, term, slug);
        events.add(e);
        return e;
    }

    @Override
    public Terms terms() {
        if(terms == null)
            terms = new DummyTerms();
        return terms;
    }
    
}
