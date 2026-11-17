package terms.erp.mainapp.view.errorhandler;

import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCErrorHandlerImpl;
import oracle.adf.model.BindingContext;
import oracle.adf.model.RegionBinding;

import oracle.adf.model.binding.DCErrorMessage;

import oracle.jbo.JboException;

public class CustomExceptionHandle extends DCErrorHandlerImpl 
{
    public CustomExceptionHandle() 
    {
        super(true);
    }

    @Override
    public void reportException(DCBindingContainer dCBindingContainer, Exception exception) {
 
        super.reportException(dCBindingContainer, exception);
    }

    private void disableAppendedCodes(Exception ex) {
        if (ex instanceof JboException) {
            JboException jboEx = (JboException) ex;
            jboEx.setAppendCodes(false);
            Object[] detailExceptions = jboEx.getDetails();
            if ((detailExceptions != null) && (detailExceptions.length > 0)) {
                for (int z = 0, numEx = detailExceptions.length; z < numEx; z++) {
                    disableAppendedCodes((Exception) detailExceptions[z]);
                }
            }
        }
    }

    //   @Override
    public DCErrorMessage getDetailedDisplayMessage(BindingContext bindingContext, RegionBinding regionBinding,
                                                    Exception exception) {
        return super.getDetailedDisplayMessage(bindingContext, regionBinding, exception);
    }

    @Override
    public String getDisplayMessage(BindingContext bindingContext, Exception exception) {
        return super.getDisplayMessage(bindingContext, exception);
    }

    @Override
    protected boolean skipException(Exception exception) {
        return super.skipException(exception);
    }

}
