package terms.erp.mainapp.view.helper;

import java.io.IOException;

import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ResourceFilter implements Filter
{
    private static ThreadLocal<HttpSession> genSession = new ThreadLocal<HttpSession>();


    private FilterConfig _filterConfig = null;

    Logger logger = Logger.getLogger("ReportFilter");

    public void init(FilterConfig filterConfig) throws ServletException {
        _filterConfig = filterConfig;
    }

    public void destroy() {
        _filterConfig = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
                                                                                                     ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        //System.out.println("RequestedSessionId in FacesSessionFilter ::::::::::" + req.getRequestedSessionId());

        genSession.set(((HttpServletRequest) request).getSession());

        chain.doFilter(request, response);

    }


    public static HttpSession getSession() 
    {
        return genSession.get();
    }
}
