package net.compsoc.ox.database.impl.dummy;

import java.util.Date;

import net.compsoc.ox.database.iface.events.Event;
import net.compsoc.ox.database.iface.events.Term;
import net.compsoc.ox.database.iface.events.Venue;
import net.compsoc.ox.database.util.TermDatesUtil;

public class DummyEvent extends DummyDatabaseObject implements Event<Integer> {
    
    private final int key;
    
    private int year;
    private String termSlug;
    private String slug;
    private String title;
    private String description;
    private String facebookEventID;
    private String venueSlug;
    private Date startTimestamp;
    private Date endTimestamp;
    
    protected DummyEvent(DummyDatabase db, int key, int year, Term term, String slug){
        super(db);
        this.key = key;
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

    @Override
    public Integer key() {
        return key;
    }

    @Override
    public String keyString() {
        return Integer.toString(key);
    }

    @Override
    public synchronized void setPrimary(int year, Term term, String slug) {
        this.year = year;
        this.termSlug = term.slug();
        this.slug = slug;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setFacebookEventID(String id) {
        this.facebookEventID = id;
    }

    @Override
    public void setVenue(Venue venue) {
        this.venueSlug = venue.slug();
    }

    @Override
    public void setStartTimestamp(Date start) {
        this.startTimestamp = start;
    }

    @Override
    public void setEndTimestamp(Date end) {
        this.endTimestamp = end;
    }

    @Override
    public Integer getTermWeek() {
        if(startTimestamp == null)
            return null;
        Term t = db.events().terms().getTermBySlug(termSlug);
        Date monWK1 = db.events().terms().termDates().getWeek1Date(year, t);
        return TermDatesUtil.getTermWeek(monWK1, startTimestamp);
    }
    
}
