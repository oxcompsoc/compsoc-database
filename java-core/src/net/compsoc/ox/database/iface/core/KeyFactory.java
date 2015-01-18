package net.compsoc.ox.database.iface.core;

public interface KeyFactory<KeyType> {
    
    public KeyType fromString(String keyString) throws InvalidKeyException;
    public String toString(KeyType key);
    
    public static class InvalidKeyException extends Exception {
        
    }
    
}
