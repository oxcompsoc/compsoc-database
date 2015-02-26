package net.compsoc.ox.web.admin.templating;

public enum Template {
    HOME("home.peb"),
    
    EVENTS("events/events.peb"),
    EVENTS_EDIT("events/events_edit.peb"),
    EVENTS_VIEW_SINGLE("events/events_view_single.peb"),
    EVENTS_CONFIG("events/events_config.peb"),
    
    MEMBERS("members/members.peb");
    
    public final String path;
    
    private Template(String path){
        this.path = path;
    }
}
