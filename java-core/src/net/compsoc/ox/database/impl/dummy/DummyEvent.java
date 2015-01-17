package net.compsoc.ox.database.impl.dummy;

import java.util.Date;

import net.compsoc.ox.database.iface.events.Event;
import net.compsoc.ox.database.iface.events.Term;
import net.compsoc.ox.database.iface.events.Venue;

public class DummyEvent extends DummyDatabaseObject implements Event {
    
    private int year;
    private String termSlug;
    private String slug;
    private String title;
    private String description;
    private String facebookEventID;
    private String venueSlug;
    private Date startTimestamp;
    private Date endTimestamp;
    
    protected DummyEvent(DummyDatabase db, int year, Term term, String slug){
        super(db);
        this.year = year;
        this.termSlug = term.slug();
        this.slug = slug;
    }

    @Override
    public int year() {
        return year;
    }

    @Override
    public Term term() {
        return termSlug == null ? null : db.events().terms().getTermBySlug(termSlug);
    }

    @Override
    public String slug() {
        return slug;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public String facebookEventID() {
        return facebookEventID;
    }

    @Override
    public Venue venue() {
        return venueSlug == null ? null : db.events().venues().getVenueBySlug(venueSlug);
    }

    @Override
    public Date startTimestamp() {
        return startTimestamp;
    }

    @Override
    public Date endTimestamp() {
        return endTimestamp;
    }
    
}
