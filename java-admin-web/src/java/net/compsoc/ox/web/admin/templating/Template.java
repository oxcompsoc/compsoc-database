package net.compsoc.ox.web.admin.templating;

public enum Template {
    HOME("home.peb"),
    EVENTS("events/events.peb"),
    EVENTS_ADD("events/events_edit.peb");
    
    public final String path;
    
    private Template(String path){
        this.path = path;
    }
}
