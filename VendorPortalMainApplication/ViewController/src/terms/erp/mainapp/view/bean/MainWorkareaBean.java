package terms.erp.mainapp.view.bean;

import adf.utils.bean.ADFBeanUtils;

import adf.utils.model.ADFModelUtils;

import java.io.IOException;
import java.io.Serializable;

import java.sql.Types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import javax.faces.model.SelectItem;

import oracle.adf.controller.ControllerContext;
import oracle.adf.controller.TaskFlowId;
import oracle.adf.model.BindingContext;
import oracle.adf.model.DataControlFrame;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCDataControl;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.logging.ADFLogger;

import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.fragment.RichRegion;
import oracle.adf.view.rich.component.rich.input.RichInputComboboxListOfValues;
import oracle.adf.view.rich.component.rich.input.RichInputListOfValues;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.layout.RichPanelFormLayout;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.component.rich.layout.RichPanelSplitter;

import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.binding.OperationBinding;

import oracle.jbo.Row;

import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;

import terms.erp.mainapp.model.helper.TermsParam;
import terms.erp.mainapp.view.utils.DBConfiguration;
import terms.erp.mainapp.view.utils.TermsADFUtils;


public class MainWorkareaBean implements Serializable {
    private String defaultRegionTF = "/WEB-INF/DefaultRegionFlow.xml#DefaultRegionFlow";
    private String taskFlowId;
    private Map<String, Object> parameterMap;
    private final String NAVIGATEFROMCLOSEBUTTON = "CB";
    private final String NAVIGATEFROMHOMEBUTTON = "HB";
    private final String NAVIGATEFROMMAINTREE = "MT";
    private final String NAVIGATEFROMMAINTCODE = "MTC";
    private final String MENU = "MENU";
    private final String FORM = "FORM";
    private String navMode = "L";
    private String newTaskFlowId;
    private String globelFileID;

    static String config = "TermsMainAppAMLocal";
    private RichPopup bindDocCheckPopup;
    private RichInputText mainDocFileNameBind;
    private RichInputText mainDocFileIdBind;
    private RichInputText mainDocFlowPathBind;
    private RichInputText mainDocModuleBind;
    private RichInputComboboxListOfValues mainTcodeBind;

    private String mainDocumentInput;
    private RichPanelFormLayout panelFormBind;
    private RichPopup popMostVisit;
    private RichPanelGroupLayout alignpopup;
    private String taskFlowId1 = "/WEB-INF/DefaultRegionFlow.xml#DefaultRegionFlow";


    public MainWorkareaBean() {
    }
    private static final ADFLogger log = ADFLogger.createADFLogger(MainWorkareaBean.class);


    public void handleNavigationMainWorkarea() {
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        NavigationHandler handler = app.getNavigationHandler();
        handler.handleNavigation(context, null, "goToMainwork");
    }
    
    public void handleNavigationDashboard() {
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        NavigationHandler handler = app.getNavigationHandler();
        handler.handleNavigation(context, null, "goToDashboard");
    }

