package net.compsoc.ox.web.admin.sections.events;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import net.compsoc.ox.database.iface.core.InvalidKeyException;
import net.compsoc.ox.database.iface.events.Events;
import net.compsoc.ox.database.iface.events.Tag;
import net.compsoc.ox.database.iface.events.Term;
import net.compsoc.ox.database.iface.events.Venue;
import net.compsoc.ox.database.util.RegularExpressions;
import net.compsoc.ox.web.admin.util.FormHandler;
import net.compsoc.ox.web.admin.util.PageBuilder;
import net.compsoc.ox.web.admin.util.RedirectException;

public abstract class EventsAbstractFormHandler<EKey, VKey> extends FormHandler {
    
    protected final Events<EKey, VKey> events;
    
    public EventsAbstractFormHandler(Events<EKey, VKey> events) {
        this.events = events;
    }
    
    public abstract void handleData(int year, Term term, String slug, String title,
        String description, String facebookEventId, Venue<VKey> venue, Date start, Date end,
        Set<Tag> tags) throws RedirectException;
    
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
        } else if (!RegularExpressions.SLUG_REGEX.matcher(slug).matches()) {
            builder.errors().add(RegularExpressions.SLUG_REGEX_ERROR);
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
        
        Venue<VKey> venue = null;
        if (venueString != null && !venueString.isEmpty()) {
            try {
                VKey venueKey = events.venues().getKeyFactory().fromString(venueString);
                venue = events.venues().getVenueByKey(venueKey);
            } catch (InvalidKeyException e) {
                // Handle with null venue
            }
            if (venue == null) {
                builder.errors().add("Invalid Venue");
                builder.put("venue_error", true);
            }
        }
        
        Set<Tag> tags = new HashSet<>();
        if (tagsString != null && !tagsString.isEmpty()) {
            String[] tagSlugs = tagsString.split(",");
            for (String tagSlug : tagSlugs) {
                Tag tag = builder.database.events().tags().getTagBySlug(tagSlug);
                if (tag == null) {
                    builder.errors().add(String.format("The tag %s does not exist", tagSlug));
                    builder.put("tags_error", true);
                } else {
                    tags.add(tag);
                }
            }
        }
        
        if (builder.errors().isEmpty()) {
            // Add / Update Event
            if (name != null && name.isEmpty())
                name = null;
            
            if (facebookEventIDString != null && facebookEventIDString.isEmpty())
                facebookEventIDString = null;
            
            handleData(year, term, slug, name, description, facebookEventIDString, venue,
                startTimestamp, endTimestamp, tags);
        }
        
        // Restore form data if an error exists
        if (!builder.errors().isEmpty()) {
            restoreFormData(builder);
        }
        
        return false;
    }
    
    @Override
    public void restoreFormData(PageBuilder builder) {
        builder.put("form_year", builder.request.getParameter("year"));
        builder.put("form_term", builder.request.getParameter("term"));
        builder.put("form_slug", builder.request.getParameter("slug"));
        
        builder.put("form_start_timestamp", builder.request.getParameter("start_timestamp"));
        builder.put("form_end_timestamp", builder.request.getParameter("end_timestamp"));
        
        builder.put("form_facebook_event_id", builder.request.getParameter("facebook_event_id"));
        builder.put("form_venue", builder.request.getParameter("venue"));
        
        builder.put("form_name", builder.request.getParameter("name"));
        builder.put("form_tags", builder.request.getParameter("tags"));
        builder.put("form_description", builder.request.getParameter("description"));
    }
}
