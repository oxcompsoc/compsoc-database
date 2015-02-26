package net.compsoc.ox.web.admin.sections.events;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import net.compsoc.ox.database.iface.core.InvalidKeyException;
import net.compsoc.ox.database.iface.core.NotFoundException;
import net.compsoc.ox.database.iface.events.Event;
import net.compsoc.ox.database.iface.events.Tag;
import net.compsoc.ox.database.iface.events.Term;
import net.compsoc.ox.database.iface.events.Venue;
import net.compsoc.ox.web.admin.sections.Section;
import net.compsoc.ox.web.admin.templating.Template;
import net.compsoc.ox.web.admin.util.FormHandler;
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
            
            // Load tags
            builder.put("available_event_tags", builder.database.events().tags().getTags());
            
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
        if(v != null)
            builder.put("form_venue", v.slug());
        
        builder.put("form_name", event.title());
        builder.put("form_description", event.description());
        
        StringBuilder tagsSB = new StringBuilder();
        for(Tag tag : event.tags()){
            tagsSB.append(tag.slug());
            tagsSB.append(',');
        }
        builder.put("form_tags", tagsSB.toString());
    }
    
    private static class EditEventFormHandler extends FormHandler {
        
        public final Event event;
        
        public EditEventFormHandler(Event event) {
            this.event = event;
        }
        
        @Override
        public boolean doPostRequest(PageBuilder builder) throws RedirectException {
            
            // Get all values
            String yearString = builder.request.getParameter("year");
            String termString = builder.request.getParameter("term");
            String slug = builder.request.getParameter("slug");
            
            String startTimestampString = builder.request.getParameter("start_timestamp");
            String endTimestampString = builder.request.getParameter("end_timestamp");
            
            String facebookEventIDString = builder.request.getParameter("facebook_event_id");
            String venueString = builder.request.getParameter("venue");
            
            String name = builder.request.getParameter("name");
            String tagsString = builder.request.getParameter("tags");
            String description = builder.request.getParameter("description");
            
            int year = 0;
            if (yearString == null || yearString.isEmpty()) {
                builder.errors().add("No year given");
                builder.put("year_error", true);
            } else if (yearString.length() != 4) {
                builder.errors().add("Invalid Year");
                builder.put("year_error", true);
            } else {
                try {
                    year = Integer.parseInt(yearString);
                } catch (NumberFormatException e) {
                    builder.errors().add("Invalid Year");
                    builder.put("year_error", true);
                }
            }
            
            Term term = null;
            if (termString == null || termString.isEmpty()) {
                builder.errors().add("No term given");
                builder.put("term_error", true);
            } else {
                term = builder.database.events().terms().getTermBySlug(termString);
                if (term == null) {
                    builder.errors().add("Invalid Term");
                    builder.put("term_error", true);
                }
            }
            
            if (slug == null || slug.isEmpty()) {
                builder.errors().add("No Slug given");
                builder.put("slug_error", true);
            } else if (!EventsConstants.SLUG_PATTERN.matcher(slug).matches()) {
                builder.errors().add(
                    "Invalid Slug, please use only lover case letters, numbers, "
                        + "hyphens and underscores.");
                builder.put("slug_error", true);
            }
            
            Date startTimestamp = null;
            if (startTimestampString == null || startTimestampString.isEmpty()) {
                builder.errors().add("No Start time given");
                builder.put("start_timestamp_error", true);
            } else {
                try {
                    startTimestamp = EventsConstants.DATETIME_FORMAT.parse(startTimestampString);
                } catch (ParseException e) {
                    builder.errors().add("Invalid Start Time");
                    builder.put("start_timestamp_error", true);
                }
            }
            
            Date endTimestamp = null;
            if (endTimestampString == null || endTimestampString.isEmpty()) {
                builder.errors().add("No End time given");
                builder.put("end_timestamp_error", true);
            } else {
                try {
                    endTimestamp = EventsConstants.DATETIME_FORMAT.parse(endTimestampString);
                } catch (ParseException e) {
                    builder.errors().add("Invalid End Time");
                    builder.put("end_timestamp_error", true);
                }
            }
            
            if (startTimestamp != null && endTimestamp != null
                && startTimestamp.compareTo(endTimestamp) > 0) {
                builder.errors().add("End Time is before Start Time.");
                builder.put("start_timestamp_error", true);
                builder.put("end_timestamp_error", true);
            }
            
            if (facebookEventIDString != null && !facebookEventIDString.isEmpty()) {
                if (!EventsConstants.FACEBOOK_ID_PATTERN.matcher(facebookEventIDString).matches()) {
                    builder.errors().add("Invalid Facebook ID");
                    builder.put("facebook_event_id_error", true);
                }
            }
            
            Venue venue = null;
            if (venueString != null && !venueString.isEmpty()) {
                venue = builder.database.events().venues().getVenueBySlug(venueString);
                if (venue == null) {
                    builder.errors().add("Invalid Venue");
                    builder.put("venue_error", true);
                }
            }
            
            Set<Tag> tags = new HashSet<>();
            if(tagsString != null && !tagsString.isEmpty()){
                String[] tagSlugs = tagsString.split(",");
                for(String tagSlug : tagSlugs){
                    Tag tag = builder.database.events().tags().getTagBySlug(tagSlug);
                    if(tag == null){
                        builder.errors().add(String.format("The tag %s does not exist", tagSlug));
                        builder.put("tags_error", true);
                    } else {
                        tags.add(tag);
                    }
                }
            }
            
            if (builder.errors().isEmpty()) {
                event.setPrimary(year, term, slug);
                event.setStartTimestamp(startTimestamp);
                event.setEndTimestamp(endTimestamp);
                
                if (facebookEventIDString != null && !facebookEventIDString.isEmpty())
                    event.setFacebookEventID(facebookEventIDString);
                else
                    event.setFacebookEventID(null);
                
                // Remove venue if null
                event.setVenue(venue);
                
                if (name != null && !name.isEmpty())
                    event.setTitle(name);
                else
                    event.setTitle(null);
                
                // Remove desctiption iff null
                event.setDescription(description);
                
                event.setTags(tags);
                
                builder.messages().add("Successfully Updated Event!");
                
                throw new RedirectException(String.format("../../view/%s/", event.keyString()));
            }
            
            // Restore form data if an error exists
            if (!builder.errors().isEmpty()) {
                builder.put("form_year", yearString);
                builder.put("form_term", termString);
                builder.put("form_slug", slug);
                
                builder.put("form_start_timestamp", startTimestampString);
                builder.put("form_end_timestamp", endTimestampString);
                
                builder.put("form_facebook_event_id", facebookEventIDString);
                builder.put("form_venue", venueString);
                
                builder.put("form_name", name);
                builder.put("form_tags", tagsString);
                builder.put("form_description", description);
            }
            
            return false;
        }
    }
    
}
