package net.compsoc.ox.web.admin.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mitchellbosecke.pebble.error.PebbleException;

import net.compsoc.ox.web.admin.templating.Template;

public class PageBuilder {
    
    public final HttpServletRequest request;
    public final HttpServletResponse response;
    private final PageRenderer renderer;
    private final Map<String, Object> context = new HashMap<>();
    
    public PageBuilder(PageRenderer renderer, HttpServletRequest request,
        HttpServletResponse response) {
        this.renderer = renderer;
        this.request = request;
        this.response = response;
    }
    
    public void render(Template template) throws IOException {
        try {
            response.setContentType("text/html");
            renderer.render(template, context, response.getWriter());
        } catch (PebbleException e) {
            throw new IOException(e);
        }
    }
    
}
