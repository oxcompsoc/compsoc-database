package net.compsoc.ox.database.impl.dummy;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.compsoc.ox.database.iface.events.Term;
import net.compsoc.ox.database.iface.events.Terms;

public class DummyTerms implements Terms {
    
    private final Map<String, DummyTerm> terms = new HashMap<>();
    
    {
        DummyTerm m = new DummyTerm();
        m.name = "Michaelmas";
        m.slug = "michaelmas";
        terms.put("michaelmas", m);
        
        DummyTerm h = new DummyTerm();
        h.name = "Hilary";
        h.slug = "hilary";
        terms.put("hilary", h);
        
        DummyTerm t = new DummyTerm();
        t.name = "Trinity";
        t.slug = "trinity";
        terms.put("trinity", t);
    }
    
    @Override
    public DummyTerm getTermBySlug(String slug){
        return terms.get(slug);
    }
    
    private class DummyTerm implements Term {
        
        private String name;
        private String slug;

        @Override
        public String name() {
            return name;
        }

        @Override
        public String slug() {
            return slug;
        }
        
    }

    @Override
    public List<Term> getTerms() {
        List<Term> ts = new LinkedList<>();
        for(Term t : terms.values()){
            ts.add(t);
        }
        return ts;
    }
    
}
