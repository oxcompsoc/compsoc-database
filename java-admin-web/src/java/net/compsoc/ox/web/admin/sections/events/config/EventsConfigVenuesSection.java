package net.compsoc.ox.web.admin.sections.events.config;

import java.io.IOException;

import net.compsoc.ox.database.iface.events.Venue;
import net.compsoc.ox.database.iface.events.Venues;
import net.compsoc.ox.database.util.RegularExpressions;
import net.compsoc.ox.database.util.exceptions.DatabaseOperationException;
import net.compsoc.ox.web.admin.sections.Section;
import net.compsoc.ox.web.admin.templating.Template;
import net.compsoc.ox.web.admin.util.FormHandler;
import net.compsoc.ox.web.admin.util.NonceManager;
import net.compsoc.ox.web.admin.util.PageBuilder;
import net.compsoc.ox.web.admin.util.PathInfo;
import net.compsoc.ox.web.admin.util.RedirectException;
import net.compsoc.ox.web.admin.util.StatusException;

public class EventsConfigVenuesSection extends Section {
    
    @Override
    public void visitSection(PathInfo info, PageBuilder builder) throws StatusException {
        builder.addBreadcrumb("Venues", "/events/config/venues/");
    }
    
    @Override
    public void renderPage(PathInfo info, PageBuilder builder) throws IOException, StatusException,
        RedirectException {
        renderPage(info, builder, builder.database.events().venues());
    }
    
    public <Key> void renderPage(PathInfo info, PageBuilder builder, Venues venues)
        throws IOException, StatusException, RedirectException {
        builder.put("title", "Event Venues Config");
        
        if (new EventsConfigVenuesFormHandler(venues).handle(builder))
            return;
        
        builder.put("venues", venues.getVenues());
        
        NonceManager.setupNonce(builder);
        builder.render(Template.EVENTS_CONFIG_VENUES);
    }
    
    @Override
    public Section getSubsection(String slug) {
        return null;
    }
    
    private static class EventsConfigVenuesFormHandler extends FormHandler {
        
        private final Venues venues;
        
        public EventsConfigVenuesFormHandler(Venues venues) {
            this.venues = venues;
        }
        
        @Override
        public boolean doPostRequest(PageBuilder builder) throws RedirectException, FormError {
            String action = builder.request.getParameter("action");
            if (action != null) {
                switch (action) {
                    case "add_venue":
                        handleAddVenue(builder);
                        break;
                    case "edit_venue":
                        handleEditVenue(builder);
                        break;
                    default:
                        builder.errors().add("Unknown Action");
                }
            }
            return false;
        }
        
        private void handleAddVenue(PageBuilder builder) throws FormError {
            
            // Ensure valid values for name and slug
            String venueSlug = builder.request.getParameter("venue_slug");
            String venueName = builder.request.getParameter("venue_name");
            
            if (venueSlug == null || venueSlug.isEmpty() || venueName == null
                || venueName.isEmpty())
                throw new FormError("Make sure both venue slug and name are included");
            
            if (!RegularExpressions.SLUG_REGEX.matcher(venueSlug).matches())
                throw new FormError(RegularExpressions.SLUG_REGEX_ERROR);
            
            try {
                venues.addVenue(venueSlug, venueName);
                builder.messages().add("Successfully Added Venue");
            } catch (DatabaseOperationException e) {
                builder.errors().add("Error adding venue: " + e.getMessage());
            }
        }
        
        private void handleEditVenue(PageBuilder builder) throws FormError {
            String currentVenueSlug = builder.request.getParameter("venue");
            if (currentVenueSlug == null || currentVenueSlug.isEmpty())
                throw new FormError("No Venue slug Given");
            
            // Fetch Venue
            Venue venue = venues.getVenueBySlug(currentVenueSlug);
            if (venue == null)
                throw new FormError("No Venue Exists with that slug");
            
            // Ensure valid values for name and slug
            String venueSlug = builder.request.getParameter("venue_slug");
            String venueName = builder.request.getParameter("venue_name");
            
            if (venueSlug == null || venueSlug.isEmpty() || venueName == null
                || venueName.isEmpty())
                throw new FormError("Make sure both venue slug and name are included");
            
            if (!RegularExpressions.SLUG_REGEX.matcher(venueSlug).matches())
                throw new FormError(RegularExpressions.SLUG_REGEX_ERROR);
            
            venue.setSlugAndName(venueSlug, venueName);
            builder.messages().add("Successfully Edited Venue");
            
        }
        
        @Override
        public void restoreFormData(PageBuilder builder) {}
        
    }
    
}
