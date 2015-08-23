package net.compsoc.ox.database.iface.events;

public interface Venue {
    
    public String name();
    
    public String slug();
    
    public void setSlugAndName(String slug, String name);
}
