package net.compsoc.ox.database.impl.sql.events;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.compsoc.ox.database.iface.core.InvalidKeyException;
import net.compsoc.ox.database.iface.core.KeyFactory;
import net.compsoc.ox.database.iface.core.NotFoundException;
import net.compsoc.ox.database.iface.events.Event;
import net.compsoc.ox.database.iface.events.Events;
import net.compsoc.ox.database.iface.events.Tag;
import net.compsoc.ox.database.iface.events.Term;
import net.compsoc.ox.database.iface.events.Terms;
import net.compsoc.ox.database.iface.events.Venue;
import net.compsoc.ox.database.impl.dummy.DummyTerms;
import net.compsoc.ox.database.util.exceptions.DatabaseOperationException;

public class SQLEvents implements Events<Integer, String> {
    
    private static final String Q_SELECT_ALL = "SELECT * FROM web_events ORDER BY start_ts DESC";
    private static final String Q_SELECT_SINGLE = "SELECT * FROM web_events WHERE event_id = ?";
    private static final String Q_SELECT_EVENT_TAGS =
        "SELECT tag_id from web_event_tags WHERE event_id = ?";
    private static final String Q_INSERT =
        "INSERT INTO web_events (year, term, event_slug, title, description, facebook_event_id, venue, start_ts, end_ts) VALUES (?, ?::term, ?, ?, ?, ?, ?, ?, ?)";
    private static final String Q_UPDATE =
        "UPDATE web_events SET year = ?, term = ?::term, event_slug = ?, title = ?, description = ?, facebook_event_id = ?, venue = ?, start_ts = ?, end_ts = ? WHERE event_id = ?";
    
    private final PreparedStatement selectAllEvents;
    private final PreparedStatement selectSingleEvent;
    private final PreparedStatement selectEventTags;
    private final PreparedStatement insertEvent;
    private final PreparedStatement updateEvent;
    
    private Terms terms; // Lazily Instantiated
    private final SQLVenues venues;
    private final SQLTags tags;
    
    public SQLEvents(Connection connection) throws SQLException {
        this.selectAllEvents = connection.prepareStatement(Q_SELECT_ALL);
        this.selectSingleEvent = connection.prepareStatement(Q_SELECT_SINGLE);
        this.selectEventTags = connection.prepareStatement(Q_SELECT_EVENT_TAGS);
        this.insertEvent = connection.prepareStatement(Q_INSERT);
        this.updateEvent = connection.prepareStatement(Q_UPDATE);
        
        tags = new SQLTags(connection);
        venues = new SQLVenues(connection);
    }
    
    @Override
    public KeyFactory<Integer> getKeyFactory() {
        return KeyFactory.IntegerKeyFactory.SINGLETON;
    }
    
    @Override
    public Terms terms() {
        // TODO: replace this with something more substantial
        if (terms == null)
            terms = new DummyTerms();
        return terms;
    }
    
    @Override
    public SQLVenues venues() {
        return venues;
    }
    
    @Override
    public SQLTags tags() {
        return tags;
    }
    
    @Override
    public synchronized List<Event<Integer, String>> getEvents() throws DatabaseOperationException {
        List<Event<Integer, String>> list = new LinkedList<>();
        try {
            ResultSet rs = selectAllEvents.executeQuery();
            while (rs.next())
                list.add(new SQLEvent(this, rs));
        } catch (SQLException e) {
            throw new DatabaseOperationException(e);
        }
        return list;
    }
    
    @Override
    public synchronized Event<Integer, String> getEvent(Integer key) throws NotFoundException,
        InvalidKeyException, DatabaseOperationException {
        try {
            selectSingleEvent.setInt(1, key);
            ResultSet rs = selectSingleEvent.executeQuery();
            if (rs.next())
                return new SQLEvent(this, rs);
            else
                throw new NotFoundException();
        } catch (SQLException e) {
            throw new DatabaseOperationException(e);
        }
    }
    
    protected synchronized ResultSet getTagsForEvent(int eventId) throws SQLException {
        selectEventTags.setInt(1, eventId);
        return selectEventTags.executeQuery();
    }
    
    @Override
    public synchronized Event<Integer, String> addEvent(int year, Term term, String slug,
        String title, String description, String facebookEventId, Venue<String> venue, Date start,
        Date end) throws DatabaseOperationException {
        try {
            insertEvent.setInt(1, year);
            insertEvent.setString(2, term.name().toLowerCase());
            insertEvent.setString(3, slug);
            insertEvent.setString(4, title);
            insertEvent.setString(5, description);
            insertEvent.setString(6, facebookEventId);
            insertEvent.setString(7, venue.key());
            insertEvent.setDate(8, new java.sql.Date(start.getTime()));
            insertEvent.setDate(9, new java.sql.Date(end.getTime()));
            
            if(insertEvent.executeUpdate() == 0)
                throw new DatabaseOperationException("Unable to create event, zero rows affected");
            
            try (ResultSet generatedKeys = insertEvent.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return(getEvent(generatedKeys.getInt(1)));
                }
                else {
                    throw new SQLException("Creating event failed, no ID obtained.");
                }
            } catch (NotFoundException | InvalidKeyException e) {
                throw new DatabaseOperationException("Exception fetching event after creation", e);
            }
            
        } catch (SQLException e) {
            throw new DatabaseOperationException(e);
        }
        
    }
    
    @Override
    public synchronized Event<Integer, String> updateEvent(Integer event, int year, Term term,
        String slug, String title, String description, String facebookEventId, Venue<String> venue,
        Date start, Date end) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public synchronized void setTags(Integer event, Set<Tag> tags) {
        // TODO Auto-generated method stub
        
    }
    
}
