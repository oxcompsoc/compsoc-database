package net.compsoc.ox.web.admin;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.io.ByteStreams;

public class StaticServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        ClassLoader cl = CompSocAdmin.class.getClassLoader();
        
        String path = request.getPathInfo();
        InputStream is = cl.getResourceAsStream("static" + path);
        
        if(is == null){
            response.sendError(404);
            return;
        }
        
        // Send file
        response.setContentType(getContentType(path));
        ByteStreams.copy(is, response.getOutputStream());
    }
    
    private static String getContentType(String path){
        int lastIndexOf = path.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "application/octet-stream";
        }
        switch(path.substring(lastIndexOf)){
            case ".css":
                return "text/css";
            case ".js":
                return "application/javascript";
            case ".eot":
                return "application/vnd.ms-fontobject";
            case ".svg":
                return "image/svg+xml";
            case ".woff":
                return "application/font-woff";
            case ".ttf":
                return "application/x-font-ttf";
            default:
                return "application/octet-stream"; 
        }
    }
}
