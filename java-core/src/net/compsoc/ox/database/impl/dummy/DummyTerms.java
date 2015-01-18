package net.compsoc.ox.database.impl.dummy;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.compsoc.ox.database.iface.events.Term;
import net.compsoc.ox.database.iface.events.TermDates;
import net.compsoc.ox.database.iface.events.Terms;

public class DummyTerms implements Terms {
    
    private final Map<String, DummyTerm> terms = new HashMap<>();
    private final TermDates termDates;
    
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
        
        termDates = new DummyTermDates(m, h, t);
    }
    
    @Override
    public DummyTerm getTermBySlug(String slug) {
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
        for (Term t : terms.values()) {
            ts.add(t);
        }
        return ts;
    }
    
    @Override
    public TermDates termDates() {
        return termDates;
    }
    
    private static class DummyTermDates implements TermDates {
        
        private static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        public final Map<Integer, Map<Term, Date>> termDates;
        
        public static final int YEAR_FALLBACK = 2015;
        
        private DummyTermDates(Term michaelmas, Term hilary, Term trinity) {
            termDates = new HashMap<>();
            
            // Dates for monday of week 1
            
            try {
                Map<Term, Date> map2014 = new HashMap<>();
                map2014.put(michaelmas, FORMAT.parse("2014-10-13"));
                termDates.put(2014, map2014);
                
                Map<Term, Date> map2015 = new HashMap<>();
                map2015.put(hilary, FORMAT.parse("2015-01-19"));
                map2015.put(trinity, FORMAT.parse("2015-04-27"));
                map2015.put(michaelmas, FORMAT.parse("2015-10-12"));
                termDates.put(2015, map2015);
                
                Map<Term, Date> map2016 = new HashMap<>();
                map2016.put(hilary, FORMAT.parse("2016-01-18"));
                map2016.put(trinity, FORMAT.parse("2016-04-25"));
                map2016.put(michaelmas, FORMAT.parse("2016-10-10"));
                termDates.put(2016, map2016);
            } catch (ParseException e) {
                throw new InternalError(e);
            }
        }
        
        @Override
        public Date getWeek1Date(int year, Term term) {
            // Attempt to get correct date
            Map<Term, Date> yearMap = termDates.get(year);
            if (yearMap != null) {
                Date termDate = yearMap.get(term);
                if (termDate != null)
                    return termDate;
            }
            
            // Fallback to YEAR_FALLBACK
            return termDates.get(YEAR_FALLBACK).get(term);
        }
        
    }
    
}
