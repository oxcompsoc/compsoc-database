
package net.compsoc.ox.web.admin.sections;

import java.io.IOException;

import net.compsoc.ox.web.admin.templating.Template;
import net.compsoc.ox.web.admin.util.PageBuilder;
import net.compsoc.ox.web.admin.util.PathInfo;

public class RootSection implements Section {

    @Override
    public void visitSection(PathInfo info, PageBuilder builder) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void handlePage(PathInfo info, PageBuilder builder) throws IOException {
        builder.render(Template.HOME);
    }
    
}
