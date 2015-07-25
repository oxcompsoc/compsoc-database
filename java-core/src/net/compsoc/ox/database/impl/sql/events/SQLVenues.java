package net.compsoc.ox.database.impl.sql.events;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.compsoc.ox.database.iface.core.KeyFactory;
import net.compsoc.ox.database.iface.events.Venue;
import net.compsoc.ox.database.iface.events.Venues;

public class SQLVenues implements Venues<String> {
    
    private static final String Q_SELECT_ALL = "SELECT * FROM web_event_venues ORDER BY venue_name ASC";
    
    private final Connection connection;
    private final PreparedStatement selectAllVenues;
    
    private Map<String, Venue<String>> cache;
    
    public SQLVenues(Connection connection) throws SQLException {
        this.connection = connection;
        this.selectAllVenues = connection.prepareStatement(Q_SELECT_ALL);
    }

    @Override
    public KeyFactory<String> getKeyFactory() {
        return KeyFactory.StringKeyFactory.SINGLETON;
    }

    @Override
    public synchronized Venue<String> getVenueByKey(String key) {
        fetchVenuesIfNeeded();
        return cache.get(key);
    }

    @Override
    public synchronized List<Venue<String>> getVenues() {
        fetchVenuesIfNeeded();
        return new ArrayList<>(cache.values());
    }
    
    private synchronized void fetchVenuesIfNeeded(){
        if(cache == null){
            cache = new LinkedHashMap<>();
            try {
                ResultSet rs = selectAllVenues.executeQuery();
                while (rs.next()){
                    SQLVenue venue = new SQLVenue(connection, rs);
                    cache.put(venue.key(), venue);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public synchronized void addVenue(String slug, String name) {
        // TODO Auto-generated method stub
        
    }
    
}
