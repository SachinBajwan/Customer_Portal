package UserMaster.view.beans;

import UserMaster.model.module.UserMasAMImpl;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import oracle.jbo.ViewObject;

public class SearchUser {
    public SearchUser() {
    }

    public static Object resolveExpression(String expression) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application app = facesContext.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        ValueExpression valueExp = elFactory.createValueExpression(elContext, expression, Object.class);
        return valueExp.getValue(elContext);
    }

    public String checkCount() {

        System.out.println(resolveExpression("#{pageFlowScope.userRole}"));
        String role = null;
        if ("CA".equals((String) resolveExpression("#{pageFlowScope.userRole}"))) {
            role = "CU";
        } else if ("PA".equals((String) resolveExpression("#{pageFlowScope.userRole}"))) {
            role = "EX";
        }

        String vendorId = (String) resolveExpression("#{pageFlowScope.vendorId}");
        ViewObject VO = getAm().getSearchUsersVO1();
        VO.setWhereClause("PortalVendorUserEO.VENDOR_ID = '" + vendorId + "' AND PortalVendorUserEO.USER_ROLE = '" +
                          role + "'");
        VO.executeQuery();
        VO.setWhereClause(null);
        System.out.println(VO.getEstimatedRowCount());
        if (VO.getEstimatedRowCount() >= 3) {
            ADFUtils.showMessage("Customer can't be create user more than 3", 2);
        } else {
            return "go";
        }
        return null;
    }

    private UserMasAMImpl getAm() {
        ELContext elContext = null;
        ValueExpression valueExp = null;
        // logger.info("start:getAm()");
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            Application app = fc.getApplication();
            ExpressionFactory elFactory = app.getExpressionFactory();
            elContext = fc.getELContext();
            valueExp =
                elFactory.createValueExpression(elContext, "#{data.UserMasAMDataControl.dataProvider}", Object.class);
        } catch (Exception e) {
            //            System.out.println("Error Occured:-" + e);
            //logger.error("getAm():" + e);
        }
        // logger.info("finish:getAm()");
        return (UserMasAMImpl) valueExp.getValue(elContext);
    }

}
