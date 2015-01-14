package net.compsoc.ox.web.admin.templating;

public enum Template {
    HOME("home.peb");
    public final String path;
    
    private Template(String path){
        this.path = path;
    }
}
