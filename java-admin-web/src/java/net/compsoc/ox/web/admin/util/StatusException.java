package net.compsoc.ox.web.admin.util;

public class StatusException extends Exception {
    
    public final int code;
    
    public StatusException(int code){
        this.code = code;
    }
    
    public static StatusException do404(){
        return new StatusException(404);
    }
    
}
