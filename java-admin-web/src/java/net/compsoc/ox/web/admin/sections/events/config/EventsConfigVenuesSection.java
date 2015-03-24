package net.compsoc.ox.web.admin.sections.events.config;

import java.io.IOException;

import net.compsoc.ox.database.iface.core.InvalidKeyException;
import net.compsoc.ox.database.iface.events.Venue;
import net.compsoc.ox.database.iface.events.Venues;
import net.compsoc.ox.database.util.RegularExpressions;
import net.compsoc.ox.web.admin.sections.Section;
import net.compsoc.ox.web.admin.templating.Template;
import net.compsoc.ox.web.admin.util.FormHandler;
import net.compsoc.ox.web.admin.util.NonceManager;
import net.compsoc.ox.web.admin.util.PageBuilder;
import net.compsoc.ox.web.admin.util.PathInfo;
import net.compsoc.ox.web.admin.util.RedirectException;
import net.compsoc.ox.web.admin.util.StatusException;
import net.compsoc.ox.web.admin.util.database.WrappedIndexedItem;

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
    
    public <Key> void renderPage(PathInfo info, PageBuilder builder, Venues<Key> venues)
        throws IOException, StatusException, RedirectException {
        builder.put("title", "Event Venues Config");
        
        if (new EventsConfigVenuesFormHandler<Key>(venues).handle(builder))
            return;
        
        builder.put("venues",
            WrappedIndexedItem.wrappedIndexedItemList(venues.getKeyFactory(), venues.getVenues()));
        
        NonceManager.setupNonce(builder);
        builder.render(Template.EVENTS_CONFIG_VENUES);
    }
    
    @Override
    public Section getSubsection(String slug) {
        return null;
    }
    
    private static class EventsConfigVenuesFormHandler<Key> extends FormHandler {
        
        private final Venues<Key> venues;
        
        public EventsConfigVenuesFormHandler(Venues<Key> venues) {
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
            
            venues.addVenue(venueSlug, venueName);
            builder.messages().add("Successfully Added Venue");
        }
        
        private void handleEditVenue(PageBuilder builder) throws FormError {
            String venueKeyString = builder.request.getParameter("venue");
            if (venueKeyString == null || venueKeyString.isEmpty())
                throw new FormError("No Venue Key Given");
            
            Key venueKey;
            try {
                venueKey = venues.getKeyFactory().fromString(venueKeyString);
            } catch (InvalidKeyException e) {
                throw new FormError("Invalid Venue Key");
            }
            
            // Fetch Venue
            Venue<Key> venue = venues.getVenueByKey(venueKey);
            if (venue == null)
                throw new FormError("No Venue Exists with that Key");
            
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
