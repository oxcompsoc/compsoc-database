package net.compsoc.ox.web.admin.sections.events;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import net.compsoc.ox.database.iface.events.Event;
import net.compsoc.ox.database.iface.events.Term;
import net.compsoc.ox.web.admin.sections.Section;
import net.compsoc.ox.web.admin.templating.Template;
import net.compsoc.ox.web.admin.util.FormHandler;
import net.compsoc.ox.web.admin.util.PageBuilder;
import net.compsoc.ox.web.admin.util.PathInfo;
import net.compsoc.ox.web.admin.util.StatusException;

public class AddEventSection extends Section {
    
    private static final Pattern SLUG_PATTERN = Pattern.compile("^[a-z0-9\\_\\-]+$");
    private static final Pattern FACEBOOK_ID_PATTERN = Pattern.compile("^[0-9]+$");
    private static final DateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm",
        Locale.ENGLISH);
    
    @Override
    public void visitSection(PathInfo info, PageBuilder builder) throws StatusException {
    }
    
    @Override
    public void renderPage(PathInfo info, PageBuilder builder) throws IOException, StatusException {
        builder.put("title", "Add Event");
        
        // Set sensible default form details
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        builder.put("form_year", calendar.get(Calendar.YEAR));
        
        if (new AddEventFormHandler().handle(builder))
            return;
        
        builder.put("terms", builder.database.events().terms().getTerms());
        
        builder.render(Template.EVENTS_ADD);
    }
    
    @Override
    public Section getSubsection(String slug) {
        return null;
    }
    
    private class AddEventFormHandler extends FormHandler {
        
        @Override
        public boolean doPostRequest(PageBuilder builder) {
            
            // Get all values
            String yearString = builder.request.getParameter("year");
            String termString = builder.request.getParameter("term");
            String slug = builder.request.getParameter("slug");
            
            String startTimestampString = builder.request.getParameter("start_timestamp");
            String endTimestampString = builder.request.getParameter("end_timestamp");
            
            String facebookEventIDString = builder.request.getParameter("facebook_event_id");
            
            String name = builder.request.getParameter("name");
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
            } else if (!SLUG_PATTERN.matcher(slug).matches()) {
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
                    startTimestamp = DATETIME_FORMAT.parse(startTimestampString);
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
                    endTimestamp = DATETIME_FORMAT.parse(endTimestampString);
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
                if (!FACEBOOK_ID_PATTERN.matcher(facebookEventIDString).matches()) {
                    builder.errors().add("Invalid Facebook ID");
                    builder.put("facebook_event_id_error", true);
                }
            }
            
            if (builder.errors().isEmpty()) {
                Event e = builder.database.events().addEvent(year, term, slug);
                e.setStartTimestamp(startTimestamp);
                e.setEndTimestamp(endTimestamp);
                
                if (facebookEventIDString != null)
                    e.setFacebookEventID(facebookEventIDString);
                
                if (name != null)
                    e.setTitle(name);
                
                if (description != null)
                    e.setDescription(description);
                
                String msg =
                    String.format("Successfully Added Event! "
                        + "<a href=\"../view/%s/\">View Event</a>", e.keyString());
                
                builder.messages().add(msg);
            }
            
            // Restore form data if an error exists
            if (!builder.errors().isEmpty()) {
                builder.put("form_year", yearString);
                builder.put("form_term", termString);
                builder.put("form_slug", slug);
                
                builder.put("form_start_timestamp", startTimestampString);
                builder.put("form_end_timestamp", endTimestampString);
                
                builder.put("form_facebook_event_id", facebookEventIDString);
                
                builder.put("form_namer", name);
                builder.put("form_description", description);
            }
            
            return false;
        }
    }
    
}
