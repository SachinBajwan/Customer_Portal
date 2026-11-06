package terms.erp.mainapp.view.errorhandler;

import oracle.adf.controller.faces.lifecycle.FacesPageLifecycle;

import oracle.adf.controller.v2.context.LifecycleContext;

import oracle.jbo.JboException;

public class CustomPageLifeCycle extends FacesPageLifecycle {
    public CustomPageLifeCycle() {
        super();
       // System.out.println("<<------Constructor of CustomPageLifeCycle----->> ");
    }


    public void prepareModel(LifecycleContext ctx) {
       // System.out.println("<<------In prepare Model-------->>");
        if (!(ctx.getBindingContext().getErrorHandler() instanceof CustomExceptionHandle)) {
            ctx.getBindingContext().setErrorHandler(new CustomExceptionHandle());
        }
        super.prepareModel(ctx);
    }
}
