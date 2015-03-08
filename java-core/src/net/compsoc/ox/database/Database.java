package net.compsoc.ox.database;

import net.compsoc.ox.database.iface.events.Events;

public abstract class Database {
    
    public abstract Events<?, ?> events();
    
}
