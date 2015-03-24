package net.compsoc.ox.web.admin.config;

import java.io.InputStream;
import java.util.Map;

import net.compsoc.ox.database.util.exceptions.ConfigurationException;
import net.compsoc.ox.web.admin.config.CompSocAdminConfig.DatabaseConfig;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

public class CompSocYAMLAdminConfig {
    
    public static CompSocAdminConfig load(InputStream is) throws ConfigurationException {
        Yaml yaml = new Yaml();
        
        try {
            Object data = yaml.load(is);
            if (data == null) {
                throw new ConfigurationException("Configuration Empty");
            }
            CompSocAdminConfig config = new CompSocAdminConfig();
            setupConfig(config, prepareMap(data, "yaml file"));
            return config;
        } catch (YAMLException e) {
            throw new ConfigurationException("Unable to load configuration file: " + e.getMessage());
        }
        
    }
    
    private static void setupConfig(CompSocAdminConfig config, Map<String, Object> data)
        throws ConfigurationException {
        
        Object dbConfig = data.get("database");
        if (dbConfig != null)
            setupDatabase(config, prepareMap(dbConfig, "database"));
        
        config.setPort(getValue(data, "port", Integer.class, "port"));
        config.setRootPath(getValue(data, "root_path", String.class, "root_path"));
        
    }
    
    private static void setupDatabase(CompSocAdminConfig config, Map<String, Object> data)
        throws ConfigurationException {
        String type = getValue(data, "type", String.class, "database type");
        if (type == null) {
            throw new ConfigurationException("database needs a type");
        }
        DatabaseConfig database = new DatabaseConfig(type);
        config.setDatabase(database);
        database.setHost(getValue(data, "host", String.class, "database host"));
        database.setPort(getValue(data, "port", Integer.class, "database port"));
        database.setUsername(getValue(data, "user", String.class, "database user"));
        database.setPassword(getValue(data, "pass", String.class, "database pass"));
        database.setName(getValue(data, "name", String.class, "database name"));
        
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
