package net.compsoc.ox.database.impl.dummy;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.compsoc.ox.database.iface.core.KeyFactory;
import net.compsoc.ox.database.iface.events.Venue;
import net.compsoc.ox.database.iface.events.Venues;

public class DummyVenues implements Venues<Integer> {
    
    private static final KeyFactory<Integer> KEY_FACTORY = new KeyFactory.IntegerKeyFactory();

    private final Map<Integer, DummyVenue> venues = new LinkedHashMap<>();
    
    {
        DummyVenue v;
        
        v = new DummyVenue();
        v.key = 1;
        v.name = "Venue 1";
        v.slug = "venue_1";
        venues.put(1, v);
        
        v = new DummyVenue();
        v.key = 2;
        v.name = "Venue 2";
        v.slug = "venue_2";
        venues.put(2, v);
        
        v = new DummyVenue();
        v.key = 3;
        v.name = "Venue 3";
        v.slug = "venue_3";
        venues.put(3, v);
    }
    
    private class DummyVenue implements Venue<Integer> {
        
        private int key;
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
        public Integer key() {
            return key;
        }
        
    }

    @Override
    public List<Venue<Integer>> getVenues() {
        List<Venue<Integer>> venues = new LinkedList<>();
        venues.addAll(this.venues.values());
        return venues;
    }

    @Override
    public KeyFactory<Integer> getKeyFactory() {
        return KEY_FACTORY;
    }

    @Override
    public Venue<Integer> getVenueByKey(Integer key) {
        return venues.get(key);
    }
    
}
