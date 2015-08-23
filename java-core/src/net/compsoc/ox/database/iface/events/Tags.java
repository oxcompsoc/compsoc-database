package net.compsoc.ox.database.iface.events;

import java.util.Collection;

public interface Tags {
    
    /**
     * @return the {@link Tag} or null if none exists with that slug.
     */
    public Tag getTagBySlug(String slug);
    
    public Collection<Tag> getTags();
    
}
