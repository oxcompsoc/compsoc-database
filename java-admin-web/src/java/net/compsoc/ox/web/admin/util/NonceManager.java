package net.compsoc.ox.web.admin.util;

import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Manages nonces for use in forms, which protect against XSS attacks and
 * duplicate form submissions.
 *
 */
public class NonceManager {
    
    private static final String NONCE_COOKIE_NAME = "nonce";
    
    /**
     * Get the current nonce / generate a new nonce if one does not exist, and
     * set it in the {@link PageBuilder} for form use.
     * 
     * @param builder the {@link PageBUilder} instance
     */
    public static void setupNonce(PageBuilder builder) {
        String nonce =
            builder.getOverriddenNonce() != null ? builder.getOverriddenNonce()
                : getCurrentNonce(builder.request);
        builder.put("nonce", nonce != null ? nonce : generateNewNonce(builder));
    }
    
    /**
     * return true iff the given nonce is valid for the user, and invalidate the
     * nonce by generating a new one if so to prevent duplicate form submission.
     * 
     * @param builder the {@link PageBuilder} instance
     * @param nonce the string to check against the valid nonce, can be null
     * @return true iff the given nonce is valid
     */
    public static boolean verifyNonce(PageBuilder builder) {
        String validNonce = getCurrentNonce(builder.request);
        if (validNonce == null)
            return false;
        
        if (validNonce.equals(builder.request.getParameter("nonce"))) {
            generateNewNonce(builder);
            return true;
        } else {
            return false;
        }
    }
    
    private static String getCurrentNonce(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(NONCE_COOKIE_NAME)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    
    private static String generateNewNonce(PageBuilder builder) {
        byte[] bytes = new byte[32];
        new Random().nextBytes(bytes);
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        String nonce = sb.toString();
        Cookie cookie = new Cookie(NONCE_COOKIE_NAME, nonce);
        cookie.setMaxAge(30 * 60);
        builder.response.addCookie(cookie);
        builder.overrideNonce(nonce);
        return nonce;
    }
    
}
