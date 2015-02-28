package net.compsoc.ox.web.admin.sections.events;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import net.compsoc.ox.database.iface.events.Event;
import net.compsoc.ox.database.iface.events.Term;
import net.compsoc.ox.web.admin.sections.Section;
import net.compsoc.ox.web.admin.templating.Template;
import net.compsoc.ox.web.admin.util.PageBuilder;
import net.compsoc.ox.web.admin.util.PathInfo;
import net.compsoc.ox.web.admin.util.RedirectException;
import net.compsoc.ox.web.admin.util.StatusException;

public class AddEventSection extends Section {
    
    @Override
    public void visitSection(PathInfo info, PageBuilder builder) throws StatusException {}
    
    @Override
    public void renderPage(PathInfo info, PageBuilder builder) throws IOException, StatusException,
        RedirectException {
        builder.put("title", "Add Event");
        builder.put("button_label", "Add Event");
        
        // Set sensible default form details
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        builder.put("form_year", calendar.get(Calendar.YEAR));
        
        if (new AddEventFormHandler().handle(builder))
            return;
        
        builder.put("terms", builder.database.events().terms().getTerms());
        builder.put("venues", builder.database.events().venues().getVenues());
        builder.put("available_event_tags", builder.database.events().tags().getTags());
        
        builder.render(Template.EVENTS_EDIT);
    }
    
    @Override
    public Section getSubsection(String slug) {
        return null;
    }
    
    private static class AddEventFormHandler extends EventsAbstractFormHandler {
        
        @Override
        public Event getEvent(PageBuilder builder, int year, Term term, String slug) {
            return builder.database.events().addEvent(year, term, slug);
        }
        
        @Override
        public void complete(PageBuilder builder, Event event) throws RedirectException {
            String msg =
                String.format("Successfully Added Event! "
                    + "<a href=\"../view/%s/\">View Event</a>", event.keyString());
            
            builder.messages().add(msg);
            
        }
    }
    
}
