
package net.compsoc.ox.web.admin.sections;

import java.io.IOException;

import net.compsoc.ox.web.admin.sections.events.EventsSection;
import net.compsoc.ox.web.admin.sections.members.MembersSection;
import net.compsoc.ox.web.admin.templating.Template;
import net.compsoc.ox.web.admin.util.PageBuilder;
import net.compsoc.ox.web.admin.util.PathInfo;

public class RootSection extends Section {
    
    private final Section eventsSection = new EventsSection();
    private final Section membersSection = new MembersSection();

    @Override
    public void visitSection(PathInfo info, PageBuilder builder) {
    }

    @Override
    public void renderPage(PathInfo info, PageBuilder builder) throws IOException {
        builder.setActivePage(MainSectionsEnum.HOME);
        
        builder.put("title", "CompSoc Admin Panel");
        
        builder.render(Template.HOME);
    }

    @Override
    public Section getSubsection(String slug) {
        switch(slug){
            case "events":
                return eventsSection;
            case "members":
                return membersSection;
        }
        return null;
    }
    
}
