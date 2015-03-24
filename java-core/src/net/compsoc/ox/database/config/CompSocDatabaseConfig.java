package net.compsoc.ox.database.config;

public interface CompSocDatabaseConfig {
    
    /**
     * @return the type of the database (never null)
     */
    public String type();
    
    public String host();
    
    /**
     * @return -1 if one is not specified (default) otherwise the number
     *         specified
     */
    public int port();
    
    public String username();
    
    public String password();
    
    public String name();
}
