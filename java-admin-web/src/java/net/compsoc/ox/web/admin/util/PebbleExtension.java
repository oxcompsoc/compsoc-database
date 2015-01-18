package net.compsoc.ox.web.admin.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mitchellbosecke.pebble.extension.AbstractExtension;
import com.mitchellbosecke.pebble.extension.Filter;
import com.mitchellbosecke.pebble.extension.Function;

public class PebbleExtension extends AbstractExtension {
    
    private static final SimpleDateFormat TIME = new SimpleDateFormat("HH:mm z");
    private static final SimpleDateFormat SHORT_DATE_TIME = new SimpleDateFormat(
        "dd MMMM - HH:mm z");
    
    private final Map<String, Filter> filters;
    private final Map<String, Function> functions;
    
    {
        filters = new HashMap<>();
        filters.put("day_of_week", new SimpleDateFormatFilter("EEEE"));
        filters.put("long_date", new SimpleDateFormatFilter("dd MMMM yyyy"));
        filters.put("time", new SimpleDateFormatFilter(TIME));
        filters.put("short_date_and_time", new SimpleDateFormatFilter(SHORT_DATE_TIME));
        filters.put("date_if_different", new DateIfDifferentFilter());
        
        functions = new HashMap<>();
        functions.put("same", new SameFunction());
    }
    
    @Override
    public Map<String, Filter> getFilters() {
        return filters;
    }
    
    @Override
    public Map<String, Function> getFunctions() {
        return functions;
    }
    
    private static class SameFunction implements Function {
        
        @Override
        public List<String> getArgumentNames() {
            return Arrays.asList(new String[] { "year1", "year2" });
        }
        
        @Override
        public Object execute(Map<String, Object> args) {
            System.out.println(args);
            Object year1 = args.get("year1");
            Object year2 = args.get("year2");
            return year1 != null && year1.equals(year2);
        }
        
    }
    
    private static class SimpleDateFormatFilter implements Filter {
        
        private final SimpleDateFormat format;
        
        public SimpleDateFormatFilter(String format) {
            this.format = new SimpleDateFormat(format);
        }
        
        public SimpleDateFormatFilter(SimpleDateFormat format) {
            this.format = format;
        }
        
        @Override
        public List<String> getArgumentNames() {
            return null;
        }
        
        @Override
        public Object apply(Object obj, Map<String, Object> args) {
            if (obj == null)
                return null;
            if (obj instanceof Date) {
                return format.format((Date) obj);
            }
            throw new InternalError("Not a date object");
        }
        
    }
    
    private static class DateIfDifferentFilter implements Filter {
        
        @Override
        public List<String> getArgumentNames() {
            return Arrays.asList(new String[] { "other" });
        }
        
        @Override
        public Object apply(Object obj, Map<String, Object> args) {
            if (obj == null)
                return null;
            
            if (!(obj instanceof Date))
                throw new InternalError("Not a date object");
            
            Object other = args.get("other");
            if (other != null) {
                
                if (!(other instanceof Date))
                    throw new InternalError("Parameter is not a date object");
                
                Calendar cal1 = Calendar.getInstance();
                Calendar cal2 = Calendar.getInstance();
                
                cal1.setTime((Date) obj);
                cal2.setTime((Date) other);
                
                if (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                    && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR))
                    return TIME.format((Date) obj);
                
            }
            return SHORT_DATE_TIME.format((Date) obj);
        }
        
    }
    
}
