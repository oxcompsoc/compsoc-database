package net.compsoc.ox.database.config;

import java.io.InputStream;
import java.util.Map;

import net.compsoc.ox.database.util.exceptions.ConfigurationException;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

public class CompSocYAMLConfig extends CompSocConfig {
    
    public CompSocYAMLConfig(InputStream is) throws ConfigurationException {
        Yaml yaml = new Yaml();
        
        try {
            Object config = yaml.load(is);
            setupConfig(prepareMap(config, "yaml file"));
        } catch (YAMLException e) {
            throw new ConfigurationException("Unable to load configuration file: " + e.getMessage());
        }
        
    }
    
    private void setupConfig(Map<String, Object> config) throws ConfigurationException {
        Object dbConfig = config.get("database");
        if (dbConfig != null)
            setupDatabase(prepareMap(dbConfig, "database"));
        
    }
    
    private void setupDatabase(Map<String, Object> config) throws ConfigurationException {
        String type = getValue(config, "type", String.class, "database type");
        if (type == null) {
            throw new ConfigurationException("database needs a type");
        }
        this.database = new DatabaseConfig(type) {
        };
        this.database.host = getValue(config, "host", String.class, "database host");
        Integer port = getValue(config, "port", Integer.class, "database port");
        this.database.port = port == null ? -1 : port;
        this.database.username = getValue(config, "user", String.class, "database user");
        this.database.password = getValue(config, "pass", String.class, "database pass");
        this.database.name = getValue(config, "name", String.class, "database name");
    }
    
    // Helper Methods
    
    @SuppressWarnings("unchecked")
    private static Map<String, Object> prepareMap(Object object, String name)
        throws ConfigurationException {
        try {
            return (Map<String, Object>) object;
        } catch (ClassCastException e) {
            throw new ConfigurationException(name
                + " is not a set of key,value pairs (YAML Associated Array)");
        }
    }
    
    private static <T> T getValue(Map<String, Object> map, String key, Class<T> cls, String name)
        throws ConfigurationException {
        try {
            return cls.cast(map.get(key));
        } catch (ClassCastException e) {
            throw new ConfigurationException("Invalid type for " + name);
        }
    }
    
}
