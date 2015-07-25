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

import net.compsoc.ox.database.iface.core.InvalidKeyException;
import net.compsoc.ox.database.iface.core.KeyFactory;
import net.compsoc.ox.database.iface.core.NotFoundException;
import net.compsoc.ox.database.iface.events.Event;
import net.compsoc.ox.database.iface.events.Events;
import net.compsoc.ox.database.iface.events.Tags;
import net.compsoc.ox.database.iface.events.Term;
import net.compsoc.ox.database.iface.events.Terms;
import net.compsoc.ox.database.iface.events.Venues;

public class DummyEvents extends DummyDatabaseObject implements Events<Integer, Integer> {
    
    private static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    
    private Map<Integer, Event<Integer, Integer>> events = new LinkedHashMap<>();
    private int nextKey = 0;
    
    private DummyVenues venues; // Lazily Instantiated
    private DummyTerms terms; // Lazily Instantiated
    private DummyTags tags; // Lazily Instantiated
    
    protected DummyEvents(DummyDatabase db) {
        super(db);
    }
    
    {
        try {
            // Initialise Dummy Data
            DummyEvent e;
            
            e = addEvent(2015, terms().getTermBySlug("hilary"), "geek_night_0");
            e.setTitle("Some Event");
            e.setDescription("This is a description");
            e.setFacebookEventID("1234");
            e.setStartTimestamp(FORMAT.parse("2015-01-19"));
            e.setEndTimestamp(new Date());
            e.setVenue(venues().getVenueByKey(1));
            
            e = addEvent(2014, terms().getTermBySlug("trinity"), "geek_night_0");
            e.setTitle("Some Event");
            e.setDescription("This is a description\nthat\ngoes\nover\nmultiple\nlines.");
            e.setFacebookEventID("1234");
            e.setStartTimestamp(FORMAT.parse("2014-06-14"));
            e.setEndTimestamp(FORMAT.parse("2014-06-14"));
            e.setVenue(venues().getVenueByKey(2));
            
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
    }
    
    public List<Event<Integer, Integer>> getEvents() {
        return new ArrayList<>(events.values());
    }
    
    @Override
    public Venues<Integer> venues() {
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
    public Event<Integer, Integer> getEvent(Integer key) throws NotFoundException, InvalidKeyException {
        if(key == null)
            throw new InvalidKeyException();
        Event<Integer, Integer> e = events.get(key);
        if(e == null)
            throw new NotFoundException();
        else
            return e;
    }

    @Override
    public Tags tags() {
        if (tags == null)
            tags = new DummyTags();
        return tags;
    }

    @Override
    public KeyFactory<Integer> getKeyFactory() {
        return KeyFactory.IntegerKeyFactory.SINGLETON;
    }
    
}
