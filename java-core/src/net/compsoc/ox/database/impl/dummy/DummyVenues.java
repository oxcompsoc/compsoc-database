package net.compsoc.ox.database.impl.dummy;

import java.util.HashMap;
import java.util.Map;

import net.compsoc.ox.database.iface.events.Venue;
import net.compsoc.ox.database.iface.events.Venues;

public class DummyVenues implements Venues {
    
    private final Map<String, DummyVenue> venues = new HashMap<>();
    
    @Override
    public DummyVenue getVenueBySlug(String slug){
        return venues.get(slug);
    }
    
    private class DummyVenue implements Venue {
        
        private String name;
        private String slug;

        @Override
        public String name() {
            return name;
        }

        @Override
        public String slug() {
            return slug;
        }
        
    }
    
}
