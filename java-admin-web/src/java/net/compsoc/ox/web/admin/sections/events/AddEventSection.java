package net.compsoc.ox.web.admin.sections.events;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import net.compsoc.ox.database.iface.core.InvalidKeyException;
import net.compsoc.ox.database.iface.core.NotFoundException;
import net.compsoc.ox.database.iface.events.Event;
import net.compsoc.ox.database.iface.events.Events;
import net.compsoc.ox.database.iface.events.Tag;
import net.compsoc.ox.database.iface.events.Term;
import net.compsoc.ox.database.iface.events.Venue;
import net.compsoc.ox.database.util.exceptions.DatabaseOperationException;
import net.compsoc.ox.web.admin.sections.Section;
import net.compsoc.ox.web.admin.templating.Template;
import net.compsoc.ox.web.admin.util.NonceManager;
import net.compsoc.ox.web.admin.util.PageBuilder;
import net.compsoc.ox.web.admin.util.PathInfo;
import net.compsoc.ox.web.admin.util.RedirectException;
import net.compsoc.ox.web.admin.util.StatusException;

public class AddEventSection extends Section {
    
    @Override
    public void visitSection(PathInfo info, PageBuilder builder) throws StatusException {
        builder.addBreadcrumb("Add Event", "/events/add/");
    }
    
    @Override
    public void renderPage(PathInfo info, PageBuilder builder) throws IOException, StatusException,
        RedirectException {
        renderPage(info, builder, builder.database.events());
    }
    
    public <EKey> void renderPage(PathInfo info, PageBuilder builder,
        Events<EKey> events) throws IOException, StatusException, RedirectException {
        builder.put("title", "Add Event");
        builder.put("button_label", "Add Event");
        
        // Set sensible default form details
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        builder.put("form_year", calendar.get(Calendar.YEAR));
        
        if (new AddEventFormHandler<EKey>(builder, events).handle(builder))
            return;
        
        builder.put("terms", builder.database.events().terms().getTerms());
        builder.put("venues", events.venues().getVenues());
        builder.put("available_event_tags", builder.database.events().tags().getTags());
        
        NonceManager.setupNonce(builder);
        builder.render(Template.EVENTS_EDIT);
    }
    
    @Override
    public Section getSubsection(String slug) {
        return null;
    }
    
    private static class AddEventFormHandler<EKey> extends
        EventsAbstractFormHandler<EKey> {
        
        private final PageBuilder builder;
        
        public AddEventFormHandler(PageBuilder builder, Events<EKey> events) {
            super(events);
            this.builder = builder;
        }

        @Override
        public void handleData(int year, Term term, String slug, String title, String description,
            String facebookEventId, Venue venue, Date start, Date end, Set<Tag> tags)
            throws RedirectException {
            Event<EKey> event;
            try {
                event = events.addEvent(year, term, slug, title, description, facebookEventId, venue, start, end);
            } catch (DatabaseOperationException e) {
                builder.errors().add("Unable to add event: " + e.getMessage());
                return;
            }
            try {
                events.setTags(event.key(), tags);
            } catch (NotFoundException | InvalidKeyException e) {
                throw new RuntimeException("Unexpected exception during event creation", e);
            } catch (DatabaseOperationException e) {
                builder.errors().add("Unable to set tags for event: " + e.getMessage());
                return;
            }
            
            String msg =
                String.format("Successfully Added Event! "
                    + "<a href=\"../view/%s/\">View Event</a>",
                    events.getKeyFactory().toString(event.key()));
            
            builder.messages().add(msg);
        }
    }
    
}
