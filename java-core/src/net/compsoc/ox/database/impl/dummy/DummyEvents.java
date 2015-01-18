package net.compsoc.ox.database.impl.dummy;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.compsoc.ox.database.iface.core.KeyFactory;
import net.compsoc.ox.database.iface.core.NotFoundException;
import net.compsoc.ox.database.iface.events.Event;
import net.compsoc.ox.database.iface.events.Events;
import net.compsoc.ox.database.iface.events.Term;
import net.compsoc.ox.database.iface.events.Terms;
import net.compsoc.ox.database.iface.events.Venues;

public class DummyEvents extends DummyDatabaseObject implements Events<Integer> {
    
    private static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    
    private Map<Integer, Event<Integer>> events = new LinkedHashMap<>();
    private int nextKey = 0;
    
    private DummyVenues venues; // Lazily Instantiated
    private DummyTerms terms; // Lazily Instantiated
    
    protected DummyEvents(DummyDatabase db) {
        super(db);
    }
    
    {
        try {
            // Initialise Dummy Data
            Event<?> e;
            
            e = addEvent(2015, terms().getTermBySlug("hilary"), "geek_night_0");
            e.setTitle("Some Event");
            e.setDescription("This is a desctiption");
            e.setFacebookEventID("1234");
            e.setStartTimestamp(FORMAT.parse("2015-01-19"));
            e.setEndTimestamp(new Date());
            
            e = addEvent(2015, terms().getTermBySlug("hilary"), "geek_night_0");
            e.setTitle("Some Event");
            e.setDescription("This is a desctiption");
            e.setFacebookEventID("1234");
            e.setStartTimestamp(FORMAT.parse("2015-01-17"));
            e.setEndTimestamp(new Date());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    public List<Event<Integer>> getEvents() {
        return new ArrayList<>(events.values());
    }
    
    @Override
    public Venues venues() {
        if (venues == null)
            venues = new DummyVenues();
        return venues;
    }
    
    @Override
    public synchronized DummyEvent addEvent(int year, Term term, String slug) {
        int k = nextKey++;
        DummyEvent e = new DummyEvent(db, k, year, term, slug);
        events.put(k, e);
        return e;
    }
    
    @Override
    public Terms terms() {
        if (terms == null)
            terms = new DummyTerms();
        return terms;
    }
    
    @Override
    public void getEvent(Integer key) throws NotFoundException {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public KeyFactory<Integer> eventKeyFactory() {
        return IntegerKeyFactory.SINGLETON;
    }
    
}
