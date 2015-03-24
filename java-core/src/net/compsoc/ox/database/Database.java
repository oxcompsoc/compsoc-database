package net.compsoc.ox.database;

import net.compsoc.ox.database.config.CompSocConfig;
import net.compsoc.ox.database.iface.events.Events;
import net.compsoc.ox.database.impl.dummy.DummyDatabase;
import net.compsoc.ox.database.impl.sql.PostgresDatabase;
import net.compsoc.ox.database.util.exceptions.ConfigurationException;
import net.compsoc.ox.database.util.exceptions.DatabaseInitialisationException;

public abstract class Database {
    
    public abstract Events<?, ?> events();
    
    public static Database fromConfig(CompSocConfig config) throws DatabaseInitialisationException,
        ConfigurationException {
        CompSocConfig.DatabaseConfig dbConf = config.database();
        if (dbConf == null) {
            throw new ConfigurationException("Configuration does not include database information");
        }
        
        switch (dbConf.type()) {
            case "dummy":
                return new DummyDatabase();
            case "postgres":
                return postgresDatabase(dbConf);
            default:
                throw new ConfigurationException("Invalid Database Type: " + dbConf.type());
                
        }
    }
    
    private static Database postgresDatabase(CompSocConfig.DatabaseConfig dbConf)
        throws DatabaseInitialisationException {
        
        return new PostgresDatabase(dbConf.host(), dbConf.port(), dbConf.name(), dbConf.username(),
            dbConf.password());
        
    }
    
}
