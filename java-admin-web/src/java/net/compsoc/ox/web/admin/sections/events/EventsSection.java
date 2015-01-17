package net.compsoc.ox.web.admin.sections.events;

import java.io.IOException;

import net.compsoc.ox.web.admin.sections.MainSectionsEnum;
import net.compsoc.ox.web.admin.sections.Section;
import net.compsoc.ox.web.admin.templating.Template;
import net.compsoc.ox.web.admin.util.PageBuilder;
import net.compsoc.ox.web.admin.util.PathInfo;
import net.compsoc.ox.web.admin.util.StatusException;

public class EventsSection extends Section {
    
    private final Section addSection = new AddEventSection();

    @Override
    public void visitSection(PathInfo info, PageBuilder builder) throws StatusException {
        builder.setActivePage(MainSectionsEnum.EVENTS);
    }

    @Override
    public void renderPage(PathInfo info, PageBuilder builder) throws IOException, StatusException {
        builder.put("title", "events");
        
        builder.put("events", builder.database.events().getEvents());
        
        builder.render(Template.EVENTS);
    }

    @Override
    public Section getSubsection(String slug) {
        switch(slug){
            case "add":
                return addSection;
        }
        return null;
    }
    
}
