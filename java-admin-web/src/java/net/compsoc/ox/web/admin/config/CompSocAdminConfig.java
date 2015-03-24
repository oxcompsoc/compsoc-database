package net.compsoc.ox.web.admin.config;

import net.compsoc.ox.database.config.CompSocDatabaseConfig;

public final class CompSocAdminConfig {
    
    protected DatabaseConfig database;
    
    // Getters
    
    public DatabaseConfig database() {
        return database;
    }
    
    // Setters
    
    protected void setDatabase(DatabaseConfig database){
        this.database = database;
    }
    
    public static final class DatabaseConfig implements CompSocDatabaseConfig {
        
        private final String type;
        private String host;
        private int port = -1;
        private String username;
        private String password;
        private String name;
        
        protected DatabaseConfig(String type) {
            if (type == null)
                throw new NullPointerException();
            this.type = type;
        }
        
        // Getters
        
        @Override
        public String type() {
            return type;
        }

        @Override
        public String host() {
            return host;
        }

        @Override
        public int port() {
            return port;
        }

        @Override
        public String username() {
            return username;
        }

        @Override
        public String password() {
            return password;
        }

        @Override
        public String name() {
            return name;
        }
        
        // Setters
        
        protected void setHost(String host){
            this.host = host;
        }
        
        protected void setPort(Integer port){
            this.port = port == null ? -1 : port;
        }
        
        protected void setUsername(String username){
            this.username = username;
        }
        
        protected void setPassword(String password){
            this.password = password;
        }
        
        protected void setName(String name){
            this.name = name;
        }
        
    }
    
}
