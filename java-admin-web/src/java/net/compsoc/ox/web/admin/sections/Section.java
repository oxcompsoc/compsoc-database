package net.compsoc.ox.web.admin.sections;

import java.io.IOException;

import net.compsoc.ox.web.admin.util.PageBuilder;
import net.compsoc.ox.web.admin.util.PathInfo;

public interface Section {
    
    public void visitSection(PathInfo info, PageBuilder builder);
    
    public void handlePage(PathInfo info, PageBuilder builder) throws IOException;
    
}
