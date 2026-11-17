package view.bspl.dashboard.beans;

import adf.utils.bean.ADFBeanUtils;
import adf.utils.exception.ADFUtilsException;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;
import oracle.adf.model.BindingContext;
import oracle.jbo.Row;
import oracle.jbo.ViewObject;
import oracle.adf.model.binding.DCDataControl;

import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewCriteria;
import oracle.jbo.ViewCriteriaManager;

public class DashboardBean {
    
    private String userName = "Ramesh Kumar";
    private String lastLoginInfo = "22-Sep-2026 18:42 from Chrome, Agra | Not you? Secure account";
    
    // Dynamic metric variables
    private String openOrdersCount = "0";
    private String inTransitCount = "0";
    private String deliveredCount = "0";
    private String invoicesDueAmount = "0";

    public DashboardBean() {
        loadDashboardMetrics();
        loadRecentOrders();
    }

    public void loadRecentOrders()    
    {
        try{
                String vendorId = ADFBeanUtils.evaluateEL("#{pageFlowScope.vendorId}").toString();
                OperationBinding ob = ADFUtils.findOperation("callOrdersProcedure");
                ob.execute(); 
                OperationBinding ob1 = ADFUtils.findOperation("getCustomerName");
                ob1.getParamsMap().put("vendorId", vendorId);
                ob1.execute();
                userName = (String) ob1.getResult();
                System.out.println(userName);
            }
        catch(Exception ex)
        {
            ex.printStackTrace();
            }
        }
    /**
     * Programmatically fetches counts from FileMasterErpVVO1 based on REMARKS
     */
    public void loadDashboardMetrics() {
        try {
            String vendorId = ADFBeanUtils.evaluateEL("#{pageFlowScope.vendorId}").toString();
            OperationBinding ob = ADFUtils.findOperation("getCustomerCode");
            ob.getParamsMap().put("vendorId", vendorId);
            ob.execute(); 
            String CustomerCode = (String) ob.getResult();
            BindingContext bctx = BindingContext.getCurrent();
            BindingContainer bindings = bctx.getCurrentBindingsEntry();
            
            // Access the Data Control and View Object
            DCDataControl dc = (DCDataControl) bindings.get("DashboardAMDataControl"); // Replace with your actual Data Control name
            if (dc != null) {
                oracle.jbo.ApplicationModule am = (oracle.jbo.ApplicationModule) dc.getDataProvider();
                ViewObject vo = am.findViewObject("CustPendingSummaryVO1"); // Make sure this matches your VO definition name
                
                if (vo != null) {
//                    ViewCriteriaManager vcm = vo.getViewCriteriaManager();
//                    ViewCriteria hVc = vcm.getViewCriteria("CustPendingSummaryVOCriteria");
//                    vo.applyViewCriteria(hVc, false);
                    System.out.println("Customer Code  : "+CustomerCode);
//                    vo.setNamedWhereClauseParam("bindCustCd", CustomerCode);   
                    vo.setWhereClause("CustPendingSummaryEO.CUST_CODE ='"+CustomerCode+"'");
                    vo.executeQuery();
                    RowSetIterator iter = vo.createRowSetIterator(null);

                    while (iter.hasNext()) {

                        Row row = iter.next();

                        String remarks = (String) row.getAttribute("Remarks");
                        Object recCnt = row.getAttribute("RecCnt");
                        
                        if (remarks != null && recCnt == mapRemarksToMetric(remarks, recCnt)) {
                            // Map your REMARKS column values to the corresponding dashboard metrics
                            if (remarks.equalsIgnoreCase("Pending Sale Order")) {
                                openOrdersCount = String.valueOf(recCnt);
                            } else if (remarks.equalsIgnoreCase("In Transit")) {
                                inTransitCount = String.valueOf(recCnt);
                            } else if (remarks.equalsIgnoreCase("Total invoice")) {
                                deliveredCount = String.valueOf(recCnt);
                            } else if (remarks.equalsIgnoreCase("Invoices Due")) {
                                invoicesDueAmount = String.valueOf(recCnt);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object mapRemarksToMetric(String remarks, Object recCnt) {
        return recCnt;
    }

    // Getters and Setters for UI binding
    public String getUserName() { return userName; }
    public String getLastLoginInfo() { return lastLoginInfo; }
    
    public String getOpenOrdersCount() { return openOrdersCount; }
    public String getInTransitCount() { return inTransitCount; }
    public String getDeliveredCount() { return deliveredCount; }
    public String getInvoicesDueAmount() { return invoicesDueAmount; }
}