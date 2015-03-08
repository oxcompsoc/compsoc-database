package net.compsoc.ox.web.admin.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mitchellbosecke.pebble.error.PebbleException;

import net.compsoc.ox.database.Database;
import net.compsoc.ox.web.admin.Constants;
import net.compsoc.ox.web.admin.sections.MainSectionsEnum;
import net.compsoc.ox.web.admin.templating.Template;

public class PageBuilder {
    
    private final PageRenderer renderer;
    private final Map<String, Object> context = new HashMap<>();
    private final List<Breadcrumb> breadcrumbs = new LinkedList<>();
    private List<String> messages;
    private List<String> errors;
    
    public final Database database;
    public final HttpServletRequest request;
    public final HttpServletResponse response;
    
    private String overriddenNonce;
    
    public PageBuilder(PageRenderer renderer, Database database, HttpServletRequest request,
        HttpServletResponse response) {
        this.renderer = renderer;
        this.database = database;
        this.request = request;
        this.response = response;
    }
    
    public void put(String key, Object value) {
        context.put(key, value);
    }
    
    public void addBreadcrumb(String label, String path){
        breadcrumbs.add(new Breadcrumb(label, path));
    }
    
    public void setActivePage(MainSectionsEnum section) {
        put("active_page", section);
    }
    
    public void render(Template template) throws IOException {
        try {
            response.setContentType("text/html");
            
            put("site_title", Constants.SITE_TITLE);
            put("static_href", request.getContextPath() + "/static");
            put("admin_root", request.getContextPath());
            put("main_menu_items", MainSectionsEnum.values());
            put("breadcrumbs", breadcrumbs);
            
            renderer.render(template, context, response.getWriter());
        } catch (PebbleException e) {
            throw new IOException(e);
        }
    }
    
    public List<String> messages() {
        if (messages == null) {
            messages = new LinkedList<>();
            put("messages", messages);
        }
        return messages;
    }
    
    public List<String> errors() {
        if (errors == null) {
            errors = new LinkedList<>();
            put("errors", errors);
        }
        return errors;
    }
    
    protected void overrideNonce(String newNonce){
        overriddenNonce = newNonce;
    }
    
    protected String getOverriddenNonce(){
        return overriddenNonce;
    }

    @SuppressWarnings("unused")
    private class Breadcrumb {
        
        public final String label;
        public final String path;
        
        public Breadcrumb(String label, String path){
            this.label = label;
            this.path = path;
        }
        
        public boolean active(){
            return breadcrumbs.get(breadcrumbs.size() - 1) == this;
        }
        
    }
    
}
