package net.compsoc.ox.web.admin.util;

public class RedirectException extends Exception {
    
    public final int code;
    public final String location;
    
    public RedirectException(String location){
        this(302, location);
    }
    
    public RedirectException(int code, String location){
        this.code = code;
        this.location = location;
    }
    
}
