package net.compsoc.ox.web.admin.sections.events.config;

import java.io.IOException;

import net.compsoc.ox.web.admin.sections.Section;
import net.compsoc.ox.web.admin.templating.Template;
import net.compsoc.ox.web.admin.util.PageBuilder;
import net.compsoc.ox.web.admin.util.PathInfo;
import net.compsoc.ox.web.admin.util.StatusException;

public class EventsConfigTagsSection extends Section {

    @Override
    public void visitSection(PathInfo info, PageBuilder builder) throws StatusException {
        builder.addBreadcrumb("Tags", "/events/config/tags/");
    }

    @Override
    public void renderPage(PathInfo info, PageBuilder builder) throws IOException, StatusException {
        builder.put("title", "Event Tags Config");
        builder.render(Template.EVENTS_CONFIG_TAGS);
    }

    @Override
    public Section getSubsection(String slug) {
        return null;
    }
    
}
