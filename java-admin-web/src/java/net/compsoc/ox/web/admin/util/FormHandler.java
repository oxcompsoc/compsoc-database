package net.compsoc.ox.web.admin.util;

public abstract class FormHandler {
    
    /**
     * @return return true if the page was handled and content written to the
     *         response, false otherwise.
     * @throws RedirectException
     */
    public boolean handle(PageBuilder builder) throws RedirectException {
        if (!builder.request.getMethod().equals("POST"))
            return false;
        
        // Check nonce
        if (NonceManager.verifyNonce(builder)) {
            return doPostRequest(builder);
        } else {
            builder.errors().add("Invalid Nonce");
            restoreFormData(builder);
            return false;
        }
    }
    
    /**
     * @return return true if the page was handled and content written to the
     *         response, false otherwise.
     * @throws RedirectException
     */
    public abstract boolean doPostRequest(PageBuilder builder) throws RedirectException;
    
    /**
     * In the event that a form can not be correctly processed, restore the form
     * data.
     * 
     * @param builder
     */
    public abstract void restoreFormData(PageBuilder builder);
    
}
