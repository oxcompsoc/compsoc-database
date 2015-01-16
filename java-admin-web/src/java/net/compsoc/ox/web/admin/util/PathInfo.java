package net.compsoc.ox.web.admin.util;

import javax.servlet.http.HttpServletRequest;

public class PathInfo {
    
    private final String[] slugs;
    private final int index;
    
    public PathInfo(HttpServletRequest request) {
        String path = request.getPathInfo();
        
        if (path.equals("/")) {
            slugs = new String[] { "" };
        } else {
            slugs = (path.endsWith("/") ? path.substring(0, path.length() - 1) : path).split("/");
        }
        
        index = 0;
    }
    
    private PathInfo(String[] slugs, int index){
        this.slugs = slugs;
        this.index = index;
    }
    
    public String slug(){
        return slugs[index];
    }
    
    public PathInfo next() {
        if(index + 1 < slugs.length)
            return new PathInfo(slugs, index + 1);
        return null;
    }
    
}
