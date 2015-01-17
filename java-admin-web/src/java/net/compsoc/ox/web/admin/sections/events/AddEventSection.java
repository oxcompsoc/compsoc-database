package net.compsoc.ox.web.admin.sections.events;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import net.compsoc.ox.database.iface.events.Term;
import net.compsoc.ox.web.admin.sections.MainSectionsEnum;
import net.compsoc.ox.web.admin.sections.Section;
import net.compsoc.ox.web.admin.templating.Template;
import net.compsoc.ox.web.admin.util.FormHandler;
import net.compsoc.ox.web.admin.util.PageBuilder;
import net.compsoc.ox.web.admin.util.PathInfo;
import net.compsoc.ox.web.admin.util.StatusException;

public class AddEventSection extends Section {
    
    private static final Pattern SLUG_PATTERN = Pattern.compile("^[a-z0-9\\_\\-]+$");
    
    @Override
    public void visitSection(PathInfo info, PageBuilder builder) throws StatusException {
        builder.setActivePage(MainSectionsEnum.EVENTS);
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
            
            builder.errors().add("Not Implemented");
            
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
