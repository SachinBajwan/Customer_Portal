package terms.erp.mainapp.view.errorhandler;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import oracle.adf.controller.ControllerContext;
import oracle.adf.share.logging.ADFLogger;
import oracle.adf.view.rich.context.ExceptionHandler;

public class ControllerExceptionHandler extends ExceptionHandler
{

    public ControllerExceptionHandler() {
    }

    @Override
    public void handleException(FacesContext facesContext, Throwable throwable, PhaseId phaseId) throws Throwable {
        // TODO Implement this method
        Throwable cause = throwable.getCause();
        throwable = cause == null ? throwable : cause;
        String msg = throwable.getMessage();
        if (msg == null || msg.trim().length() == 0) {
            msg = throwable.toString();
        }

        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));
    }
}

