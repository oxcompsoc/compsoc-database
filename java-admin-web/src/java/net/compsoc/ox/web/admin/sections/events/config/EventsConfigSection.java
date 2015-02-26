package net.compsoc.ox.web.admin.sections.events.config;

import java.io.IOException;

import net.compsoc.ox.web.admin.sections.Section;
import net.compsoc.ox.web.admin.templating.Template;
import net.compsoc.ox.web.admin.util.PageBuilder;
import net.compsoc.ox.web.admin.util.PathInfo;
import net.compsoc.ox.web.admin.util.StatusException;

public class EventsConfigSection extends Section {

    @Override
    public void visitSection(PathInfo info, PageBuilder builder) throws StatusException {
    }

    @Override
    public void renderPage(PathInfo info, PageBuilder builder) throws IOException, StatusException {
        builder.put("title", "Events Config");
        builder.render(Template.EVENTS_CONFIG);
    }

    @Override
    public Section getSubsection(String slug) {
        return null;
    }
    
}
