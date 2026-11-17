package view.bspl.self.beans;

import javax.faces.event.ActionEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCDataControl;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import oracle.jbo.ApplicationModule;
import oracle.jbo.ViewObject;

public class ProfileBean {
    public ProfileBean() {
    }

    public void saveProfile(ActionEvent actionEvent) {
        try {
            BindingContext bctx = BindingContext.getCurrent();
            BindingContainer bindings = bctx.getCurrentBindingsEntry();
            DCDataControl dc = (DCDataControl) bindings.get("SelfServiceAMDataControl");
            if (dc != null) {
                ApplicationModule am = (ApplicationModule) dc.getDataProvider();
                ViewObject vo = am.findViewObject("PortalVendorRegdVO1");
                
                DCBindingContainer bc = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
                OperationBinding operationbinding = bc.getOperationBinding("Commit");
                operationbinding.execute();
                }
//                else
//                {
//                        ADFUtils.showMessage("Please Add Items in Order Details.", 2);  
//                    }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
