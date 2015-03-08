package net.compsoc.ox.database.iface.events;

import net.compsoc.ox.database.iface.core.IndexedItem;

public interface Venue<Key> extends IndexedItem<Key> {
    
    public String name();
    
    public String slug();
    
}
