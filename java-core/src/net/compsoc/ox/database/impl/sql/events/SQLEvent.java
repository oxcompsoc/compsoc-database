package net.compsoc.ox.database.impl.sql.events;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import net.compsoc.ox.database.iface.events.Event;
import net.compsoc.ox.database.iface.events.Tag;
import net.compsoc.ox.database.iface.events.Term;
import net.compsoc.ox.database.iface.events.Venue;
import net.compsoc.ox.database.util.TermDatesUtil;
import net.compsoc.ox.database.util.exceptions.DatabaseOperationException;

public class SQLEvent implements Event<Integer, String> {

    private final SQLEvents events;
    
    private final int key;
    
    private final int year;
    private final String termSlug;
    private final String slug;
    private final String title;
    private final String description;
    private final String facebookEventID;
    private final String venueKey;
    private final Date startTimestamp;
    private final Date endTimestamp;
    
    public SQLEvent(SQLEvents events, ResultSet currentSet)
        throws SQLException {
        this.events = events;
        
        this.key = currentSet.getInt("event_id");
        this.year = currentSet.getInt("year");
        this.termSlug = currentSet.getString("term");
        this.slug = currentSet.getString("event_slug");
        this.title = currentSet.getString("title");
        this.description = currentSet.getString("description");
        this.facebookEventID = currentSet.getString("facebook_event_id");
        this.venueKey = currentSet.getString("venue");
        this.startTimestamp = currentSet.getDate("start_ts");
        this.endTimestamp = currentSet.getDate("end_ts");
    }

    @Override
    public Integer key() {
        return Integer.valueOf(key);
    }

    @Override
    public int year() {
        return year;
    }

    @Override
    public Term term() {
        return termSlug == null ? null : events.terms().getTermBySlug(termSlug);
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
    public Venue<String> venue() {
        return venueKey == null ? null : events.venues().getVenueByKey(venueKey);
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
    public Set<Tag> tags() throws DatabaseOperationException {
        Set<Tag> tags = new LinkedHashSet<>();
        try {
            ResultSet rs = events.getTagsForEvent(key);
            while (rs.next()){
                Tag tag = events.tags().getTagByID(rs.getInt("tag_id"));
                if(tag != null)
                    tags.add(tag);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException(e);
        }
        return tags;
    }

    @Override
    public synchronized Integer getTermWeek() {
        if(startTimestamp == null)
            return null;
        Term t = events.terms().getTermBySlug(termSlug);
        Date monWK1 = events.terms().termDates().getWeek1Date(year, t);
        return TermDatesUtil.getTermWeek(monWK1, startTimestamp);
    }
    
}
