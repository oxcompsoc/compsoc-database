package net.compsoc.ox.web.admin.util;

import java.io.IOException;
import java.io.Writer;
import java.util.EnumMap;
import java.util.Map;

import net.compsoc.ox.web.admin.templating.Template;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

public class PageRenderer {
    
    private final PebbleEngine engine;
    private final Map<Template, PebbleTemplate> templates = new EnumMap<>(Template.class);
    
    public PageRenderer() {
        engine = new PebbleEngine();
        
        engine.addExtension(new PebbleExtension());
        
        engine.getLoader().setPrefix("resources/templates/");
        
        // Load all templates
        System.out.println("Loading Templates");
        
        for (Template template : Template.values()) {
            System.out.println("Loading Template: " + template.path);
            try {
                templates.put(template, engine.getTemplate(template.path));
            } catch (PebbleException e) {
                System.out.println("Unable to load template: " + e.getMessage());
            }
        }
    }
    
    public void render(Template template, Map<String, Object> context, Writer writer)
        throws PebbleException, IOException {
        templates.get(template).evaluate(writer, context);
    }
    
}
