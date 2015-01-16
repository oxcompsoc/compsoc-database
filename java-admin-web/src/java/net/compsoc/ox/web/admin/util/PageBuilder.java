package net.compsoc.ox.web.admin.util;

import java.io.IOException;
import java.util.HashMap;
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
    
    public final Database database;
    public final HttpServletRequest request;
    public final HttpServletResponse response;
    
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
            
            renderer.render(template, context, response.getWriter());
        } catch (PebbleException e) {
            throw new IOException(e);
        }
    }
    
}