    public void setParameterMap(Map<String, Object> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public void handleHomeNavigation() {
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        NavigationHandler handler = app.getNavigationHandler();
        handler.handleNavigation(context, null, "goToDashboard");
    }

    public void homeButtonAL(ActionEvent ae) {
      /*  navMode = NAVIGATEFROMHOMEBUTTON;
        ADFBeanUtils.findOperation("onPageLoadExecute").execute();
        if (this.pendingChangeExist()) {
            RichPopup.PopupHints hints = new RichPopup.PopupHints();
            getBindDocCheckPopup().show(hints);
        } else {
            reloadThePage();
            regionBind.refresh(FacesContext.getCurrentInstance());
            try {
                this.handleHomeNavigation();
            } catch (Exception e) {
                // TODO: Add catch code
                e.printStackTrace();
            }
        }*/
      handleNavigationDashboard();
    }

    public void homeDocumentCodeVCL(ValueChangeEvent vce) {
        vce.getComponent().processUpdates(FacesContext.getCurrentInstance());
        if (vce != null) {
            Row row = (Row) TermsADFUtils.evaluateEL("#{bindings.DocumentCodeLovVVO1Iterator.currentRow}");
            String fileName = (String) row.getAttribute("DocFileNameTrans");
            String tfFileId = (String) row.getAttribute("DocFileIdTrans");
            String flowPath = (String) row.getAttribute("DocFlowPathTrans");
            // String tcode = (String) row.getAttribute("TransModule");
            String tcode = (String) row.getAttribute("DocTCodeTrans");
            log.info("In Landing Page-->>fileName::" + fileName + ":tfFileId::" + tfFileId + ":flowPath::" + flowPath);
            TermsADFUtils.setValueToPageFlowScope("CurrModule", tcode);
            HashMap<String, Object> tCodeMap = new HashMap<String, Object>();
            tCodeMap.put("tCode", tcode);
            TermsADFUtils.setValueToPageFlowScope("GblTCodeMap", tCodeMap);
            if (fileName != null && flowPath != null && tfFileId != null) {
                if (flowPath != null) {
                    try {
                        this.handleNavigationMainWorkarea();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    log.info("|homeDocumentCodeVCL|The tf path is-----::" + TermsADFUtils.getTaskFlowId(flowPath));
                    this.getDocumentFileDetails(tfFileId);
                    setDynamicTaskFlowId(flowPath);
                    this.setParameterMap(this.getParameterMap());
                    row.setAttribute("DocTCodeTrans", null);

                    reloadHomePage();

                    DCBindingContainer bc = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
                    OperationBinding operationbinding = bc.getOperationBinding("Insert_Visit");
                    operationbinding.getParamsMap().put("UserId", TermsADFUtils.getValueFromPageFlowScope("UserId"));
                    operationbinding.getParamsMap().put("TCode", tcode);
                    operationbinding.getParamsMap().put("FileId", tfFileId);
                    operationbinding.getParamsMap().put("FileName", fileName);
                    operationbinding.getParamsMap().put("FilePath", flowPath);

                    operationbinding.execute();

                }
            } else {
                log.info("Something went wrong in File Id and File Name---");
            }
        }
    }

    public void globalModuleNavigationAL(ActionEvent ae) {
        Object obj = ae.getComponent().getAttributes().get("moduleName");
        System.out.println("Module Id is----::" + obj.toString());
        TermsADFUtils.setValueToPageFlowScope("CurrModule", obj.toString());
        log.info(" obj.toString():" + obj.toString() + " CurrModule:" +
                 TermsADFUtils.getValueFromPageFlowScope("CurrModule"));
        try {
            this.handleNavigationMainWorkarea();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("|globalModuleNavigationAL|The Default taskflowww---->>" + defaultRegionTF);
        this.setDynamicTaskFlowId(defaultRegionTF);
        reloadHomePage();
        ADFBeanUtils.findOperation("onPageLoadExecute").execute();
    }

    public TaskFlowId getDynamicTaskFlowId() {
        try {
            return TaskFlowId.parse(taskFlowId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setDynamicTaskFlowId(String taskFlowId) {
        this.taskFlowId = taskFlowId;
    }


    public void globalNavigation(String tFileId, String tfPath) {
        log.info("|globalNavigation|Taskflow path---" + tfPath);
        if (this.pendingChangeExist()) {
            newTaskFlowId = tfPath;
            globelFileID = tFileId;
            getMainTcodeBind().setValue(null);
            ADFBeanUtils.showPopup(bindDocCheckPopup);
        } else {
//            log.info("|globalNavigation|else path is-----::" + TermsADFUtils.getTaskFlowId(tfPath));
//            this.getDocumentFileDetails(tFileId);
            setDynamicTaskFlowId1(tfPath);
//            this.setParameterMap(this.getParameterMap());
            collapsedPanelSplitter();
        }
    }

    // Open Document from Tree Menu
    public void documentOpenAL(ActionEvent ace) {
        navMode = NAVIGATEFROMMAINTREE;
        String tfName =
            (String) (ace.getComponent().getAttributes().get("TaskFlowName") != null ?
                      ace.getComponent().getAttributes().get("TaskFlowName") : null);
        String tfFileId =
            (String) (ace.getComponent().getAttributes().get("TaskFlowId") != null ?
                      ace.getComponent().getAttributes().get("TaskFlowId") : null);
        String flowPath =
            (String) (ace.getComponent().getAttributes().get("TaskFlowSource") != null ?
                      ace.getComponent().getAttributes().get("TaskFlowSource") : null);
        String fileType =
            (String) (ace.getComponent().getAttributes().get("FileType") != null ?
                      ace.getComponent().getAttributes().get("FileType") : null);

        log.info("|documentOpenAL|Flow Path is--::" + flowPath + ">> File Type-->>" + fileType + ">> File Id--::" +
                 tfFileId);

//        if (flowPath != null) {
//            String tcode = (String) (mainDocModuleBind.getValue());
//            DCBindingContainer bc = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
//            OperationBinding operationbinding = bc.getOperationBinding("Insert_Visit");
//            operationbinding.getParamsMap().put("UserId", TermsADFUtils.getValueFromPageFlowScope("UserId"));
//            operationbinding.getParamsMap().put("TCode", tcode);
//            operationbinding.getParamsMap().put("FileId", tfFileId);
//            operationbinding.getParamsMap().put("FileName", tfName);
//            operationbinding.getParamsMap().put("FilePath", flowPath);
//
//            operationbinding.execute();
//        }
//
//        if (tfFileId.equals("ERP0000001435")) {
//            String TCODE = "QCRT";
//            HashMap<String, Object> tCodeMap = new HashMap<String, Object>();
//            tCodeMap.put("tCode", TCODE);
//            TermsADFUtils.setValueToPageFlowScope("GblTCodeMap", tCodeMap);
//        } else if (tfFileId.equals("ERP0000000435")) {
//            String TCODE = "QCII";
//            HashMap<String, Object> tCodeMap = new HashMap<String, Object>();
//            tCodeMap.put("tCode", TCODE);
//            TermsADFUtils.setValueToPageFlowScope("GblTCodeMap", tCodeMap);
//        }

        if (fileType != null) {
            if (fileType.equalsIgnoreCase(FORM)) {
                if (checkValidation(tfName, tfFileId, flowPath)) {
                    globalNavigation(tfFileId, flowPath);
                }
            } else if (fileType.equalsIgnoreCase(MENU)) {
                if (this.pendingChangeExist()) {
                    newTaskFlowId = null;
                    globelFileID = null;
                    RichPopup.PopupHints hints = new RichPopup.PopupHints();
                    getBindDocCheckPopup().show(hints);
                } else {
                    reloadThePage();
                    // regionBind.refresh(FacesContext.getCurrentInstance());
                    setDynamicTaskFlowId(defaultRegionTF);
                }
            }
        } else {
            ADFBeanUtils.showFacesMessageError("Error", "File type is missing in FileMasterErp.");
        }
    }


    public void collapsedPanelSplitter() {
        // System.out.println(" TermsADFUtils.findComponentInRoot(\"pt_ps1\"):"+TermsADFUtils.findComponentInRoot("pt_ps1"));

        RichPanelSplitter ps = (RichPanelSplitter) TermsADFUtils.findComponentInRoot("pt_ps1");
        //System.err.println("ps.isCollapsed(): " + ps.isCollapsed());
        if (ps != null) {
            ps.setCollapsed(true);
        } else {
            log.info("Panel Splitter not found");
            //System.err.println("Panel Splitter not found");
        }
        AdfFacesContext.getCurrentInstance().addPartialTarget(ps);
    }

    public void mainDocumentTCodeVCL(ValueChangeEvent vce) {
        vce.getComponent().processUpdates(FacesContext.getCurrentInstance());
        String oldTcode = "";
        navMode = NAVIGATEFROMMAINTCODE;
        if (vce != null) {
            System.out.println("|mainDocumentTCodeVCL| TCode Inout ------>>" + vce.getNewValue() + "Getter Value-->>" +
                               getMainDocumentInput());
            DCBindingContainer bc = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
            OperationBinding operationbinding = bc.getOperationBinding("filterDocumentDetails");
            if (operationbinding != null && vce.getNewValue().toString().trim() != null) {
                operationbinding.getParamsMap().put("tCode", vce.getNewValue().toString().trim());
                operationbinding.execute();
            }

            String fileName = (String) (mainDocFileNameBind.getValue());
            String tfFileId = (String) (mainDocFileIdBind.getValue());
            String flowPath = (String) (mainDocFlowPathBind.getValue());
            String tcode = (String) (mainDocModuleBind.getValue());
            System.out.println("In Main WorkArea Page-->>fileName::" + fileName + ":tfFileId::" + tfFileId +
                               ":flowPath::" + flowPath);
            TermsADFUtils.setValueToPageFlowScope("CurrModule", tcode);


            String TCODE = (String) vce.getNewValue();
            TCODE = TCODE.toUpperCase();
            if (null != ADFBeanUtils.resolveExpression("#{pageFlowScope.GblTCodeMap}")) {
                oldTcode = ADFBeanUtils.resolveExpression("#{pageFlowScope.GblTCodeMap}").toString();
            } else {
                oldTcode = "NA";
            }
            HashMap<String, Object> tCodeMap = new HashMap<String, Object>();
            tCodeMap.put("tCode", TCODE);
            TermsADFUtils.setValueToPageFlowScope("GblTCodeMap", tCodeMap);
            tcode = TCODE;
//            String userId = (String) TermsADFUtils.getValueFromPageFlowScope("UserId");
//            String userId1 = (String) ADFBeanUtils.resolveExpression("#{pageFlowScope.UserId}");
            if (fileName != null && flowPath != null && tfFileId != null) {
                if (checkValidation(fileName, tfFileId, flowPath)) {

                    OperationBinding operationbinding1 = bc.getOperationBinding("Insert_Visit");
                    operationbinding1.getParamsMap().put("UserId", TermsADFUtils.getValueFromPageFlowScope("UserId"));
                    operationbinding1.getParamsMap().put("TCode", tcode);
                    operationbinding1.getParamsMap().put("FileId", tfFileId);
                    operationbinding1.getParamsMap().put("FileName", fileName);
                    operationbinding1.getParamsMap().put("FilePath", flowPath);

                    operationbinding1.execute();

                    globalNavigation(tfFileId, flowPath);
                }


                /*                 if (flowPath != null)
                {
                    System.out.println("The tf path is-----::" + TermsADFUtils.getTaskFlowId(flowPath));
                    this.getDocumentFileDetails(tfFileId);
                    setDynamicTaskFlowId(TermsADFUtils.getTaskFlowId(flowPath));
                    this.setParameterMap(this.getParameterMap());
                    row.setAttribute("DocTCodeTrans", null);
                    collapsedPanelSplitter();

                } */
            } else {
                log.info("Something went wrong in File Id and File Name---");
                // ADFUtils.showMessage("Something went wrong in jar file", 2);
            }
            mainTcodeBind.setValue(null);
            AdfFacesContext.getCurrentInstance().addPartialTarget(mainTcodeBind);

            setMainDocumentInput(null);
            AdfFacesContext.getCurrentInstance().addPartialTarget(panelFormBind);

            if (TCODE.equals("QCRT") || TCODE.equals("QCII")) {
                if (oldTcode.equals("{tCode=QCII}") || oldTcode.equals("{tCode=QCRT}")) {
                    ADFBeanUtils.findOperation("onPageLoadExecute").execute();
                    if (this.pendingChangeExist()) {
                        RichPopup.PopupHints hints = new RichPopup.PopupHints();
                        getBindDocCheckPopup().show(hints);
                    } else {
                        reloadThePage();
                        regionBind.refresh(FacesContext.getCurrentInstance());
                        try {
                            this.handleHomeNavigation();
                        } catch (Exception e) {
                            // TODO: Add catch code
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

    }

    public void getDocumentFileDetails(String docId) {
        log.info("|getDocumentFileDetails| DOC_FILE_ID--->>:" + docId);
        TermsADFUtils.setValueToPageFlowScope("DOC_FILE_ID", docId);
        Object[] obj =
            DBConfiguration.callFunctionForMultiResult(Types.VARCHAR, "FILE_USER_PRIV(?,?,?,?)",
                                                       new Object[] { TermsADFUtils.getValueFromPageFlowScope("UnitCode"),
                                                                      docId,
                                                                      TermsADFUtils.getValueFromPageFlowScope("UserId") },
                                                       new Object[] { TermsParam.class },
                                                       new HashMap<String, Object>());

        log.info("Docuemnts Priviledge " + Arrays.toString(obj));
        String result = (String) obj[0];
        log.info("FILE_USER_PRIV User id-----::" + obj[0]);

        HashMap map = (HashMap<String, Object>) obj[1];

        TermsADFUtils.setValueToPageFlowScope("DocMap", map);
        System.out.println("Document map getDocumentFileDetails is " + map);
    }

    public HashMap<String, Object> getParameterMap() {
        HashMap<String, Object> params = new HashMap<>();

        HashMap<String, Object> globalMap = this.getGlobalParamMap();
        HashMap<String, Object> map = this.getDocMap();
        HashMap<String, Object> globalMutableMap = this.getGlobalMutableParamMap();
        HashMap<String, Object> globalTCodeMap = this.getGlobalTCodeParamMap();

        log.info("Global Map getParameterMap=" + this.getGlobalParamMap());
        log.info("Document map getParameterMap=" + this.getDocMap());
        log.info("globalMutableMap getParameterMap=" + this.getGlobalMutableParamMap());

        if (globalMap != null)
            params.putAll(globalMap);

        if (map != null)
            params.putAll(map);

        if (globalMutableMap != null)
            params.putAll(globalMutableMap);

        if (globalTCodeMap != null)
            params.putAll(globalTCodeMap);

        log.info("Total params getParameterMap=" + params);

        return params;
    }

    public HashMap<String, Object> getGlobalTCodeParamMap() {
        return (HashMap<String, Object>) TermsADFUtils.getValueFromPageFlowScope("GblTCodeMap");
    }

    public HashMap<String, Object> getGlobalParamMap() {
        return (HashMap<String, Object>) TermsADFUtils.getValueFromPageFlowScope("GblMap");
    }

    public HashMap<String, Object> getGlobalMutableParamMap() {
        return (HashMap<String, Object>) TermsADFUtils.getValueFromPageFlowScope("GblMutableMap");
    }

    public HashMap<String, Object> getDocMap() {
        Object object = TermsADFUtils.getValueFromPageFlowScope("DocMap");
        if (object == null) {
            return new HashMap<>();
        } else {
            return (HashMap<String, Object>) object;
        }
    }

    private boolean pendingChangeExist() {
//        log.info(" pendingChangeExist taskFlowId:" + taskFlowId + " mainWorkAreaTaskFlowId:" + defaultRegionTF +
//                 " taskFlowId.equalsIgnoreCase(mainWorkAreaTaskFlowId):" +
//                 taskFlowId.equalsIgnoreCase(defaultRegionTF));
//
//        if (!taskFlowId.equalsIgnoreCase(defaultRegionTF)) {
//            ControllerContext cctx = ControllerContext.getInstance();
//            return cctx.getCurrentRootViewPort().getTaskFlowContext().isDataDirty();
//        } else {
            return false;
//        }
    }

    public static String rollbackPartialTransaction() {
        BindingContext bc = BindingContext.getCurrent();
        DataControlFrame dcf = bc.findDataControlFrame(bc.getCurrentDataControlFrame());
        Collection<DCDataControl> dcCol = dcf.datacontrols();
        for (DCDataControl dc : dcCol) {
            log.info("The Name of Data Control is----->>" + dc.equals(config));
            if (!dc.equals(config)) {
                if (dc.isTransactionDirty()) {
                    try {
                        dc.rollbackTransaction();
                    } catch (Exception e) {
                        // TODO: Add catch code
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    private RichRegion regionBind;

    public void setRegionBind(RichRegion regionBind) {
        this.regionBind = regionBind;
    }

    public RichRegion getRegionBind() {
        return regionBind;
    }


    public static String reloadThePage() {
        rollbackPartialTransaction();
        //        FacesContext fcontext = FacesContext.getCurrentInstance();
        //        String viewId = fcontext.getViewRoot().getViewId();
        //        String actionUrl = fcontext.getApplication().getViewHandler().getActionURL(fcontext, viewId);
        //
        //        try {
        //            ExternalContext eContext = fcontext.getExternalContext();
        //            String resourceUrl = actionUrl;
        //
        //            eContext.redirect(resourceUrl);
        //
        //        } catch (IOException ioe) {
        //            // TODO: Add catch code
        //            ioe.printStackTrace();
        //        }
        return null;
    }

    public boolean checkValidation(String tfFileName, String tfFileId, String tfPath) {
        if (tfFileName == null) {
            ADFBeanUtils.showFacesMessageError("Error", "File Name is missing in FileMasterErp.");
            return false;
        } else if (tfFileId == null) {
            ADFBeanUtils.showFacesMessageError("Error", "File Id is missing in FileMasterErp.");
            return false;
        } else if (tfPath == null) {
            ADFBeanUtils.showFacesMessageError("Error", "File Path is missing in FileMasterErp.");
            return false;
        } else {
            return true;
        }
    }

    public void yesConfirmBtnAL(ActionEvent actionEvent) {
        log.info("|yesConfirmBtnAL|Check Before--->>" +
                 ControllerContext.getInstance().getCurrentViewPort().getTaskFlowContext().getTaskFlowId() +
                 " newTaskFlowId:" + newTaskFlowId + "Nav Mode is---->>" + navMode);

        getMainTcodeBind().setValue(null);
        if (navMode.equalsIgnoreCase(NAVIGATEFROMHOMEBUTTON)) {
            log.info("inside Navigation NAVIGATEFROMHOMEBUTTON landTaskFlowId:");
            reloadThePage();
            regionBind.refresh(FacesContext.getCurrentInstance());
            try {
                this.handleHomeNavigation();
            } catch (Exception e) {
                // TODO: Add catch code
                e.printStackTrace();
            }
        } else if (navMode.equalsIgnoreCase(NAVIGATEFROMCLOSEBUTTON)) {
            log.info("inside Navigation NAVIGATEFROMCLOSEBUTTON");
            setDynamicTaskFlowId(defaultRegionTF);
            reloadThePage();
            regionBind.refresh(FacesContext.getCurrentInstance());
        } else {
            log.info("<<-----New Task flow id---->>:" + newTaskFlowId);
            if (newTaskFlowId != null) {
                reloadThePage();
                this.getDocumentFileDetails(globelFileID);
                //setDynamicTaskFlowId(TermsADFUtils.getTaskFlowId(newTaskFlowId));
                setDynamicTaskFlowId(newTaskFlowId);
                this.setParameterMap(this.getParameterMap());
                collapsedPanelSplitter();

            } else {
                reloadThePage();
                this.getDocumentFileDetails(globelFileID);
                setDynamicTaskFlowId(newTaskFlowId);
                this.setParameterMap(this.getParameterMap());
                collapsedPanelSplitter();
            }
        }
        ADFBeanUtils.findOperation("onPageLoadExecute").execute();
        bindDocCheckPopup.cancel();
    }

    public void noConfirmBtnAL(ActionEvent actionEvent) {
        bindDocCheckPopup.cancel();
    }

    public void setBindDocCheckPopup(RichPopup bindDocCheckPopup) {
        this.bindDocCheckPopup = bindDocCheckPopup;
    }

    public RichPopup getBindDocCheckPopup() {
        return bindDocCheckPopup;
    }

    public void formCloseAL(ActionEvent actionEvent) {
        navMode = NAVIGATEFROMCLOSEBUTTON;
        log.info("Close Link Action Listener--->>" + pendingChangeExist());
        if (this.pendingChangeExist()) {
            RichPopup.PopupHints hints = new RichPopup.PopupHints();
            getBindDocCheckPopup().show(hints);
        } else {
            setDynamicTaskFlowId(defaultRegionTF);
            reloadThePage();
            regionBind.refresh(FacesContext.getCurrentInstance());

        }
        ADFBeanUtils.findOperation("onPageLoadExecute").execute();
    }

    //    public void reportFactoryAL(ActionEvent actionEvent)
    //    {
    //        String repFacPath=(String)TermsADFUtils.getValueFromPageFlowScope("ReportFactoryPath");
    //        System.out.println("Rep Url---->>"+repFacPath);
    //        String url =
    //            "window.open('"+repFacPath+ "','_blank')";
    //        System.out.println("url:" + url);
    //        FacesContext fctx = FacesContext.getCurrentInstance();
    //        ExtendedRenderKitService erks =
    //            Service.getRenderKitService(fctx, ExtendedRenderKitService.class);
    //        erks.addScript(fctx, url);
    //    }


    public void goApprovalPendanyAL(ActionEvent actionEvent) {
        String apprFlow =
            "/WEB-INF/bspl/vendor/profile/ui/flow/VendorOptionsTF.xml#VendorOptionsTF";
        if (apprFlow != null) {
            try {
                this.handleNavigationMainWorkarea();
            } catch (Exception e) {
                e.printStackTrace();
            }
            reloadThePage();

            this.setDynamicTaskFlowId(apprFlow);
            this.setParameterMap(this.getParameterMap());
        }
    }

    public void goDashboardAL(ActionEvent actionEvent) {
        String dashFlow = "/WEB-INF/terms/terms/setup/ui/workarea/flow/LandingGraphFlow.xml#LandingGraphFlow";
        if (dashFlow != null) {
            try {
                this.handleNavigationMainWorkarea();
            } catch (Exception e) {
                e.printStackTrace();
            }
            reloadThePage();

            this.setDynamicTaskFlowId(dashFlow);
            this.setParameterMap(this.getParameterMap());
        }
    }

    public void goDocumentPendingListAL(ActionEvent actionEvent) {
        String docPendFlow =
            "/WEB-INF/terms/terms/setup/ui/workarea/flow/DocumentPendingListFlow.xml#DocumentPendingListFlow";
        if (docPendFlow != null) {
            try {
                this.handleNavigationMainWorkarea();
            } catch (Exception e) {
                e.printStackTrace();
            }
            reloadThePage();

            this.setDynamicTaskFlowId(docPendFlow);
            this.setParameterMap(this.getParameterMap());
        }
    }

    public void setMainDocFileNameBind(RichInputText mainDocFileNameBind) {
        this.mainDocFileNameBind = mainDocFileNameBind;
    }

    public RichInputText getMainDocFileNameBind() {
        return mainDocFileNameBind;
    }

    public void setMainDocFileIdBind(RichInputText mainDocFileIdBind) {
        this.mainDocFileIdBind = mainDocFileIdBind;
    }

    public RichInputText getMainDocFileIdBind() {
        return mainDocFileIdBind;
    }

    public void setMainDocFlowPathBind(RichInputText mainDocFlowPathBind) {
        this.mainDocFlowPathBind = mainDocFlowPathBind;
    }

    public RichInputText getMainDocFlowPathBind() {
        return mainDocFlowPathBind;
    }

    public void setMainDocModuleBind(RichInputText mainDocModuleBind) {
        this.mainDocModuleBind = mainDocModuleBind;
    }

    public RichInputText getMainDocModuleBind() {
        return mainDocModuleBind;
    }

    public List<SelectItem> onItems(String employee) {
        List<SelectItem> empList = new ArrayList<SelectItem>();
        empList.clear();
        Row[] rows = null;
        //Call the AM method by passing the user entered value to AM method
        DCBindingContainer bc = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
        OperationBinding operationbinding = bc.getOperationBinding("findByDocumentCode");
        if (operationbinding != null && employee != null) {
            operationbinding.getParamsMap().put("tCode", employee);
            //Execute the Am Method and returns the rows
            rows = (Row[]) operationbinding.execute();
        }
        log.info("|onItems|Begin|Bean row------>>" + rows.length + " TCodelist:" + empList.size());
        String key = null;
        String name = null;
        if (rows != null) {
            for (Row row : rows) {
                //It gives name and Id in the suggested Items
                key = (String) row.getAttribute("TCode");
                name = (String) row.getAttribute("ForSearch");
                empList.add(new SelectItem(key, name));
            }
        }
        log.info("|End|onItems|Bean row------>>" + rows.length + " TCodelist:" + empList.size());
        return empList;
    }


    public static String reloadHomePage() {
        rollbackPartialTransaction();
        FacesContext fcontext = FacesContext.getCurrentInstance();
        String viewId = fcontext.getViewRoot().getViewId();
        String actionUrl = fcontext.getApplication().getViewHandler().getActionURL(fcontext, viewId);

        try {
            ExternalContext eContext = fcontext.getExternalContext();
            String resourceUrl = actionUrl;

            eContext.redirect(resourceUrl);

        } catch (IOException ioe) {
            // TODO: Add catch code
            ioe.printStackTrace();
        }
        return null;
    }

    public void setMainTcodeBind(RichInputComboboxListOfValues mainTcodeBind) {
        this.mainTcodeBind = mainTcodeBind;
    }

    public RichInputComboboxListOfValues getMainTcodeBind() {
        return mainTcodeBind;
    }

    public void setMainDocumentInput(String mainDocumentInput) {
        this.mainDocumentInput = mainDocumentInput;
    }

    public String getMainDocumentInput() {
        return mainDocumentInput;
    }

    public void setPanelFormBind(RichPanelFormLayout panelFormBind) {
        this.panelFormBind = panelFormBind;
    }

    public RichPanelFormLayout getPanelFormBind() {
        return panelFormBind;
    }

    public void passwordReset(ActionEvent actionEvent) {
        try {

            String TCODE = "RESET";
            HashMap<String, Object> tCodeMap = new HashMap<String, Object>();
            tCodeMap.put("tCode", TCODE);
            TermsADFUtils.setValueToPageFlowScope("GblTCodeMap", tCodeMap);
            globalNavigation1("ERP0000001919", "/WEB-INF/PassResetTF.xml#PassResetTF");
        } catch (Exception ex) {
            ADFBeanUtils.showFacesMessageError("Error in password reset", ex.toString());
        }

    }

    public void globalNavigation1(String tFileId, String tfPath) {
        //        log.info("|globalNavigation|Taskflow path---" + tfPath);
        //
        //            log.info("|globalNavigation|else path is-----::" + TermsADFUtils.getTaskFlowId(tfPath));
        //            this.getDocumentFileDetails(tFileId);
        //            setDynamicTaskFlowId(tfPath);
        //            this.setParameterMap(this.getParameterMap());
        //           //collapsedPanelSplitter();
        String tcode = "RESET";

        TermsADFUtils.setValueToPageFlowScope("CurrModule", tcode);
        HashMap<String, Object> tCodeMap = new HashMap<String, Object>();
        tCodeMap.put("tCode", tcode);
        TermsADFUtils.setValueToPageFlowScope("GblTCodeMap", tCodeMap);

        this.handleNavigationMainWorkarea();

        log.info("|homeDocumentCodeVCL|The tf path is-----::" + TermsADFUtils.getTaskFlowId(tfPath));
        // this.getDocumentFileDetails(tFileId);
        setDynamicTaskFlowId(tfPath);
        this.setParameterMap(this.getParameterMap());
        reloadHomePage();
    }


    public void setPopMostVisit(RichPopup popMostVisit) {
        this.popMostVisit = popMostVisit;
    }

    public RichPopup getPopMostVisit() {
        return popMostVisit;
    }

    public void LoadFavourites(ActionEvent actionEvent) {
        System.out.println("load...");
        loadPopup();
    }

    public void loadPopup() {

        try {


            DCBindingContainer bc = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
            DCIteratorBinding dcIteratorBinding = bc.findIteratorBinding("UserMostVisitVO1Iterator");
            dcIteratorBinding.executeQuery();
            //            AdfFacesContext.getCurrentInstance().addPartialTarget(t3);
            //            RichPopup.PopupHints hints = new RichPopup.PopupHints();
            //            hints.add(RichPopup.PopupHints.HintTypes.HINT_ALIGN_ID, this.alignpopup);
            //            hints.add(RichPopup.PopupHints.HintTypes.HINT_ALIGN, RichPopup.PopupHints.AlignTypes.ALIGN_AFTER_START);
            //            popMostVisit.show(hints);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAlignpopup(RichPanelGroupLayout alignpopup) {
        this.alignpopup = alignpopup;
    }

    public RichPanelGroupLayout getAlignpopup() {
        return alignpopup;
    }


    public void Initialize_Screen(ActionEvent actionEvent) {

        Row row = (Row) TermsADFUtils.evaluateEL("#{bindings.UserMostVisitVO1Iterator.currentRow}");
        String fileName = (String) row.getAttribute("FileName");
        String tfFileId = (String) row.getAttribute("FileId");
        String flowPath = (String) row.getAttribute("FlowPath");
        String tcode = (String) row.getAttribute("Tcode");
        log.info("In Landing Page-->>fileName::" + fileName + ":tfFileId::" + tfFileId + ":flowPath::" + flowPath);

        DCBindingContainer bc = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
        OperationBinding operationbinding = bc.getOperationBinding("Insert_Visit");
        operationbinding.getParamsMap().put("UserId", TermsADFUtils.getValueFromPageFlowScope("UserId"));
        operationbinding.getParamsMap().put("TCode", tcode);
        operationbinding.getParamsMap().put("FileId", tfFileId);
        operationbinding.getParamsMap().put("FileName", fileName);
        operationbinding.getParamsMap().put("FilePath", flowPath);

        operationbinding.execute();

        TermsADFUtils.setValueToPageFlowScope("CurrModule", tcode);
        HashMap<String, Object> tCodeMap = new HashMap<String, Object>();
        tCodeMap.put("tCode", tcode);
        TermsADFUtils.setValueToPageFlowScope("GblTCodeMap", tCodeMap);
        if (fileName != null && flowPath != null && tfFileId != null) {
            if (flowPath != null) {
                try {
                    this.handleNavigationMainWorkarea();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                log.info("|homeDocumentCodeVCL|The tf path is-----::" + TermsADFUtils.getTaskFlowId(flowPath));
                this.getDocumentFileDetails(tfFileId);
                setDynamicTaskFlowId(flowPath);
                this.setParameterMap(this.getParameterMap());
                row.setAttribute("Tcode", null);

                reloadHomePage();
            }

        } else {
            log.info("Something went wrong in File Id and File Name---");
        }
    }

    public TaskFlowId getDynamicTaskFlowId1() {
        return TaskFlowId.parse(taskFlowId1);
    }

    public void setDynamicTaskFlowId1(String taskFlowId) {
        this.taskFlowId1 = taskFlowId;
    }
}
