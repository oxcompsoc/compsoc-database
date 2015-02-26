package net.compsoc.ox.database.impl.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.compsoc.ox.database.iface.events.Tag;
import net.compsoc.ox.database.iface.events.Tags;

public class DummyTags implements Tags {
    
    private final Map<String, DummyTag> tags = new HashMap<>();
    
    {
        
        DummyTag t;
        
        t = new DummyTag();
        t.name = "Geek Night";
        t.slug = "geek_night";
        tags.put(t.slug, t);
        
        t = new DummyTag();
        t.name = "Talk";
        t.slug = "talk";
        tags.put(t.slug, t);
        
        t = new DummyTag();
        t.name = "LAN Party";
        t.slug = "lan_party";
        tags.put(t.slug, t);
        
    }
    
    private static class DummyTag implements Tag {
        
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
    public List<Tag> getTags() {
        return new ArrayList<Tag>(tags.values());
    }

    @Override
    public Tag getTagBySlug(String slug) {
        return tags.get(slug);
    }
    
}
