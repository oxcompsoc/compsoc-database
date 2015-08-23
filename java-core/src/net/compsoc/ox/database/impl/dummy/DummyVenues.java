package net.compsoc.ox.database.impl.dummy;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import net.compsoc.ox.database.iface.events.Venue;
import net.compsoc.ox.database.iface.events.Venues;
import net.compsoc.ox.database.util.exceptions.DatabaseOperationException;

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

        @Override
        public void setSlugAndName(String slug, String name) {
            this.slug = slug;
            this.name = name;
        }
        
    }

    @Override
    public Collection<DummyVenue> getVenues() {
        return Collections.unmodifiableCollection(this.venues.values());
    }

    @Override
    public Venue getVenueBySlug(String slug) {
        return venues.get(slug);
    }

    @Override
    public void addVenue(String slug, String name) throws DatabaseOperationException {
        if(venues.containsKey(slug)){
            throw new DatabaseOperationException("A venue with that slug already exists");
        }
        DummyVenue v = new DummyVenue();
        v.name = name;
        v.slug = slug;
        venues.put(slug, v);
    }
    
}
