package net.compsoc.ox.database.impl.dummy;

public abstract class DummyDatabaseObject {
    
    protected final DummyDatabase db;
    
    protected DummyDatabaseObject(DummyDatabase db){
        this.db = db;
    }
    
}
