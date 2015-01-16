package net.compsoc.ox.web.admin.sections;

public enum MainSectionsEnum {
    HOME("Home", "/"),
    EVENTS("Events", "/events/"),
    MEMBERS("Members", "/members/");
    
    public final String label;
    public final String path;
    
    private MainSectionsEnum(String label, String path){
        this.label = label;
        this.path = path;
    }
}
