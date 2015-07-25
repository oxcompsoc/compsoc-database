package net.compsoc.ox.database.impl.sql.events;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import net.compsoc.ox.database.iface.core.InvalidKeyException;
import net.compsoc.ox.database.iface.core.KeyFactory;
import net.compsoc.ox.database.iface.core.NotFoundException;
import net.compsoc.ox.database.iface.events.Event;
import net.compsoc.ox.database.iface.events.Events;
import net.compsoc.ox.database.iface.events.Term;
import net.compsoc.ox.database.iface.events.Terms;
import net.compsoc.ox.database.impl.dummy.DummyTerms;
import net.compsoc.ox.database.util.exceptions.DatabaseOperationException;

public class SQLEvents implements Events<Integer, String> {
    
    private static final String Q_SELECT_ALL = "SELECT * FROM web_events ORDER BY start_ts DESC";
    private static final String Q_SELECT_SINGLE = "SELECT * FROM web_events WHERE event_id = ?";
    private static final String Q_SELECT_EVENT_TAGS =
        "SELECT tag_id from web_event_tags WHERE event_id = ?";
    
    private final Connection connection;
    private final PreparedStatement selectAllEvents;
    private final PreparedStatement selectSingleEvent;
    private final PreparedStatement selectEventTags;
    
    private Terms terms; // Lazily Instantiated
    private final SQLVenues venues;
    private final SQLTags tags;
    
    public SQLEvents(Connection connection) throws SQLException {
        this.connection = connection;
        this.selectAllEvents = connection.prepareStatement(Q_SELECT_ALL);
        this.selectSingleEvent = connection.prepareStatement(Q_SELECT_SINGLE);
        this.selectEventTags = connection.prepareStatement(Q_SELECT_EVENT_TAGS);
        
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
                list.add(new SQLEvent(this, connection, rs));
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
                return new SQLEvent(this, connection, rs);
            else
                throw new NotFoundException();
        } catch (SQLException e) {
            throw new DatabaseOperationException(e);
        }
    }
    
    @Override
    public synchronized Event<Integer, String> addEvent(int year, Term term, String slug) {
        // TODO: implement
        return null;
    }
    
    protected synchronized ResultSet getTagsForEvent(int eventId) throws SQLException {
        selectEventTags.setInt(1, eventId);
        return selectEventTags.executeQuery();
    }
    
}
