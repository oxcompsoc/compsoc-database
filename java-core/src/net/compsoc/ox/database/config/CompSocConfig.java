package net.compsoc.ox.database.config;

public abstract class CompSocConfig {
    
    protected DatabaseConfig database;
    
    public DatabaseConfig database() {
        return database;
    }
    
    public static abstract class DatabaseConfig {
        
        private final String type;
        protected String host;
        protected int port = -1;
        protected String username;
        protected String password;
        protected String name;
        
        protected DatabaseConfig(String type) {
            if (type == null)
                throw new NullPointerException();
            this.type = type;
        }
        
        /**
         * @return the type of the database (never null)
         */
        public String type() {
            return type;
        }
        
        public String host() {
            return host;
        }
        
        /**
         * @return -1 if one is not specified (default) otherwise the number
         *         specified
         */
        public int port() {
            return port;
        }
        
        public String username() {
            return username;
        }
        
        public String password() {
            return password;
        }
        
        public String name() {
            return name;
        }
        
    }
    
}
