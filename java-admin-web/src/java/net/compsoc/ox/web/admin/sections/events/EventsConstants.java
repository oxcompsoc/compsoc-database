package net.compsoc.ox.web.admin.sections.events;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Pattern;

public class EventsConstants {
    
    protected static final Pattern FACEBOOK_ID_PATTERN = Pattern.compile("^[0-9]+$");
    protected static final DateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm",
        Locale.ENGLISH);
    
}
