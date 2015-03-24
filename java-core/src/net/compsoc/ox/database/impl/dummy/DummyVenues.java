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
    private int nextKey = 1;
    
    {
        DummyVenue v;
        
        v = new DummyVenue();
        v.key = nextKey++;
        v.name = "Venue 1";
        v.slug = "venue_1";
        venues.put(v.key, v);
        
        v = new DummyVenue();
        v.key = nextKey++;
        v.name = "Venue 2";
        v.slug = "venue_2";
        venues.put(v.key, v);
        
        v = new DummyVenue();
        v.key = nextKey++;
        v.name = "Venue 3";
        v.slug = "venue_3";
        venues.put(v.key, v);
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

        @Override
        public void setSlugAndName(String slug, String name) {
            this.slug = slug;
            this.name = name;
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

    @Override
    public void addVenue(String slug, String name) {
        DummyVenue v = new DummyVenue();
        v.key = nextKey++;
        v.name = name;
        v.slug = slug;
        venues.put(v.key, v);
    }
    
}
