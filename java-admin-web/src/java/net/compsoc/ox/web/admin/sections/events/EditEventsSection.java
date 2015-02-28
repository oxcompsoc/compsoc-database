package net.compsoc.ox.web.admin.sections.events;

import java.io.IOException;
import java.util.Date;

import net.compsoc.ox.database.iface.core.InvalidKeyException;
import net.compsoc.ox.database.iface.core.NotFoundException;
import net.compsoc.ox.database.iface.events.Event;
import net.compsoc.ox.database.iface.events.Tag;
import net.compsoc.ox.database.iface.events.Term;
import net.compsoc.ox.database.iface.events.Venue;
import net.compsoc.ox.web.admin.sections.Section;
import net.compsoc.ox.web.admin.templating.Template;
import net.compsoc.ox.web.admin.util.PageBuilder;
import net.compsoc.ox.web.admin.util.PathInfo;
import net.compsoc.ox.web.admin.util.RedirectException;
import net.compsoc.ox.web.admin.util.StatusException;

public class EditEventsSection extends Section {
    
    private final Section EditSingleEventSection = new ViewSingleEventSection();
    
    @Override
    public void visitSection(PathInfo info, PageBuilder builder) throws StatusException {}
    
    @Override
    public void renderPage(PathInfo info, PageBuilder builder) throws IOException, StatusException,
        RedirectException {
        throw new RedirectException(301, "../");
    }
    
    @Override
    public Section getSubsection(String slug) {
        return EditSingleEventSection;
    }
    
    public static class ViewSingleEventSection extends Section {
        
        @Override
        public void visitSection(PathInfo info, PageBuilder builder) throws StatusException {
            
        }
        
        @Override
        public void renderPage(PathInfo info, PageBuilder builder) throws IOException,
            StatusException, RedirectException {
            
            // Get Event
            Event event;
            try {
                event = builder.database.events().getEvent(info.slug());
            } catch (InvalidKeyException | NotFoundException ev) {
                throw StatusException.do404();
            }
            
            fillFormWithData(builder, event);
            
            // Process form data
            if (new EditEventFormHandler(event).handle(builder))
                return;
            
            builder.render(Template.EVENTS_EDIT);
        }
        
        @Override
        public Section getSubsection(String slug) {
            // TODO Auto-generated method stub
            return null;
        }
        
    }
    
    private static void fillFormWithData(PageBuilder builder, Event event) {
        if (event.title() != null)
            builder.put("title", "Edit Event: " + event.title());
        else
            builder.put("title", "Edit Event: <no name>");
        
        builder.put("button_label", "Save");
        
        builder.put("terms", builder.database.events().terms().getTerms());
        builder.put("venues", builder.database.events().venues().getVenues());
        builder.put("available_event_tags", builder.database.events().tags().getTags());
        
        // Put properties
        builder.put("form_year", event.year());
        builder.put("form_term", event.term().slug());
        builder.put("form_slug", event.slug());
        
        Date start = event.startTimestamp();
        if (start != null)
            builder.put("form_start_timestamp", EventsConstants.DATETIME_FORMAT.format(start));
        
        Date end = event.endTimestamp();
        if (end != null)
            builder.put("form_end_timestamp", EventsConstants.DATETIME_FORMAT.format(end));
        
        builder.put("form_facebook_event_id", event.facebookEventID());
        Venue v = event.venue();
        if (v != null)
            builder.put("form_venue", v.slug());
        
        builder.put("form_name", event.title());
        builder.put("form_description", event.description());
        
        StringBuilder tagsSB = new StringBuilder();
        for (Tag tag : event.tags()) {
            tagsSB.append(tag.slug());
            tagsSB.append(',');
        }
        builder.put("form_tags", tagsSB.toString());
    }
    
    private static class EditEventFormHandler extends EventsAbstractFormHandler {
        
        public final Event event;
        
        public EditEventFormHandler(Event event) {
            this.event = event;
        }
        
        @Override
        public Event getEvent(PageBuilder builder, int year, Term term, String slug) {
            event.setPrimary(year, term, slug);
            return event;
        }

        @Override
        public void complete(PageBuilder builder, Event event) throws RedirectException {
            throw new RedirectException(String.format("../../view/%s/", event.keyString()));
        }
    }
    
}
