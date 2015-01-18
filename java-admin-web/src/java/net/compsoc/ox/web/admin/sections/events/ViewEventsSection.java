package net.compsoc.ox.web.admin.sections.events;

import java.io.IOException;
import net.compsoc.ox.database.iface.core.InvalidKeyException;
import net.compsoc.ox.database.iface.core.NotFoundException;
import net.compsoc.ox.database.iface.events.Event;
import net.compsoc.ox.web.admin.sections.Section;
import net.compsoc.ox.web.admin.templating.Template;
import net.compsoc.ox.web.admin.util.PageBuilder;
import net.compsoc.ox.web.admin.util.PathInfo;
import net.compsoc.ox.web.admin.util.RedirectException;
import net.compsoc.ox.web.admin.util.StatusException;

public class ViewEventsSection extends Section {
    
    private final Section ViewSingleEventSection = new ViewSingleEventSection();
    
    @Override
    public void visitSection(PathInfo info, PageBuilder builder) throws StatusException {
    }
    
    @Override
    public void renderPage(PathInfo info, PageBuilder builder) throws IOException, StatusException,
        RedirectException {
        throw new RedirectException(301, "../");
    }
    
    @Override
    public Section getSubsection(String slug) {
        return ViewSingleEventSection;
    }
    
    public static class ViewSingleEventSection extends Section {

        @Override
        public void visitSection(PathInfo info, PageBuilder builder) throws StatusException {
            
        }

        @Override
        public void renderPage(PathInfo info, PageBuilder builder) throws IOException,
            StatusException {
            
            // Get Event
            Event event;
            try {
                event = builder.database.events().getEvent(info.slug());
            } catch (InvalidKeyException | NotFoundException ev) {
                throw StatusException.do404();
            }
            
            if(event.title() != null)
                builder.put("title", "Event: " + event.title());
            else
                builder.put("title", "Event: <no name>");
                
            builder.put("event", event);
            builder.render(Template.EVENTS_VIEW_SINGLE);
        }

        @Override
        public Section getSubsection(String slug) {
            // TODO Auto-generated method stub
            return null;
        }
        
    }
    
}
