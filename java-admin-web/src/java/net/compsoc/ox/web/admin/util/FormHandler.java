package net.compsoc.ox.web.admin.util;

public abstract class FormHandler {
    
    /**
     * @return return true if the page was handled and content written to the
     *         response, false otherwise.
     */
    public boolean handle(PageBuilder builder) {
        if(builder.request.getMethod().equals("POST"))
            return doPostRequest(builder);
        return false;
    }
    /**
     * @return return true if the page was handled and content written to the
     *         response, false otherwise.
     */
    public abstract boolean doPostRequest(PageBuilder builder);
    
}
