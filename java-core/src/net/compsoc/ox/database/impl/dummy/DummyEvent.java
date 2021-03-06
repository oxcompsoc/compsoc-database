package net.compsoc.ox.database.impl.dummy;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import net.compsoc.ox.database.iface.events.Event;
import net.compsoc.ox.database.iface.events.Tag;
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
    private final Set<String> tagSlugs;
    private Date startTimestamp;
    private Date endTimestamp;
    
    protected DummyEvent(DummyDatabase db, int key, int year, Term term, String slug){
        super(db);
        this.key = key;
        this.year = year;
        this.termSlug = term.slug();
        this.slug = slug;
        this.tagSlugs = new LinkedHashSet<>();
    }

    @Override
    public Integer key() {
        return key;
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

    public synchronized void setPrimary(int year, Term term, String slug) {
        this.year = year;
        this.termSlug = term.slug();
        this.slug = slug;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFacebookEventID(String id) {
        this.facebookEventID = id;
    }

    public synchronized void setVenue(Venue venue) {
        this.venueSlug = venue.slug();
    }

    public void setStartTimestamp(Date start) {
        this.startTimestamp = start;
    }

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

    @Override
    public Set<Tag> tags() {
        Set<Tag> tags = new LinkedHashSet<>();
        for(String tagSlug : this.tagSlugs)
            tags.add(db.events().tags().getTagBySlug(tagSlug));
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        tagSlugs.clear();
        for(Tag tag : tags)
            tagSlugs.add(tag.slug());
    }
    
    public synchronized void updateVenueSlugIfNeeded(String oldSlug, String newSlug){
        if(venueSlug.equals(oldSlug))
            venueSlug = newSlug;  
    }
    
}
