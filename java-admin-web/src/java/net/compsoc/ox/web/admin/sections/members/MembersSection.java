package net.compsoc.ox.web.admin.sections.members;

import java.io.IOException;

import net.compsoc.ox.web.admin.sections.MainSectionsEnum;
import net.compsoc.ox.web.admin.sections.Section;
import net.compsoc.ox.web.admin.templating.Template;
import net.compsoc.ox.web.admin.util.PageBuilder;
import net.compsoc.ox.web.admin.util.PathInfo;
import net.compsoc.ox.web.admin.util.StatusException;

public class MembersSection extends Section {

    @Override
    public void visitSection(PathInfo info, PageBuilder builder) throws StatusException {
        builder.setActivePage(MainSectionsEnum.MEMBERS);
    }

    @Override
    public void renderPage(PathInfo info, PageBuilder builder) throws IOException, StatusException {
        builder.put("title", "members");
        builder.render(Template.HOME);
    }

    @Override
    public Section getSubsection(String slug) {
        return null;
    }
    
}
