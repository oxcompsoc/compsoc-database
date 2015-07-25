package net.compsoc.ox.web.admin.sections.events;

import java.io.IOException;
import java.util.List;

import net.compsoc.ox.database.iface.events.Events;
import net.compsoc.ox.database.util.exceptions.DatabaseOperationException;
import net.compsoc.ox.web.admin.sections.MainSectionsEnum;
import net.compsoc.ox.web.admin.sections.Section;
import net.compsoc.ox.web.admin.sections.events.config.EventsConfigSection;
import net.compsoc.ox.web.admin.templating.Template;
import net.compsoc.ox.web.admin.util.PageBuilder;
import net.compsoc.ox.web.admin.util.PathInfo;
import net.compsoc.ox.web.admin.util.StatusException;
import net.compsoc.ox.web.admin.util.database.WrappedIndexedItem;

public class EventsSection extends Section {
    
    private final Section addSection = new AddEventSection();
    private final Section viewSection = new ViewEventsSection();
    private final Section editSection = new EditEventSection();
    private final Section configSection = new EventsConfigSection();
    
    @Override
    public void visitSection(PathInfo info, PageBuilder builder) throws StatusException {
        builder.setActivePage(MainSectionsEnum.EVENTS);
        builder.addBreadcrumb(MainSectionsEnum.EVENTS.label, MainSectionsEnum.EVENTS.path);
    }
    
    @Override
    public void renderPage(PathInfo info, PageBuilder builder) throws IOException, StatusException {
        renderPage(info, builder, builder.database.events());
    }
    
    public <EKey> void renderPage(PathInfo info, PageBuilder builder, Events<EKey, ?> events)
        throws IOException, StatusException {
        builder.put("title", MainSectionsEnum.EVENTS.label);
        
        try {
            List<?> eventsList = WrappedIndexedItem.wrappedIndexedItemList(events.getKeyFactory(), events.getEvents());
            builder.put("events", eventsList);
        } catch (DatabaseOperationException e) {
            builder.errors().add("Unable to fetch events");
        }
        
        builder.render(Template.EVENTS);
    }
    
    @Override
    public Section getSubsection(String slug) {
        switch (slug) {
            case "add":
                return addSection;
            case "view":
                return viewSection;
            case "edit":
                return editSection;
            case "config":
                return configSection;
        }
        return null;
    }
    
}
