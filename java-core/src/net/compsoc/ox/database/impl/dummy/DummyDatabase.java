package net.compsoc.ox.database.impl.dummy;

import net.compsoc.ox.database.Database;

public class DummyDatabase extends Database {
    
    private DummyEvents events; // Lazily Instantiated

    @Override
    public DummyEvents events() {
        if(events == null)
            events = new DummyEvents(this);
        return events;
    }
    
    
}
