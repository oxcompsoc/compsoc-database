package net.compsoc.ox.web.admin.sections.events;

import java.io.IOException;

import net.compsoc.ox.database.iface.core.InvalidKeyException;
import net.compsoc.ox.database.iface.core.NotFoundException;
import net.compsoc.ox.database.iface.events.Event;
import net.compsoc.ox.database.iface.events.Events;
import net.compsoc.ox.web.admin.sections.Section;
import net.compsoc.ox.web.admin.templating.Template;
import net.compsoc.ox.web.admin.util.PageBuilder;
import net.compsoc.ox.web.admin.util.PathInfo;
import net.compsoc.ox.web.admin.util.RedirectException;
import net.compsoc.ox.web.admin.util.StatusException;
import net.compsoc.ox.web.admin.util.database.WrappedIndexedItem;

public class ViewEventsSection extends Section {
    
    private final Section ViewSingleEventSection = new ViewSingleEventSection();
    
    @Override
    public void visitSection(PathInfo info, PageBuilder builder) throws StatusException {}
    
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
            renderPage(info, builder, builder.database.events());
        }
        
        public <EKey> void renderPage(PathInfo info, PageBuilder builder, Events<EKey, ?> events)
            throws IOException, StatusException {
            
            // Get Event
            Event<EKey, ?> event;
            try {
                EKey eventKey = events.getKeyFactory().fromString(info.slug());
                event = events.getEvent(eventKey);
            } catch (InvalidKeyException | NotFoundException ev) {
                throw StatusException.do404();
            }
            
            String title = event.title() != null ? "Event: " + event.title() : "Event: <no name>";
            builder.addBreadcrumb(title, "/events/view/" + info.slug() + "/");
            builder.put("title", title);
            
            builder.put("event_wrapper", new WrappedIndexedItem<EKey>(events.getKeyFactory(), event));
            builder.render(Template.EVENTS_VIEW_SINGLE);
        }
        
        @Override
        public Section getSubsection(String slug) {
            // TODO Auto-generated method stub
            return null;
        }
        
    }
    
}
