package net.compsoc.ox.database.impl.dummy;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.compsoc.ox.database.iface.events.Venue;
import net.compsoc.ox.database.iface.events.Venues;

public class DummyVenues implements Venues {
    
    private final Map<String, DummyVenue> venues = new LinkedHashMap<>();
    
    {
        DummyVenue v;
        
        v = new DummyVenue();
        v.name = "Venue 1";
        v.slug = "venue_1";
        venues.put(v.slug, v);
        
        v = new DummyVenue();
        v.name = "Venue 2";
        v.slug = "venue_2";
        venues.put(v.slug, v);
        
        v = new DummyVenue();
        v.name = "Venue 3";
        v.slug = "venue_3";
        venues.put(v.slug, v);
    }
    
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

    @Override
    public List<Venue> getVenues() {
        List<Venue> venues = new LinkedList<>();
        venues.addAll(this.venues.values());
        return venues;
    }
    
}
