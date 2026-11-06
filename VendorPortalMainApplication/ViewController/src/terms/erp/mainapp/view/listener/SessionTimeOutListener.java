package terms.erp.mainapp.view.listener;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import oracle.adf.controller.v2.lifecycle.Lifecycle;
import oracle.adf.controller.v2.lifecycle.PagePhaseEvent;
import oracle.adf.controller.v2.lifecycle.PagePhaseListener;

import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class SessionTimeOutListener implements PagePhaseListener 
{
    public SessionTimeOutListener() {
        super();
    }

    public void afterPhase(PagePhaseEvent pagePhaseEvent) 
    {
        if (pagePhaseEvent.getPhaseId() == Lifecycle.PREPARE_RENDER_ID) {

            String myAppName = "terms";
            FacesContext facesCtx = FacesContext.getCurrentInstance();
            ExternalContext extCtx = facesCtx.getExternalContext();
            HttpSession session = (HttpSession)extCtx.getSession(false);

            if (session != null) 
            {
                int secsTimeout = session.getMaxInactiveInterval();

                if (secsTimeout > 0) {
                    HttpServletRequest req = (HttpServletRequest)extCtx.getRequest();


                    String url = extCtx.getRequestContextPath() + "/faces/ErpSession";
                    String appURL =
                        "http://" + req.getServerName() + ":" + req.getServerPort() + "/" + myAppName + "/faces/ErpSession.jspx";
                    // pad the timeout by a couple of seconds to ensure session times out
                    // on the server
                    secsTimeout += 2;

                    String jsCall = "document.acmeStartClientSessionTimers = function()\n" +
                        "{\n" +
                        "  if(document.acmeSessionTimeoutTimer != null)\n" +
                        "    window.clearTimeout(document.acmeSessionTimeoutTimer);\n" +
                        "\n" +
                        "  document.acmeSessionTimeoutTimer = window.setTimeout(\"document.acmeClientSessionTimeout();\", " +
                        secsTimeout * 1000 + ");\n" +
                        "\n" +
                        "}\n" +
                        "document.acmeStartClientSessionTimers();\n" +
                        "\n" +
                        "document.acmeClientSessionTimeout = function()\n" +
                        "{\n" +
                        "    window.location.href = '" + url + "' \n" +
                        "}";
                   
                    ExtendedRenderKitService rks =
                        Service.getRenderKitService(facesCtx, ExtendedRenderKitService.class);
                    rks.addScript(facesCtx, jsCall);
                }
            }
        }
    }

    public void beforePhase(PagePhaseEvent pagePhaseEvent) {
    }


}

