package terms.erp.mainapp.view.listener;

import terms.erp.mainapp.view.errorhandler.CustomPageLifeCycle;

import oracle.adf.controller.faces.lifecycle.ADFPhaseListener;
import oracle.adf.controller.v2.lifecycle.PageLifecycle;


public class CustomAdfPhaseListener extends ADFPhaseListener {

    public CustomAdfPhaseListener() {
        super();
       // System.out.println("<<-----Constructor of CustomAdfPhaseList----->>");
    }

    protected PageLifecycle createPageLifecycle() {
        //System.out.println("<<-----In cretae life Cycle ----->>");
        return new CustomPageLifeCycle();
    }
}