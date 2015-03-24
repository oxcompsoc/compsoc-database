package net.compsoc.ox.database.util;

import java.util.regex.Pattern;

public class RegularExpressions {
    
    public static final Pattern SLUG_REGEX = Pattern.compile("^[a-z0-9\\-\\_]+$");
    public static final String SLUG_REGEX_ERROR =
        "Invalid Slug, please use only lower case letters, numbers, hyphens and underscores.";
    
}
