package net.compsoc.ox.database.iface.events;

import java.util.List;

public interface Tags {
    
    public Tag getTagBySlug(String slug);
    
    public List<Tag> getTags();
    
}
