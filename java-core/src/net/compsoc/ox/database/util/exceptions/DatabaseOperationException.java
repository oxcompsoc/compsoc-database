package net.compsoc.ox.database.util.exceptions;

public class DatabaseOperationException extends Exception {
    
    public DatabaseOperationException(Throwable throwable) {
        super(throwable);
    }
    
    public DatabaseOperationException(String message) {
        super(message);
    }

    public DatabaseOperationException(String string, Exception e) {
        super(string, e);
    }
}
