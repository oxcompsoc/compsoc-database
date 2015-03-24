package net.compsoc.ox.database;

import net.compsoc.ox.database.config.CompSocDatabaseConfig;
import net.compsoc.ox.database.iface.events.Events;
import net.compsoc.ox.database.impl.dummy.DummyDatabase;
import net.compsoc.ox.database.impl.sql.PostgresDatabase;
import net.compsoc.ox.database.util.exceptions.ConfigurationException;
import net.compsoc.ox.database.util.exceptions.DatabaseInitialisationException;

public abstract class Database {
    
    public abstract Events<?, ?> events();
    
    public static Database fromConfig(CompSocDatabaseConfig config)
        throws DatabaseInitialisationException, ConfigurationException {
        
        switch (config.type()) {
            case "dummy":
                return new DummyDatabase();
            case "postgres":
                return postgresDatabase(config);
            default:
                throw new ConfigurationException("Invalid Database Type: " + config.type());
                
        }
    }
    
    private static Database postgresDatabase(CompSocDatabaseConfig config)
        throws DatabaseInitialisationException {
        
        return new PostgresDatabase(config.host(), config.port(), config.name(), config.username(),
            config.password());
        
    }
    
}
