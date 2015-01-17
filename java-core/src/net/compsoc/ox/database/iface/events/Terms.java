package net.compsoc.ox.database.iface.events;

import java.util.List;

public interface Terms {
    
    public Term getTermBySlug(String slug);
    
    public List<Term> getTerms();
    
}
