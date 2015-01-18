package net.compsoc.ox.web.admin.sections;

import java.io.IOException;

import net.compsoc.ox.web.admin.util.PageBuilder;
import net.compsoc.ox.web.admin.util.PathInfo;
import net.compsoc.ox.web.admin.util.RedirectException;
import net.compsoc.ox.web.admin.util.StatusException;

public abstract class Section {
    
    public abstract void visitSection(PathInfo info, PageBuilder builder) throws StatusException;
    
    public abstract void renderPage(PathInfo info, PageBuilder builder) throws IOException,
        StatusException, RedirectException;
    
    public abstract Section getSubsection(String slug);
    
    public void handle(PathInfo info, PageBuilder builder) throws IOException, StatusException,
        RedirectException {
        visitSection(info, builder);
        
        PathInfo next = info.next();
        if (next != null) {
            Section subSection = getSubsection(next.slug());
            if (subSection != null)
                subSection.handle(next, builder);
            else
                throw StatusException.do404();
        } else {
            renderPage(info, builder);
        }
    }
    
}
