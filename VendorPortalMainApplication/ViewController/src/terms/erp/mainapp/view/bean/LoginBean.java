package terms.erp.mainapp.view.bean;


import VendorProfile.view.bean.ADFUtils;

import adf.utils.exception.ADFUtilsException;

import groovy.json.JsonSlurper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;

import javax.faces.event.ActionEvent;

import java.io.IOException;


import java.io.InputStreamReader;
import java.io.OutputStream;

import java.lang.reflect.Method;

import java.math.BigDecimal;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;

import java.net.URL;

import java.security.SecureRandom;

import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;


import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Properties;

import java.util.Random;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import java.util.ResourceBundle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.event.ValueChangeEvent;

import javax.faces.validator.ValidatorException;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import javax.mail.internet.MimeMultipart;

import javax.mail.util.ByteArrayDataSource;

import javax.naming.InitialContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.sql.DataSource;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputComboboxListOfValues;
import oracle.adf.view.rich.component.rich.input.RichInputFile;
import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.input.RichSelectOneRadio;
import oracle.adf.view.rich.component.rich.layout.RichPanelFormLayout;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.component.rich.layout.RichPanelHeader;
import oracle.adf.view.rich.component.rich.layout.RichPanelTabbed;
import oracle.adf.view.rich.component.rich.layout.RichShowDetailItem;
import oracle.adf.view.rich.component.rich.nav.RichButton;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.output.RichImage;
import oracle.adf.view.rich.component.rich.output.RichOutputFormatted;
import oracle.adf.view.rich.component.rich.output.RichOutputText;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.adf.view.rich.event.DialogEvent;

import oracle.adf.view.rich.event.PopupCanceledEvent;

import oracle.adf.view.rich.util.ResetUtils;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import oracle.javatools.resourcebundle.ResourceBundleManagerRT;

import oracle.jbo.JboException;
import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewObject;
import oracle.jbo.ViewCriteria;
import oracle.jbo.ViewCriteriaRow;
import oracle.jbo.Row;
import oracle.jbo.client.Configuration;
import oracle.jbo.domain.Timestamp;

import oracle.jbo.server.ApplicationModuleImpl;

import oracle.jdbc.internal.OracleTypes;

import org.apache.myfaces.trinidad.event.AttributeChangeEvent;
import org.apache.myfaces.trinidad.event.DisclosureEvent;

import org.apache.myfaces.trinidad.model.UploadedFile;

import terms.erp.mainapp.model.helper.FinYear;
import terms.erp.mainapp.model.helper.ProcedureResult;
import terms.erp.mainapp.model.helper.TermsParam;
import terms.erp.mainapp.model.helper.Unit;
import terms.erp.mainapp.model.services.TermsMainAppAMImpl;
import terms.erp.mainapp.view.utils.ADFBeanUtils;
import terms.erp.mainapp.view.utils.TermsADFUtils;
import terms.erp.mainapp.view.utils.DBConfiguration;


public class LoginBean implements Serializable {
    public static String user;
    private RichInputText bindUserName;
    private RichInputComboboxListOfValues bindUnitCode;
    private RichInputText bindUserPassword;
    private RichInputComboboxListOfValues bindFinYear;
    private RichInputText bindUnitName;
    private RichInputText bindContactPerson;
    private RichInputComboboxListOfValues bindVendFlg;
    private RichInputText bindPanNo;
    private RichInputText bindGstNo;
    private RichInputText bindFirmName;
    private RichInputText bindMobileNo;
    private RichInputText bindPinNo;
    private RichInputComboboxListOfValues bindCountryDesc;
    private RichInputText bindCountry;
    private RichButton saveBtnSignup;
    Properties emailProperties;
    MimeMessage emailMessage;
    private RichInputText bindEmail;
    private static String otpCode = null;
    private long otpGeneratedTime;
    private RichPopup bindPopOtp;
    private RichInputText bindInputOtp;
    private RichOutputFormatted bindMsgOutPut;
    private RichSelectBooleanCheckbox bindChkBox;
    private RichInputComboboxListOfValues bindVendTyp;
    private RichInputText bindAddress;
    private RichInputComboboxListOfValues bindState;
    private RichInputComboboxListOfValues bindDistrict;
    private RichInputText bindPhone;
    private RichInputText bindWebsite;
    private RichInputText bindFax;
    private RichPanelHeader mainPanelHeader;
    private RichOutputFormatted bindSignUpSuccessMsg;
    private RichShowDetailItem bindGeneralTab;
    private RichShowDetailItem bindAddressTab;
    private RichShowDetailItem bindContactTab;
    private RichShowDetailItem bindServiceTab;
    private RichButton bindPreviousBtn;
    private RichButton bindNextBtn;
    private RichPanelTabbed bindpanelTab;
    private RichOutputFormatted bindMsgRegId;
    private RichPanelGroupLayout createLoginPanel;
    private RichPanelGroupLayout tempLoginPanel;
    private RichButton bindCreateLoginBtn;
    private RichPanelHeader createLoginHeader;
    private RichInputText bindRegistrationId;
    private RichInputText bindContactMiddle;
    private RichInputText bindContactLast;
    private RichInputText bindStateDesc;
    private RichInputText bindDistrictDesc;
    private RichInputText bindUserLogin;
    private RichInputText bindPassword;
    private RichInputText bindCpassword;
    private RichInputText bindRegFlag;
    private RichInputText bindVendorFlag;
    private RichInputText bindVendorId;
    private RichInputText bindAadhar;
    private RichInputComboboxListOfValues bindCity;
    private RichInputText bindCityDesc;
    private RichInputText bindOtherCity;
    private RichTable bindDocTable;
    private RichInputFile bindInputFile;
    private RichInputText pathBinding;
    private RichSelectBooleanCheckbox bindReviewFlg;
    private RichPanelHeader bindHindiPanel;
    private RichPanelHeader bindEnglishPanel;
    private RichSelectOneRadio bindRegFlg;
    private RichSelectBooleanCheckbox bindDataProcessFlg;
    private RichSelectBooleanCheckbox bindTranAlertFlg;
    private RichSelectBooleanCheckbox bindEmailAlertFlg;
    private RichSelectBooleanCheckbox bindWAAlertFlg;
    private RichSelectBooleanCheckbox bindCookiesFlg;
    private RichSelectBooleanCheckbox bindAgeConfirmFlg;
    private RichSelectBooleanCheckbox bindDProcessEngFlg;
    private RichSelectBooleanCheckbox bindTranAlertEngFlg;
    private RichSelectBooleanCheckbox bindEmailAlertEngFlg;
    private RichSelectBooleanCheckbox bindWAEngFlg;
    private RichSelectBooleanCheckbox bindCookiesEngFlg;
    private RichSelectBooleanCheckbox bindAgeAlertEngFlg;
    private RichSelectOneChoice bindNoticeLanFlg;
    private RichPanelHeader bindConsentMainHeader;
    private RichInputText bindNoticeLanR;
    private RichInputText bindProDataR;
    private RichInputText bindRecTranR;
    private RichInputText bindEmailR;
    private RichInputText bindWpR;
    private RichInputText bindCookFlgR;
    private RichInputText bindAgeFlgR;
    private RichPanelHeader bindKycMainPanel;
    private RichPanelHeader bindBussDtlMainHeader;
    private RichPanelHeader bindPersonalDtlMainHdr;
    private RichCommandButton bindReviewCancelBtn;
    private RichPanelHeader bindReviewMainPanelHdr;
    private RichInputText bindRegEmailId;
    private RichInputText bindEmailOtp;
    private RichPanelFormLayout bindUserNameForm;
    private RichPanelFormLayout bindEmailOtpForm;
    private RichSelectOneRadio bindLoginMode;
    private String verificationCode;
    private boolean captchaVerified = false;


    public LoginBean() {
    }

    private Date licncDate;

    public void setLicncDate(Date licncDate) {
        this.licncDate = licncDate;
    }

    public Date getLicncDate() {
        return licncDate;
    }

    /**
     * This is used for nevigation
     * */
    private String linkAction;

    public void setLinkAction(String linkAction) {
        this.linkAction = linkAction;
    }

    public String getLinkAction() {
        // setTaskflowSpec("D");
        //System.out.println("inside getLinkAction() :::" + getTaskflowSpec());
        return linkAction;
    }

    /**
     * Created By:Aakash Kumar
     * Created On:08-08-2021
     * Remarks:Authenticate Login User for the Application.
     */

    public void doLoginAL(ActionEvent actionEvent) {

        if (bindLoginMode.getValue().toString().equals("P")) {
            if (this.getBindUserName().getValue() == null ||
                this.getBindUserName().getValue().toString().trim().length() == 0) {
                FacesContext.getCurrentInstance().addMessage(this.getBindUserName().getClientId(),
                                                             TermsADFUtils.showErrorMessage("Mandatory",
                                                                                            "Please Enter User Name"));
                return;
            }

            if (this.getBindUserPassword().getValue() == null ||
                this.getBindUserPassword().getValue().toString().trim().length() == 0) {
                FacesContext.getCurrentInstance().addMessage(this.getBindUserPassword().getClientId(),
                                                             TermsADFUtils.showErrorMessage("Mandatory",
                                                                                            "Please Enter Password"));
                return;
            }

            if (!verifyCaptcha()) {
                this.getBindUserName().setValue("");
                this.getBindUserPassword().setValue("");
                //                FacesContext.getCurrentInstance().addMessage(null,
                //                                                             new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                //                                                                              "Please verify CAPTCHA first."));
                return;
            }


            //            if (this.getBindUnitCode().getValue() == null ||
            //                this.getBindUnitCode().getValue().toString().trim().length() == 0) {
            //                FacesContext.getCurrentInstance().addMessage(this.getBindUnitCode().getClientId(),
            //                                                             TermsADFUtils.showErrorMessage("Mandatory",
            //                                                                                           "Please Enter Unit Code"));
            //                return;
            //            }


            //            if (this.getBindFinYear().getValue() == null ||
            //                this.getBindFinYear().getValue().toString().trim().length() == 0) {
            //                FacesContext.getCurrentInstance().addMessage(this.getBindFinYear().getClientId(),
            //                                                             TermsADFUtils.showErrorMessage("Mandatory",
            //                                                                                           "Please Enter Financial Year"));
            //                return;
            //            }

            //            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

            String res =
                this.authenticateUser(this.getBindUserName().getValue().toString().trim(),
                                      this.getBindUserPassword().getValue().toString().trim());
            System.out.println("|authenticate User| function value : " + res);

            if (res.equals("S")) {
                ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
                HttpServletResponse response = (HttpServletResponse) ectx.getResponse();

                String url = ectx.getRequestContextPath() + "/faces/MainWorkarea.jspx";
                System.out.println("Landing page Url is-----:" + url);

                try {
                    response.sendRedirect(url);
                    FacesContext.getCurrentInstance().responseComplete();

                    //                    DCBindingContainer bc = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
                    //                    OperationBinding operationbinding = bc.getOperationBinding("Visit_Limit");
                    //                    operationbinding.getParamsMap().put("UserId", TermsADFUtils.getValueFromPageFlowScope("UserId"));
                    //                    operationbinding.execute();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (res.equals("U")) {
                FacesContext.getCurrentInstance().addMessage(null,
                                                             TermsADFUtils.showErrorMessage("Login Failed",
                                                                                            "Username not found."));
                return;
            } else if (res.equals("P")) {
                FacesContext.getCurrentInstance().addMessage(null,
                                                             TermsADFUtils.showErrorMessage("Login Failed",
                                                                                            "Password not match."));
                return;
            } else {

                // this.setLinkAction("goToDashbord");
            }
        } else if (bindLoginMode.getValue().toString().equals("E")) {
            if (this.getBindRegEmailId().getValue() == null ||
                this.getBindRegEmailId().getValue().toString().trim().length() == 0) {
                FacesContext.getCurrentInstance().addMessage(this.getBindRegEmailId().getClientId(),
                                                             TermsADFUtils.showErrorMessage("Mandatory",
                                                                                            "Please Enter Registered Email-Id"));
                return;
            }

            if (this.getBindEmailOtp().getValue() == null ||
                this.getBindEmailOtp().getValue().toString().trim().length() == 0) {
                FacesContext.getCurrentInstance().addMessage(this.getBindEmailOtp().getClientId(),
                                                             TermsADFUtils.showErrorMessage("Mandatory",
                                                                                            "Please Enter Registered Email-Id"));
                return;
            }

            if (!verifyCaptcha()) {
                this.getBindRegEmailId().setValue("");
                this.getBindEmailOtp().setValue("");
                //                FacesContext.getCurrentInstance().addMessage(null,
                //                                                             new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                //                                                                              "Please verify CAPTCHA first."));
                return;
            }

            String email = this.getBindRegEmailId().getValue().toString();
            String otp = this.getBindEmailOtp().getValue().toString();
            if (verifyEmailOtp(email, otp).equals("S")) {
                System.out.println("Authentic...");
                
                String res =
                    this.authenticateUserEmail(this.getBindRegEmailId().getValue().toString().trim());
                System.out.println("|authenticate User| function value : " + res);

                if (res.equals("S")) {
                    ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
                    HttpServletResponse response = (HttpServletResponse) ectx.getResponse();

                    String url = ectx.getRequestContextPath() + "/faces/MainWorkarea.jspx";
                    System.out.println("Landing page Url is-----:" + url);

                    try {
                        response.sendRedirect(url);
                        FacesContext.getCurrentInstance().responseComplete();

                        //                    DCBindingContainer bc = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
                        //                    OperationBinding operationbinding = bc.getOperationBinding("Visit_Limit");
                        //                    operationbinding.getParamsMap().put("UserId", TermsADFUtils.getValueFromPageFlowScope("UserId"));
                        //                    operationbinding.execute();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } 
                
                
            } else {
                System.out.println("Not Authentic...");
            }
        }
    }

    public String verifyEmailOtp(String Email, String Otp) {
        if (chkEmailExist(Email).equals("N")) {
            ADFUtils.showMessage("Registered Email* does not exist!", 2);
            bindRegEmailId.setValue("");
            return "F";
        }
        if (otpCode.equals(Otp)) {
            long elapsedTime = System.currentTimeMillis() - otpGeneratedTime;

            // Check expiry
            if (elapsedTime >= OTP_VALIDITY) {

                ADFUtils.showMessage("OTP expired. Please try again!", 2);
                return "F";
            }            
        }
        else{
                ADFUtils.showMessage("Invalid OTP!", 2);
                return "F";
            }
        return "S";
    }

    private String insertLoginInUserSessionData(String userId, String machine, String unit, String httpSessionNo,
                                                String loginParam, String vSession) {
        Object obj = null;
        try {
            obj =
                DBConfiguration.callFunction(Types.VARCHAR, "INS_USER_SESSION(?,?,?,?,?,?)",
                                             new Object[] { userId, machine, unit, httpSessionNo, loginParam,
                                                            vSession });

            System.out.println("|User Session Login Data| result of INS_USER_SESSION:" + obj);
        } catch (Exception e) {
            e.printStackTrace();
            return "Something went wrong when inserting UserSessionData!!";
        }
        return "D";
    }


    private String insertLoginOutUserSessionData(String userId, String machine, String unit, String httpSessionNo,
                                                 String loginParam, String vSession) {
        Object obj = null;
        try {
            obj =
                DBConfiguration.callFunction(Types.VARCHAR, "INS_USER_SESSION(?,?,?,?,?,?)",
                                             new Object[] { userId, machine, unit, httpSessionNo, loginParam,
                                                            vSession });

            System.out.println("|User Session Out Data| result of INS_USER_SESSION:" + obj);
        } catch (Exception e) {
            e.printStackTrace();
            return "Something went wrong when inserting UserSessionData!!";
        }
        return "D";
    }
    
    private String authenticateUser(String name, String password) {
        OperationBinding op = null;
        Object obj[] = new Object[4];
        try {
            //            obj = DBConfiguration.callFunctionForMultiResult(Types.VARCHAR, "authenticate_user(?,?,?)", new Object[]
            //                                                         {
            //                                                     name,password });
            op = ADFBeanUtils.findOperation("authenticateUser");
            op.getParamsMap().put("username", name);
            op.getParamsMap().put("password", password);
            op.execute();
            obj = (Object[]) op.getResult();
            //            System.out.println("result : " + obj[0].toString());
            //            System.out.println("result : " + obj[1].toString());
            System.out.println("result : " + obj[2].toString());
            System.out.println("--authenticateUserPassword--Result---::" + op.getResult());
            if (obj[2].toString().equals("US")) {
                String insertLoginInUserSessionDataResult =
                    this.insertLoginInUserSessionData(obj[1].toString(),
                                                      TermsADFUtils.getClientIpAddress() + "/" +
                                                      TermsADFUtils.getClientCompName(), null,
                                                      TermsADFUtils.getSessionId(), "LI", null);
                System.out.println("insertLoginInUserSessionDataResult:" + insertLoginInUserSessionDataResult);   
                op = ADFBeanUtils.findOperation("authenticateUser");
                op.getParamsMap().put("username", name);
                op.getParamsMap().put("password", password);
                op.execute();
                obj = (Object[]) op.getResult();
                System.out.println(obj[5].toString());
                this.assignDataToPageFlowScope(obj);
                this.assignDataToSession("N");
                return obj[2].toString();
            }
            if (obj[2].toString().equals("S")) {
                this.assignDataToPageFlowScope(obj);
                this.assignDataToSession("N");
                String insertLoginInUserSessionDataResult =
                    this.insertLoginInUserSessionData(obj[1].toString(),
                                                      TermsADFUtils.getClientIpAddress() + "/" +
                                                      TermsADFUtils.getClientCompName(), null,
                                                      TermsADFUtils.getSessionId(), "LI", null);
                System.out.println("insertLoginInUserSessionDataResult:" + insertLoginInUserSessionDataResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Something went wrong when Authentication of User!!";
        }
        return obj[2].toString();
    }


    private String authenticateUserEmail(String email) {
        OperationBinding op = null;
        Object obj[] = new Object[4];
        try {
            //            obj = DBConfiguration.callFunctionForMultiResult(Types.VARCHAR, "authenticate_user(?,?,?)", new Object[]
            //                                                         {
            //                                                     name,password });
            op = ADFBeanUtils.findOperation("authenticateUserEmail");
            op.getParamsMap().put("email", email);
//            op.getParamsMap().put("password", password);
            op.execute();
            obj = (Object[]) op.getResult();
            //            System.out.println("result : " + obj[0].toString());
            //            System.out.println("result : " + obj[1].toString());
            System.out.println("result : " + obj[2].toString());
            System.out.println("--authenticateUserPassword--Result---::" + op.getResult());
            if (obj[2].toString().equals("US")) {
                String insertLoginInUserSessionDataResult =
                    this.insertLoginInUserSessionData(obj[1].toString(),
                                                      TermsADFUtils.getClientIpAddress() + "/" +
                                                      TermsADFUtils.getClientCompName(), null,
                                                      TermsADFUtils.getSessionId(), "LI", null);
                System.out.println("insertLoginInUserSessionDataResult:" + insertLoginInUserSessionDataResult);   
                op = ADFBeanUtils.findOperation("authenticateUserEmail");
                op.getParamsMap().put("email", email);
                op.execute();
                obj = (Object[]) op.getResult();
                System.out.println(obj[5].toString());
                this.assignDataToPageFlowScope(obj);
                this.assignDataToSession("N");
                return obj[2].toString();
            }
            
            if (obj[2].toString().equals("S")) {
                this.assignDataToPageFlowScope(obj);
                this.assignDataToSession("N");
                String insertLoginInUserSessionDataResult =
                    this.insertLoginInUserSessionData(obj[1].toString(),
                                                      TermsADFUtils.getClientIpAddress() + "/" +
                                                      TermsADFUtils.getClientCompName(), null,
                                                      TermsADFUtils.getSessionId(), "LI", null);
                System.out.println("insertLoginInUserSessionDataResult:" + insertLoginInUserSessionDataResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Something went wrong when Authentication of User!!";
        }
        return obj[2].toString();
    }


    public void assignDataToSession(String val) {

        //        System.out.println("object in session:" + Arrays.toString(obj) + " local server value:" + obj[11]);

        TermsADFUtils.setValueToSession("LocalServerLoc", val);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println("AppSessionId--->" + timestamp.getTime());
        TermsADFUtils.setValueToSession("AppSessionId", timestamp.getTime());


        //        TermsADFUtils.setValueToSession("UnitCode", unitCode);
        //        TermsADFUtils.setValueToSession("UnitName", unitName);
        //        TermsADFUtils.setValueToSession("FinYear", finYear);
        //        TermsADFUtils.setValueToSession("UserName", name);
        //        TermsADFUtils.setValueToSession("EmpCode", obj[2]);
        //        TermsADFUtils.setValueToSession("EmpName", obj[3]);
        //        TermsADFUtils.setValueToSession("PwdExpDate", obj[4]);
        //        TermsADFUtils.setValueToSession("PwdAlertDate", obj[5]);
        //        TermsADFUtils.setValueToSession("UnitDetails", obj[6]);
        //
        //        this.setMapToSession((HashMap) obj[7]);
        //        this.setMapToSession((HashMap) obj[8]);
        //        this.setMapToSession((HashMap) obj[9]);
        //        System.out.println((HashMap) obj[7] + "...............OBJ7");
        //        System.out.println((HashMap) obj[8] + "...............OBJ8");
        //        System.out.println((HashMap) obj[9] + "...............OBJ9");

        //        TermsADFUtils.setValueToSession("GLBL_MAP", map);
    }


    public Connection getConnection() throws Exception {
        InitialContext initialContext = new InitialContext();
        DataSource ds = (DataSource) initialContext.lookup("java:comp/env/jdbc/APPLICATIONDBDS");
        java.sql.Connection conn = ds.getConnection();
        return conn;
    }

    public void assignDataToPageFlowScope(Object[] ob) {

        //old parameters
        TermsADFUtils.setValueToPageFlowScope("userName", ob[3]);
        TermsADFUtils.setValueToPageFlowScope("vendorId", ob[0]);
        TermsADFUtils.setValueToPageFlowScope("userId", ob[1]);
        TermsADFUtils.setValueToPageFlowScope("userType", ob[4]);
        TermsADFUtils.setValueToPageFlowScope("sessionId", ob[5]);
        TermsADFUtils.setValueToPageFlowScope("userRole", ob[6]);
        
//        System.out.println(ob[3].toString());
//        System.out.println(ob[0].toString());
//        System.out.println(ob[1].toString());
//        System.out.println(ob[4].toString());
//        System.out.println(ob[5].toString());

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("userName", ob[3]);
        map.put("vendorId", ob[0]);
        map.put("userId", ob[1]);
        map.put("userType", ob[4]);
        map.put("sessionId", ob[5]);
        map.put("userRole", ob[6]);

        TermsADFUtils.setValueToPageFlowScope("GblMap", map);

    }

    public String onLogout() {

        //        String userSetupFlag = (TermsADFUtils.getValueFromSession("setupUserFlag") != null) ? TermsADFUtils.getValueFromSession("setupUserFlag").toString() : null;
        //
        //        if(userSetupFlag != null && userSetupFlag.equalsIgnoreCase("N")){


        String insertLoginOutUserSessionDataResult =
            this.insertLoginOutUserSessionData(TermsADFUtils.getUserId(), null, null, TermsADFUtils.getSessionId(),
                                               "LO", TermsADFUtils.getVSession().toString());
        System.out.println("insertLoginOutUserSessionDataResult:" + insertLoginOutUserSessionDataResult);
        //        }

        ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();

        System.out.println("In logOut");


        HttpServletResponse response = (HttpServletResponse) ectx.getResponse();
        String url = ectx.getRequestContextPath() + "/faces/login.jspx";
        System.out.println("|On Logout| url is ::::::::::: " + url);

        ResourceBundleManagerRT rt = (ResourceBundleManagerRT) ResourceBundleManagerRT.getResourceBundleManager();
        rt.flush();

        ResourceBundle.clearCache(Thread.currentThread().getContextClassLoader());

        HttpSession session = (HttpSession) ectx.getSession(false);
        session.invalidate();

        try {
            response.sendRedirect(url);
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setMapToSession(HashMap map) {
        for (Object key : map.keySet()) {
            TermsADFUtils.setValueToSession((String) key, map.get(key));
        }
    }

    public void setMapToPageFlowScope(HashMap map) {
        for (Object key : map.keySet()) {
            TermsADFUtils.setValueToPageFlowScope((String) key, map.get(key));
        }
    }

    public String licenceOk;

    public void setLicenceOk(String licenceOk) {
        this.licenceOk = licenceOk;
    }

    public String getLicenceOk() {
        if (licenceOk == null) {
            ServletContext ctx = (ServletContext) TermsADFUtils.getExternalContext().getContext();
            Date expdate = (Date) ctx.getAttribute("LicenceExpDate");
            if (new Date().compareTo(expdate) > 0) {
                licenceOk = "N";
            } else {
                licenceOk = "Y";
            }
        }
        return licenceOk;
    }


    public String sessionLogout() {
        ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();

        System.out.println("In Session Out------>>");

        HttpServletResponse response = (HttpServletResponse) ectx.getResponse();
        String url = ectx.getRequestContextPath() + "/faces/login.jspx";
        System.out.println(" From Session url is ::::::::::: " + url);

        HttpSession session = (HttpSession) ectx.getSession(false);
        session.invalidate();

        try {
            response.sendRedirect(url);
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void setBindUserName(RichInputText bindUserName) {
        this.bindUserName = bindUserName;
    }

    public RichInputText getBindUserName() {
        return bindUserName;
    }

    public void setBindUnitCode(RichInputComboboxListOfValues bindUnitCode) {
        this.bindUnitCode = bindUnitCode;
    }

    public RichInputComboboxListOfValues getBindUnitCode() {
        return bindUnitCode;
    }

    public void setBindUserPassword(RichInputText bindUserPassword) {
        this.bindUserPassword = bindUserPassword;
    }

    public RichInputText getBindUserPassword() {
        return bindUserPassword;
    }

    public void setBindFinYear(RichInputComboboxListOfValues bindFinYear) {
        this.bindFinYear = bindFinYear;
    }

    public RichInputComboboxListOfValues getBindFinYear() {
        return bindFinYear;
    }

    public void setBindUnitName(RichInputText bindUnitName) {
        this.bindUnitName = bindUnitName;
    }

    public RichInputText getBindUnitName() {
        return bindUnitName;
    }

    private Boolean findOutLoginUser(String userName) {
        Object obj = null;
        String msg = "";
        if (userName != null) {
            try {
                obj = DBConfiguration.callFunction(Types.VARCHAR, "UX_FIND_USER(?)", new Object[] { userName });

                System.out.println(" result of UX_FIND_USER:" + obj);
                if (obj != null) {
                    String res = obj.toString().substring(0, 1);
                    msg = obj.toString().substring(obj.toString().indexOf("-") + 1);

                    if (res.equalsIgnoreCase("Y")) {
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                msg = e.fillInStackTrace().toString();
                FacesContext.getCurrentInstance().addMessage(null, TermsADFUtils.showErrorMessage("Error", msg));
                return false;
            }
        }
        FacesContext.getCurrentInstance().addMessage(null, TermsADFUtils.showErrorMessage("Error", msg));

        return false;
    }

    public Boolean firstUserLoginValidate(String userId) {
        Object obj = null;
        try {
            obj = DBConfiguration.callFunction(Types.VARCHAR, "UX_FIRST_TIME_LOGIN(?)", new Object[] { userId });

            System.out.println(" result of UX_FIRST_TIME_LOGIN:" + obj);
            if (obj != null) {
                if (obj.toString().equalsIgnoreCase("N")) {
                    System.out.println("Inside UX_FIRST_TIME_LOGIN");
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public void findLoginUserVCE(ValueChangeEvent valueChangeEvent) {
        valueChangeEvent.getComponent().processUpdates(FacesContext.getCurrentInstance());
        System.out.println("Enter in VCE After Process Update");
        String newValue = (String) valueChangeEvent.getNewValue();
        try {
            String username = newValue;
            Boolean findUser = findOutLoginUser(username);
            if (!findUser) {
                FacesMessage message = new FacesMessage("Username not found. Try Again.");
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(bindUserName.getClientId(), message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onChangeUnitlovLanding(ValueChangeEvent valueChangeEvent) {
        valueChangeEvent.getComponent().processUpdates(FacesContext.getCurrentInstance());
        if (valueChangeEvent.getNewValue() != null) {
            Object Obj = null;
            String un_Cd = null;
            HashMap<String, Object> map = new HashMap<String, Object>();
            un_Cd = (String) valueChangeEvent.getNewValue();
            Obj = DBConfiguration.callFunction(Types.VARCHAR, "FUN_PROFCTR_CHK(?)", new Object[] { un_Cd });
            System.out.println("value of obj" + Obj);
            TermsADFUtils.setValueToPageFlowScope("ProfApp", Obj);
            map.put("ProfApp", Obj);
            // for companytype
            Obj = null;
            Obj = DBConfiguration.callFunction(Types.VARCHAR, "FUN_PROFCTR_CHK(?,?)", new Object[] { un_Cd, "C" });
            System.out.println("value of obj" + Obj);
            TermsADFUtils.setValueToSession("CompanyType", Obj);
            TermsADFUtils.setValueToPageFlowScope("CompanyType", Obj);
            map.put("CompanyType", Obj);
            Obj = null;
            Obj = DBConfiguration.callFunction(Types.VARCHAR, "FUN_PROFCTR_CHK(?,?)", new Object[] { un_Cd, "T" });
            System.out.println("value of obj" + Obj);
            TermsADFUtils.setValueToSession("taxType", Obj);
            TermsADFUtils.setValueToPageFlowScope("taxType", Obj);
            map.put("taxType", Obj);
            Obj = null;
            Obj = DBConfiguration.callFunction(Types.VARCHAR, "FUN_PROFCTR_CHK(?,?)", new Object[] { un_Cd, "F" });
            System.out.println("value of obj" + Obj);
            TermsADFUtils.setValueToSession("faReq", Obj);
            TermsADFUtils.setValueToPageFlowScope("faReq", Obj);
            map.put("faReq", Obj);
            Obj = null;
            Obj = DBConfiguration.callFunction(Types.VARCHAR, "FUN_PROFCTR_CHK(?,?)", new Object[] { un_Cd, "B" });
            System.out.println("value of obj" + Obj);

            TermsADFUtils.setValueToSession("brandReq", Obj);
            TermsADFUtils.setValueToPageFlowScope("brandReq", Obj);
            map.put("brandReq", Obj);

            CallableStatement st = null;
            try {
                Connection conn = null;
                Statement stmt = null;
                conn = getConnection();
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select * from DATA_CONTROL_NAME_ADF");
                while (rs.next()) {
                    String adfAMDataControl = rs.getString("DATA_CONTROL_NAME");
                    ApplicationModuleImpl am = (ApplicationModuleImpl) ADFBeanUtils.resolvElDC(adfAMDataControl);
                    String unit = (String) ADFBeanUtils.resolveExpression("#{pageFlowScope.unitCode}");
                    String empcd = (String) ADFBeanUtils.resolveExpression("#{pageFlowScope.empCode}");
                    System.out.println("unit++" + un_Cd + "emp++++" + empcd);
                    if (unit != null && empcd != null) {
                        if (am == null) {
                        } else {
                            am.resetState(2);
                            st = am.getDBTransaction().createCallableStatement("call PACK_GL_PARAM.SET_GL_VAL(?,?)", 0);
                            st.setObject(1, un_Cd);
                            st.setObject(2, empcd);
                            st.executeUpdate();
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (st != null) {
                    try {
                        st.close();
                        st = null;
                    } catch (SQLException e) {
                    }
                }
            }
        }
    }

    public void signUp(ActionEvent actionEvent) {
        try {
            ADFContext.getCurrent().getPageFlowScope().put("sign", "X");
            //            this.assignDataToPageFlowScope("newSignup");
            this.assignDataToSession("N");

            BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();
            OperationBinding operationBinding =
                bindings.getOperationBinding("CreateInsert"); // create portalvendorRegd Row
            Object result = operationBinding.execute();

            //            OperationBinding operationBinding1 =
            //                bindings.getOperationBinding("CreateInsert1"); // create portalvendorRegdAddress Row
            //            Object result1 = operationBinding1.execute();

            ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) ectx.getResponse();

            String url = ectx.getRequestContextPath() + "/faces/SignUp.jspx";
            System.out.println("Landing page Url is-----:" + url);
            response.sendRedirect(url);
            FacesContext.getCurrentInstance().responseComplete();
            //            setDisclosedFirstTab(bindGeneralTab);
            //            chkMode("N");
            //            AdfFacesContext.getCurrentInstance().addPartialTarget(mainPanelHeader);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void backToLogin(ActionEvent actionEvent) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            UIViewRoot root = facesContext.getViewRoot();
            RichInputText inputText = (RichInputText) root.findComponent("it1");
            RichInputText inputText1 = (RichInputText) root.findComponent("it2");
            inputText.setValue(null);
            inputText1.setValue(null);
            RichInputText user = (RichInputText) root.findComponent("it3");
            RichInputText pass = (RichInputText) root.findComponent("it4");
            RichInputText c_pass = (RichInputText) root.findComponent("it6");
            user.setValue(null);
            pass.setValue(null);
            c_pass.setValue(null);

            tempLoginPanel.setVisible(true);
            createLoginPanel.setVisible(false);
            AdfFacesContext.getCurrentInstance().addPartialTarget(tempLoginPanel);
            AdfFacesContext.getCurrentInstance().addPartialTarget(createLoginPanel);

            BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();
            OperationBinding operationBinding =
                bindings.getOperationBinding("Rollback"); // create portalvendormaster Row
            Object result = operationBinding.execute();

            ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) ectx.getResponse();

            String url = ectx.getRequestContextPath() + "/faces/login.jspx";
            System.out.println("login page Url is-----:" + url);
            response.sendRedirect(url);
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void backToLoginPage(ActionEvent actionEvent) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            UIViewRoot root = facesContext.getViewRoot();
            RichInputText inputText = (RichInputText) root.findComponent("it1");
            RichInputText inputText1 = (RichInputText) root.findComponent("it2");
            inputText.setValue(null);
            inputText1.setValue(null);

            ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) ectx.getResponse();

            String url = ectx.getRequestContextPath() + "/faces/login.jspx";
            System.out.println("login page Url is-----:" + url);
            response.sendRedirect(url);
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chkMode(String mode) {
        if (mode.equals("S")) { // Save Record
            bindFirmName.setDisabled(true);
            bindVendTyp.setDisabled(true);
            bindRegFlg.setDisabled(true);
            bindGstNo.setDisabled(true);
            bindPanNo.setDisabled(true);
            bindAadhar.setDisabled(true);
            bindVendFlg.setDisabled(true);
            bindAddress.setDisabled(true);
            bindState.setDisabled(true);
            bindCity.setDisabled(true);
            bindDistrict.setDisabled(true);
            bindPinNo.setDisabled(true);
            bindContactPerson.setDisabled(true);
            bindContactMiddle.setDisabled(true);
            bindContactLast.setDisabled(true);
            bindMobileNo.setDisabled(true);
            bindPhone.setDisabled(true);
            bindEmail.setDisabled(true);
            bindWebsite.setDisabled(true);
            bindFax.setDisabled(true);
            bindChkBox.setDisabled(true);
            saveBtnSignup.setDisabled(true);
            bindSignUpSuccessMsg.setVisible(true);
            bindMsgRegId.setVisible(true);
            saveBtnSignup.setDisabled(true);

        }
        if (mode.equals("N")) { // New Record
            bindFirmName.setDisabled(false);
            bindVendTyp.setDisabled(false);
            bindRegFlg.setDisabled(false);
            bindGstNo.setDisabled(false);
            bindPanNo.setDisabled(false);
            bindAadhar.setDisabled(false);
            bindVendFlg.setDisabled(false);
            bindAddress.setDisabled(false);
            bindState.setDisabled(false);
            bindCity.setDisabled(false);
            bindDistrict.setDisabled(false);
            bindPinNo.setDisabled(false);
            bindContactPerson.setDisabled(false);
            bindContactMiddle.setDisabled(false);
            bindContactLast.setDisabled(false);
            bindMobileNo.setDisabled(false);
            bindPhone.setDisabled(false);
            bindEmail.setDisabled(false);
            bindWebsite.setDisabled(false);
            bindFax.setDisabled(false);
            bindChkBox.setDisabled(false);
            saveBtnSignup.setDisabled(false);
            bindSignUpSuccessMsg.setVisible(false);
            bindMsgRegId.setVisible(false);
        }
    }

    public void cevmodecheck() {
        makeComponentHierarchyReadOnly(this.getBindBussDtlMainHeader(), true);
        makeComponentHierarchyReadOnly(this.getBindPersonalDtlMainHdr(), true);
        makeComponentHierarchyReadOnly(this.getBindKycMainPanel(), true);
        makeComponentHierarchyReadOnly(this.getBindConsentMainHeader(), true);
        makeComponentHierarchyReadOnly(this.getBindReviewMainPanelHdr(), true);
       
    }


    public void saveNewRegistration(ActionEvent actionEvent) {
        try {
            Row row = (Row) ADFBeanUtils.evaluateEL("#{bindings.PortalVendorRegdVO1Iterator.currentRow}");
            System.out.println(row.getAttribute("ReviewFlg"));
            System.out.println(row.getAttribute("BillAddressLine1"));
            if (row.getAttribute("ReviewFlg").equals("Y")) {

                if (validateKycDocs("next") == null) {
                    return;
                }

                if (chkConsentAgreement() == null) {
                    return;
                }

                //                if (row.getAttribute("BillAddressLine1") == null ||
                //                    row.getAttribute("BillAddressLine1").toString().trim().length() == 0) {
                //                    FacesContext.getCurrentInstance().addMessage(this.getBindAddress().getClientId(),
                //                                                                 TermsADFUtils.showInfoMessage("Mandatory",
                //                                                                                               "Please Enter Address Details"));
                //                    return;
                //                }
                //        if (this.getBindVendFlg().getValue() == null ||
                //            this.getBindVendFlg().getValue().toString().trim().length() == 0) {
                //            FacesContext.getCurrentInstance().addMessage(this.getBindVendFlg().getClientId(),
                //                                                         TermsADFUtils.showInfoMessage("Mandatory",
                //                                                                                       "Please Enter Address Details"));
                //            return;
                //        }
                //        if (this.getBindContactPerson().getValue() == null ||
                //            this.getBindContactPerson().getValue().toString().trim().length() == 0) {
                //            FacesContext.getCurrentInstance().addMessage(this.getBindContactPerson().getClientId(),
                //                                                         TermsADFUtils.showInfoMessage("Mandatory",
                //                                                                                       "Please Enter Contact Details"));
                //            return;
                //        }
                ViewObject vendorVO = getAm().getPortalVendorRegdVO1();
                Long vendorId = (Long) vendorVO.getCurrentRow().getAttribute("VendorId");
                AdfFacesContext adfFacesContext = AdfFacesContext.getCurrentInstance();
                //        if (bindRegFlg.getValue().toString().equals("R")) {
                //            vendorVO.getCurrentRow().setAttribute("GstRegNo", bindGstNo.getValue().toString().toUpperCase());
                //        }
                //        vendorVO.getCurrentRow().setAttribute("PanNo", bindPanNo.getValue().toString().toUpperCase());


                OperationBinding op = ADFBeanUtils.findOperation("generateRegistrationId");
                Object res = op.execute();
                System.out.println("Customer Code : " + res.toString());

                vendorVO.getCurrentRow().setAttribute("CustomerCode", res.toString());
                vendorVO.getCurrentRow().setAttribute("Loginname", res.toString());
                vendorVO.getCurrentRow().setAttribute("UserRole", "CA");
                System.out.println(vendorVO.getCurrentRow().getAttribute("CustomerCode"));
                System.out.println(vendorVO.getCurrentRow().getAttribute("Loginname"));

                //                OperationBinding opu = ADFBeanUtils.findOperation("generateUserLogin");
                //                opu.getParamsMap().put("First", bindContactPerson.getValue().toString().toLowerCase());
                //                opu.getParamsMap().put("Last", bindContactLast.getValue().toString().toLowerCase());
                //                Object resu = opu.execute();
                //                System.out.println("User Login : " + resu.toString());


                String password = generate_password(vendorVO.getCurrentRow().getAttribute("Name").toString());
                vendorVO.getCurrentRow().setAttribute("TempPassword", password);
                //                OperationBinding op1 = ADFBeanUtils.findOperation("selectVendorServices");
                //                Object result = op1.execute();
                //                System.out.println("Services insert : " + result);
                //                System.out.println(vendorVO.getCurrentRow().getAttribute("Name"));

                //                BigDecimal lineId = (BigDecimal) vendorVO.getCurrentRow().getAttribute("LineId");
                OperationBinding ope = ADFBeanUtils.findOperation("createProfileData");
                ope.getParamsMap().put("LineId", vendorId);
                ope.getParamsMap().put("UserId", vendorVO.getCurrentRow().getAttribute("CustomerCode"));
                Object reply = ope.execute();
                if (!reply.toString().equals("Y")) {
                    ADFBeanUtils.showFacesMessageInfo("", reply.toString());
                    return;
                }
                if (reply.toString().equals("Y")) {
                    BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();
                    OperationBinding operationBinding =
                        bindings.getOperationBinding("Commit"); // create portalvendormaster Row
//                    Object result1 = operationBinding.execute();
                    ADFBeanUtils.showFacesMessageInfo("", "Customer created successfully!");
                    ADFBeanUtils.showFacesMessageInfo("", "Customer Code : "+res.toString());
                    cevmodecheck();
//                    getBindReviewCancelBtn().setVisible(true);
//                    AdfFacesContext.getCurrentInstance().addPartialTarget(bindReviewCancelBtn);
                   
                }
                /*send email to Vendor for password change*/
                //                    String emailId = getBindEmail().getValue().toString();
                //                    String ServiceDesc = null;
                //                    System.out.println("Vendor Id : " + vendorId);
                //
                //                    if (emailId != null) {
                //                        String MsgBody = ServiceDesc;
                //                        String MsgSubject = "New Registration for Approval";
                //                        String Recipient = emailId;
                //                        StringBuilder MsgBody1 =
                //                            new StringBuilder("<html><body><b><font color='#004d99'>New Registration <font color='#008000'>'" +
                //                                              res.toString() + "'");
                //                        StringBuilder MsgBody2 =
                //                            new StringBuilder("<html><body><br><br><font color='#990000'><b>" + MsgBody +
                //                                              "</b></font></body></html>");
                //                        StringBuilder MsgBody3 =
                //                            new StringBuilder("<html><body><b><br><br><font color='#008000'>UserLogin : </font><font color='#004d99'>'" +
                //                                              resu.toString() +
                //                                              "'</font> <font color='#008000'>Temporary Password : </font><font color='#004d99'>'" +
                //                                              password +
                //                                              "'</font> <font color='#008000'>Change your Temporary Password on Login Screen</font></b></html></body><html>");
                //                        StringBuilder MsgBody4 =
                //                            new StringBuilder("<html><body><b><br><br><font color='#008000'>Please complete your Profile on Vendor Portal for further process. Thanks! </font></b></html></body><html>");
                //                        String finalmsgbody = (MsgBody1.toString() + MsgBody3.toString() + MsgBody4.toString());
                //                        String mailStatus = sendMailMethod(finalmsgbody, MsgSubject, Recipient, "Y", "N", "N");
                //                        System.out.println("Email sent Status :" + mailStatus);
                //
                //
                //                        chkMode("S");
                //                        AdfFacesContext.getCurrentInstance().addPartialTarget(saveBtnSignup);
                //                        AdfFacesContext.getCurrentInstance().addPartialTarget(mainPanelHeader);
                //                        StringBuilder msgEmail =
                //                            new StringBuilder("<html><body><b>Congratulations!! Your request has been submitted successfully.</b></body></html>");
                //                        FacesMessage pageMsg = new FacesMessage(msgEmail.toString());
                //                        pageMsg.setSeverity(FacesMessage.SEVERITY_ERROR);
                //
                //                    } else {
                //                        ADFBeanUtils.showFacesMessageInfo("", "Please select Services");
                //                    }
                //                }
            } else {
                ADFBeanUtils.showFacesMessageInfo("", "Please confirm that the given information is correct.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public String chkEmailExist(String email) {

        if (email != null) {

            ViewObject chkEmailVO = getAm().getCheckEmailExist1();
            chkEmailVO.setNamedWhereClauseParam("bindEmail", email);
            chkEmailVO.executeQuery();
            if (chkEmailVO.getEstimatedRowCount() == 0) {
                return "N";
            }

        }
        return "Y";
    }


    public void validatePAN(ValueChangeEvent e) {
        e.getComponent().processUpdates(FacesContext.getCurrentInstance());
        if (e.getNewValue() != null) {
            String pan = e.getNewValue().toString().toUpperCase();
            System.out.println(e.getNewValue().toString().length());
            int lenght = e.getNewValue().toString().length();
            if (lenght == 10) {
                ViewObject chkPanVO = getAm().getchkDuplicatePanVO1();
                chkPanVO.setNamedWhereClauseParam("bindPan", pan);
                chkPanVO.executeQuery();
                if (chkPanVO.getEstimatedRowCount() >= 1) {
                    bindPanNo.setValue("");
                    AdfFacesContext.getCurrentInstance().addPartialTarget(bindPanNo);
                    System.out.println("PAN no. already exist");
                    ADFBeanUtils.showFacesMessageInfo("", "PAN No. already registered.");
                    return;
                }
            } else {
                bindPanNo.setValue("");
                AdfFacesContext.getCurrentInstance().addPartialTarget(bindPanNo);
                FacesContext.getCurrentInstance().addMessage(this.getBindPanNo().getClientId(),
                                                             TermsADFUtils.showErrorMessage("Mandatory",
                                                                                            "Please Enter 10 digit PAN No."));
            }
        }
    }

    public void validateAadhar(ValueChangeEvent e) {
        e.getComponent().processUpdates(FacesContext.getCurrentInstance());
        String aadhar = e.getNewValue().toString();
        if (e.getNewValue() != null) {
            if (aadhar.length() == 12) {
                if (!isInteger(aadhar)) {
                    bindAadhar.setValue("");
                    AdfFacesContext.getCurrentInstance().addPartialTarget(bindAadhar);
                    ADFBeanUtils.showFacesMessageInfo("", "Aadhar Card in Digits only!");
                    return;
                } else {
                    OperationBinding op = null;
                    op = ADFBeanUtils.findOperation("checkAadharDupliate");
                    op.getParamsMap().put("aadhar", aadhar);
                    op.execute();
                    System.out.println(op.getResult().toString());
                    if (op.getResult().toString().equals("Y")) {
                        bindAadhar.setValue("");
                        AdfFacesContext.getCurrentInstance().addPartialTarget(bindAadhar);
                        ADFBeanUtils.showFacesMessageInfo("", "Aadhar Card already Exist!");
                        return;

                    }
                }
            } else {
                bindAadhar.setValue("");
                AdfFacesContext.getCurrentInstance().addPartialTarget(bindAadhar);
                AdfFacesContext.getCurrentInstance().addPartialTarget(bindAadhar);
                ADFBeanUtils.showFacesMessageInfo("", "Adhaar Card must be of 12 digits");
                return;
            }
        }
    }

    public boolean isInteger(String input) {

        return input != null && input.matches("\\d+");

    }


    public void validateGstNo(ValueChangeEvent e) {
        try {
            e.getComponent().processUpdates(FacesContext.getCurrentInstance());
            System.out.println(e.getNewValue().toString().length());
            if (null != bindRegFlag.getValue() && bindRegFlag.getValue().toString().equals("R")) {
                int lenght = e.getNewValue().toString().length();
                OperationBinding op = null;
                if (lenght == 15) {
                    String gstno = e.getNewValue().toString().subSequence(2, 12).toString().toUpperCase();
                    System.out.println("gstno----->" + gstno);

                    ViewObject chkPanVO = getAm().getchkDuplicatePanVO1();
                    chkPanVO.setNamedWhereClauseParam("bindPan", gstno);
                    chkPanVO.executeQuery();
                    if (chkPanVO.getEstimatedRowCount() >= 1) {
                        bindGstNo.setValue(null);
                        System.out.println("PAN no. already exist");
                        ADFBeanUtils.showFacesMessageInfo("", "GST No. already registered.");
                        return;
                    }

                    String stateCode = e.getNewValue().toString().subSequence(0, 2).toString().toUpperCase();
                    System.out.println(stateCode);
                    {

                        op = ADFBeanUtils.findOperation("setStateFromGst");
                        op.getParamsMap().put("stateCode", stateCode);
                        op.execute();
                        System.out.println(op.getResult().toString());
                        if (op.getResult().toString().equals("Y")) {
                            bindVendFlg.setDisabled(true);
                            bindState.setDisabled(true);
                            AdfFacesContext.getCurrentInstance().addPartialTarget(bindVendFlg);
                            AdfFacesContext.getCurrentInstance().addPartialTarget(bindState);
                        } else {
                            bindGstNo.setValue(null);
                            ADFBeanUtils.showFacesMessageInfo("", "Please enter proper GST No.");
                            return;
                        }
                    }

                    if (gstno != null) {
                        System.out.println("gstno 2 ----->" + gstno);
                        getBindPanNo().setValue(gstno);
                        AdfFacesContext.getCurrentInstance().addPartialTarget(bindPanNo);
                    }

                } else {
                    bindGstNo.setValue("");
                    AdfFacesContext.getCurrentInstance().addPartialTarget(bindGstNo);
                    FacesContext.getCurrentInstance().addMessage(this.getBindGstNo().getClientId(),
                                                                 TermsADFUtils.showErrorMessage("Mandatory",
                                                                                                "Please Enter 15 digit GST No."));
                }
            } else {
                bindGstNo.setValue("");
                AdfFacesContext.getCurrentInstance().addPartialTarget(bindGstNo);
                ADFBeanUtils.showFacesMessageInfo("", "Please select Registration Description.");
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void regFlag(ValueChangeEvent e) {
        e.getComponent().processUpdates(FacesContext.getCurrentInstance());
        System.out.println(bindRegFlag.getValue());
        if (bindRegFlag.getValue().toString().equals("U")) {

            bindGstNo.setValue("");
            bindState.setValue("");
            bindStateDesc.setValue("");
            bindCity.setValue("");
            bindCityDesc.setValue("");
            bindPinNo.setValue("");
            bindAddress.setValue("");

            bindGstNo.setRequired(false);
            bindGstNo.setDisabled(true);
            bindPanNo.setRequired(true);
            bindPanNo.setDisabled(false);
            bindRegFlg.setDisabled(true);

            AdfFacesContext.getCurrentInstance().addPartialTarget(bindState);
            AdfFacesContext.getCurrentInstance().addPartialTarget(bindStateDesc);
            AdfFacesContext.getCurrentInstance().addPartialTarget(bindCity);
            AdfFacesContext.getCurrentInstance().addPartialTarget(bindCityDesc);
            AdfFacesContext.getCurrentInstance().addPartialTarget(bindPinNo);
            AdfFacesContext.getCurrentInstance().addPartialTarget(bindGstNo);
            AdfFacesContext.getCurrentInstance().addPartialTarget(bindPanNo);
            AdfFacesContext.getCurrentInstance().addPartialTarget(bindRegFlg);
        }

    }


    public void firmNameVal(ValueChangeEvent e) {
        e.getComponent().processUpdates(FacesContext.getCurrentInstance());
        String firm = e.getNewValue().toString().trim().toUpperCase();
        System.out.println(firm);
        bindFirmName.setValue(firm);
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindGstNo);

    }


    public static boolean chkIndianMobile(String s) {

        Pattern p = Pattern.compile("^\\d{10}$");
        Matcher m = p.matcher(s);

        return (m.matches());
    }

    public static boolean chkNriMobile(String s) {
        Pattern p = Pattern.compile("^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$");
        Matcher m = p.matcher(s);

        return (m.matches());
    }

    public void chkValidMobile(ValueChangeEvent e) {
        e.getComponent().processUpdates(FacesContext.getCurrentInstance());
        String mobile = e.getNewValue().toString();

        if (chkIndianMobile(mobile)) {
            System.out.println("Valid Mobile No.");
        } else {
            bindMobileNo.setValue("");
            AdfFacesContext.getCurrentInstance().addPartialTarget(bindMobileNo);
            FacesContext.getCurrentInstance().addMessage(this.getBindMobileNo().getClientId(),
                                                         TermsADFUtils.showErrorMessage("Mandatory",
                                                                                        "Please enter valid Mobile No."));
            return;
        }

    }


    public void validatePin(ValueChangeEvent e) {
        e.getComponent().processUpdates(FacesContext.getCurrentInstance());
        System.out.println(e.getNewValue().toString().length());
        int lenght = e.getNewValue().toString().length();
        if (lenght == 6) {

            System.out.println("Valid Pin Code...");

        } else {
            bindPinNo.setValue("");
            AdfFacesContext.getCurrentInstance().addPartialTarget(bindPinNo);
            FacesContext.getCurrentInstance().addMessage(this.getBindPinNo().getClientId(),
                                                         TermsADFUtils.showErrorMessage("Mandatory",
                                                                                        "Please enter 6 digit PIN No."));
        }
    }

    public void vendorFromVal(ValueChangeEvent e) {
        e.getComponent().processUpdates(FacesContext.getCurrentInstance());
        System.out.println(bindVendorFlag.getValue().toString());
        if (bindVendorFlag.getValue().toString().equals("I")) {
            bindCountry.setValue("INDI");
            bindCountryDesc.setValue("INDIA");
        } else {
            bindCountry.setValue("");
            bindCountryDesc.setValue("");
            bindState.setValue("");
            bindStateDesc.setValue("");
            bindCity.setValue("");
            bindCityDesc.setValue("");
            bindDistrict.setValue("");
            bindDistrictDesc.setValue("");
            bindPinNo.setValue("");
        }
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindCountry);
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindCountryDesc);
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindState);
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindStateDesc);
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindCity);
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindCityDesc);
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindDistrict);
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindDistrictDesc);
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindPinNo);
    }


    public static char[] OTP(int len) {
        System.out.println("Generating OTP using random() : ");
        System.out.print("You OTP is : ");

        // Using numeric values
        String numbers = "0123456789";
        // Using random method
        Random rndm_method = new Random();

        char[] otp = new char[len];

        for (int i = 0; i < len; i++) {
            // Use of charAt() method : to get character value
            // Use of nextInt() as it is scanning the value as int
            otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
        }
        return otp;
    }

    public void verifyEmail(ValueChangeEvent e) {
        e.getComponent().processUpdates(FacesContext.getCurrentInstance());
        System.out.println(e.getNewValue().toString());
        String emailId = e.getNewValue().toString();
        if (emailId != null) {
            int otpLen = 4;
            char[] val = OTP(otpLen);
            otpCode = new String(val);
            String MsgBody =
                "OTP for Vendor Registration: " + "<html><body><b><font color='#004d99'>" + otpCode +
                "</font></b></body></html>";
            String MsgSubject = "OTP for Registration on Vendor Portal";
            String Recipient = emailId;
            StringBuilder MsgBody1 =
                new StringBuilder("<html><body><b>Invalid Email-Id. Please check.</b></body></html>");
            //            String mailStatus = "N";
            String mailStatus = sendMailMethod(MsgBody.toString(), MsgSubject, Recipient, "Y", "N", "N");
            //            emailSend("N", "N", "N", "N", "N", "N", "N", "N", "N", "N", null, "N", MsgBody.toString(), "N", "N");
            if (mailStatus.equals("Y")) {
                RichPopup.PopupHints hints = new RichPopup.PopupHints();
                getBindPopOtp().show(hints);
            } else {
                bindEmail.setValue("");
                AdfFacesContext.getCurrentInstance().addPartialTarget(bindEmail);
                StringBuilder msgEmail =
                    new StringBuilder("<html><body><b>Invalid Email-Id. Please check.</b></body></html>");
                FacesMessage pageMsg = new FacesMessage(msgEmail.toString());
                pageMsg.setSeverity(FacesMessage.SEVERITY_ERROR);
            }
        }

        else {
            FacesContext.getCurrentInstance().addMessage(this.getBindEmail().getClientId(),
                                                         TermsADFUtils.showErrorMessage("Mandatory",
                                                                                        "Please enter Email."));
        }
    }


    public static String sendMailMethod(String bodyMsg, String subject, String toUser, String isAnyAtchmnt,
                                        String fileNameNPath, String mailFilename) {

        OperationBinding op1 = ADFBeanUtils.findOperation("InfoFunction");
        Object result = op1.execute();

        System.out.println("Result" + result);

        Map<String, String> mn = (Map<String, String>) result;


        System.out.println("Hello" + mn);

        String mailhost = mn.get("Host");

        String mailport = mn.get("Port");

        String mailauth = mn.get("Auth");

        String mailtls = mn.get("Starttls");

        String mailUsername = mn.get("Username");

        String emailPassword = mn.get("Password");
        ;

        // Setting Properties
        Properties emailProperties = new Properties();
        emailProperties.put("mail.smtp.port", mailport);
        emailProperties.put("mail.smtp.auth", mailauth); //"true"
        emailProperties.put("mail.smtp.starttls.enable", mailtls); //"true"
        emailProperties.put("mail.smtp.host", mailhost);

        //                emailProperties.put("mail.smtp.host", "smtp.office365.com");
        //                emailProperties.put("mail.smtp.port", "587");
        //                emailProperties.put("mail.smtp.auth", "true");
        //                emailProperties.put("mail.smtp.starttls.enable", "true");
        //                emailProperties.put("mail.smtp.starttls.required", "true");
        //                emailProperties.put("mail.smtp.auth.mechanisms", "XOAUTH2");
        //                emailProperties.put("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3");


        //Login Credentials
        final String user = mailUsername; //change accordingly
        final String password = emailPassword; //change accordingly

        //Authenticating...
        Session session = Session.getInstance(emailProperties, new javax.mail.Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        //1) create MimeBodyPart object and set your message content
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(user));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toUser));

            message.setSubject(subject);

            BodyPart messageBody = new MimeBodyPart();

            //messageBody.setText(bodyMsg);
            messageBody.setContent(bodyMsg, "text/html");

            // If there is any attachment to send
            if ("N".equalsIgnoreCase(isAnyAtchmnt)) {
                //2) create new MimeBodyPart object and set DataHandler object to this object
                MimeBodyPart messageBodyPart2 = new MimeBodyPart();


                System.out.println("Exact path--->" + fileNameNPath);
                javax.activation.DataSource source = new FileDataSource(fileNameNPath);
                messageBodyPart2.setDataHandler(new DataHandler(source));
                messageBodyPart2.setFileName(mailFilename);


                //5) create Multipart object and add MimeBodyPart objects to this object
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBody);
                multipart.addBodyPart(messageBodyPart2);

                //6) set the multiplart object to the message object
                message.setContent(multipart);
            }
            //If there is plain eMail- No Attachment
            else {
                message.setContent(bodyMsg, "text/html"); //for a html email
            }
        } catch (MessagingException e) {
        }

        Transport transport = null;


        try {
            transport = session.getTransport("smtp");
        } catch (NoSuchProviderException e) {
            System.out.println("No such Provider Exception");
        }

        try {
            transport.connect(mailhost, user, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

            //            StringBuilder msgEmail = new StringBuilder("<html><body><b>Email Sent</b></body></html>");
            //            FacesMessage pageMsg = new FacesMessage(msgEmail.toString());
            //            pageMsg.setSeverity(FacesMessage.SEVERITY_INFO);
            //            FacesContext.getCurrentInstance().addMessage("No Network Error !", pageMsg);
            System.out.println("Email sent successfully.");
            return "Y";
        } catch (MessagingException e) {
            System.out.println("somthing went wrong-->Messaging Exception" + e.getMessage());
            StringBuilder msgEmail = new StringBuilder("<html><body><b>Something went wrong !</b>");
            msgEmail.append("<p><b>please provide correct parameters</b></p>");
            msgEmail.append(e.getMessage());
            msgEmail.append("</body></html>");
            FacesMessage pageMsg = new FacesMessage(msgEmail.toString());
            pageMsg.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage("No Network Error !", pageMsg);
            return "N";
        }

    }


    public String emailSend(String mailhost, String auth, String Starttls, String mailUsername, String emailPassword,
                            String toUser, String ccUser, String bcc, String subject, String isAnyAtchmapt,
                            byte[] pdfAttachment, String ref_document_number, String bodyMsg, String port,
                            String companyName) {


        OperationBinding op1 = ADFBeanUtils.findOperation("InfoFunction");
        Object result = op1.execute();

        System.out.println("Result" + result);

        Map<String, String> mn = (Map<String, String>) result;


        System.out.println("Hello" + mn);

        mailhost = mn.get("Host");

        port = mn.get("Port");

        auth = mn.get("Auth");

        Starttls = mn.get("Starttls");

        mailUsername = mn.get("Username");

        emailPassword = mn.get("Password");
        ;

        companyName = "BSPL";
        toUser = "sachin@bharuwasolutions.com;sawan.kumar@bharuwasolutions.com;";

        //toUser="shubham.chaudhary@bharuwasolutions.com;raghvendra.singh@bharuwasolutions.com;sawan.kumar@bharuwasolutions.com;";
        //  ccUser = "sawan.kumar@bharuwasolutions.com;rajat.khanduri@bharuwasolutions.com";
        // bcc = "sawan.kumar@bharuwasolutions.com,rajat.khanduri@bharuwasolutions.com";
        //                isAnyAtchmapt="N";
        //                Properties emailProperties = new Properties();
        //                emailProperties.put("mail.smtp.host", mailhost);
        //                emailProperties.put("mail.smtp.auth", auth);
        //                emailProperties.put("mail.smtp.starttls.enable", Starttls);
        Properties emailProperties = new Properties();
        if (companyName.equalsIgnoreCase("BSPL")) {
            emailProperties.put("mail.smtp.host", mailhost);
            emailProperties.put("mail.smtp.port", port);
            emailProperties.put("mail.smtp.auth", "true");
            emailProperties.put("mail.smtp.starttls.enable", "true");
            emailProperties.put("mail.smtp.starttls.required", "true");
            emailProperties.put("mail.smtp.connectiontimeout", "60000");
            emailProperties.put("mail.smtp.timeout", "60000");
            emailProperties.put("mail.smtp.writetimeout", "60000");
        } else {
            emailProperties.put("mail.smtp.host", mailhost);
            emailProperties.put("mail.smtp.port", port);
            emailProperties.put("mail.smtp.auth", "true");
            emailProperties.put("mail.smtp.ssl.enable", "true"); // SSL enabled
            emailProperties.put("mail.smtp.socketFactory.port", port);
            emailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            emailProperties.put("mail.smtp.ssl.trust", mailhost);
            emailProperties.put("mail.smtp.connectiontimeout", "60000"); // 10 sec
            emailProperties.put("mail.smtp.timeout", "60000");
            emailProperties.put("mail.smtp.writetimeout", "60000");
        }
        //Login Credentials
        final String user = mailUsername; //change accordingly
        final String password = emailPassword; //change accordingly
        //Authenticating...
        Session session = Session.getInstance(emailProperties, new javax.mail.Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });
        //1) create MimeBodyPart object and set your message content
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(user));
        } catch (Exception ex) {
            ex.printStackTrace();
            //   WriteErrorLogToFile(ex.toString());
        }
        if (toUser == null || toUser.equalsIgnoreCase("") || toUser.isEmpty()) {

        } else {
            try {
                String[] recipientList = toUser.split(";");
                InternetAddress[] recipientAddress = new InternetAddress[recipientList.length];
                int counter = 0;
                for (String recipient : recipientList) {
                    recipientAddress[counter] = new InternetAddress(recipient.trim());
                    counter++;
                }
                message.setRecipients(Message.RecipientType.TO, recipientAddress);
                //  message.addRecipient(Message.RecipientType.TO, (Address) new InternetAddress(toUser));
                // message.setRecipients(Message.RecipientType.TO, (Address[]) InternetAddress.parse(toUser));
            } catch (Exception ex) {
                ex.printStackTrace();
                //  WriteErrorLogToFile(ex.toString());
            }
        }
        if (ccUser == null || ccUser.equalsIgnoreCase("") || ccUser.isEmpty()) {
        } else {
            try {
                String[] recipientListCC = ccUser.split(";");
                InternetAddress[] recipientAddressCC = new InternetAddress[recipientListCC.length];
                int counterCC = 0;
                for (String recipient : recipientListCC) {
                    recipientAddressCC[counterCC] = new InternetAddress(recipient.trim());
                    counterCC++;
                }
                message.setRecipients(Message.RecipientType.CC, recipientAddressCC);
                //message.setRecipients(Message.RecipientType.CC, (Address[]) InternetAddress.parse(ccUser));
            } catch (Exception ex) {
                ex.printStackTrace();
                // WriteErrorLogToFile(ex.toString());
            }
        }
        if (bcc == null || bcc.equalsIgnoreCase("") || bcc.isEmpty()) {
        } else {
            try {
                String[] recipientListBCC = ccUser.split(";");
                InternetAddress[] recipientAddressBCC = new InternetAddress[recipientListBCC.length];
                int counterBCC = 0;
                for (String recipient : recipientListBCC) {
                    recipientAddressBCC[counterBCC] = new InternetAddress(recipient.trim());
                    counterBCC++;
                }
                message.setRecipients(Message.RecipientType.BCC, recipientAddressBCC);
            } catch (Exception ex) {
                ex.printStackTrace();
                //   WriteErrorLogToFile(ex.toString());
            }
            //message.setRecipients(Message.RecipientType.BCC, (Address[]) InternetAddress.parse(bcc));
        }
        try {
            message.setSubject(subject);
            BodyPart messageBody = new MimeBodyPart();
            // bodyMsg ="<html><body><H5>||OM||</H5></br><p>Dear "+created_by+",</br>  </p><p>Please find the attached here with our Purchase order/Service/Contact order No. (" +po+ ") dated ("+date+")  along with Terms & Conditions. You are requested to go through the same and send us the order confirmation.</p><br/> <br/> <p style=\"color:red;\">** Note it is auto generated mail, do not reply on this email. Please contact buyer, if need any assistance/query**</p></br></br><p>Thanks & Regards</br><b>"+signature+"</b></p></body></html>";
            messageBody.setText(bodyMsg);
            messageBody.setContent(bodyMsg, "text/html");
            // If there is any attachment to send
            if ("Y".equalsIgnoreCase(isAnyAtchmapt)) {
                //2) create new MimeBodyPart object and set DataHandler object to this object
                //                MimeBodyPart messageBodyPart2 = new MimeBodyPart();
                //                System.out.println("Exact path--->" + fileNameNPath);
                //                javax.activation.DataSource source = new FileDataSource(fileNameNPath);
                //                messageBodyPart2.setDataHandler(new DataHandler(source));
                //                messageBodyPart2.setFileName(mailFilename);
                //                //5) create Multipart object and add MimeBodyPart objects to this object
                //                Multipart multipart = new MimeMultipart();
                //                multipart.addBodyPart(messageBody);
                //                multipart.addBodyPart(messageBodyPart2);
                //                //6) set the multiplart object to the message object
                //                message.setContent(multipart);
                MimeBodyPart attachmentPart = new MimeBodyPart();
                //  DataSource source = new ByteArrayDataSource(pdfAttachment, "application/pdf");
                //  attachmentPart.setDataHandler(new DataHandler(source));
                //  attachmentPart.setFileName(ref_document_number + ".pdf");
                // Add message body and attachment to the multipart
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBody);
                multipart.addBodyPart(attachmentPart);
                // Set the multipart content
                message.setContent(multipart);
            }
            //If there is plain eMail- No Attachment
            else {
                message.setContent(bodyMsg, "text/html"); //for a html email
            }
        } catch (MessagingException e) {
            //   WriteErrorLogToFile(e.toString());
            return e.toString();
        }
        Transport transport = null;
        try {
            transport = session.getTransport("smtp");
        } catch (NoSuchProviderException e) {
            System.out.println("No such Provider Exception");
            // WriteErrorLogToFile(e.toString());
            return e.toString();
        }
        try {
            transport.connect(mailhost, user, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            System.out.println("Email sent successfully.");
            return "success";
        } catch (MessagingException e) {
            System.out.println("somthing went wrong-->Messaging Exception" + e);
            //  WriteErrorLogToFile(e.toString());
            return e.toString();
        }
    }

    public void otpDialog(DialogEvent dialogEvent) {
        String inputOtp = bindInputOtp.getValue().toString();
        if (inputOtp.equals(otpCode)) {
            bindInputOtp.setValue("");
            bindMsgOutPut.setVisible(false);
            bindEmail.setDisabled(true);
            StringBuilder msgEmail = new StringBuilder("<html><body><b>OTP Verified successfully!</b></body></html>");
            FacesMessage pageMsg = new FacesMessage(msgEmail.toString());
            pageMsg.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("No Network Error !", pageMsg);
            bindEmail.setDisabled(true);
            AdfFacesContext.getCurrentInstance().addPartialTarget(bindEmail);
        } else {
            bindMsgOutPut.setVisible(true);
            RichPopup.PopupHints hints = new RichPopup.PopupHints();
            getBindPopOtp().show(hints);
        }
    }


    public void cancelPopup(PopupCanceledEvent p) {
        bindInputOtp.setValue("");
        bindEmail.setValue("");
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindInputOtp);
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindEmail);

    }

    private TermsMainAppAMImpl getAm() {
        ELContext elContext = null;
        ValueExpression valueExp = null;
        // logger.info("start:getAm()");
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            Application app = fc.getApplication();
            ExpressionFactory elFactory = app.getExpressionFactory();
            elContext = fc.getELContext();
            valueExp =
                elFactory.createValueExpression(elContext, "#{data.TermsMainAppAMDataControl.dataProvider}",
                                                Object.class);
        } catch (Exception e) {
            //            System.out.println("Error Occured:-" + e);
            //logger.error("getAm():" + e);
        }
        // logger.info("finish:getAm()");
        return (TermsMainAppAMImpl) valueExp.getValue(elContext);
    }

    public void setBindInputOtp(RichInputText bindInputOtp) {
        this.bindInputOtp = bindInputOtp;
    }

    public RichInputText getBindInputOtp() {
        return bindInputOtp;
    }

    public void setBindMsgOutPut(RichOutputFormatted bindMsgOutPut) {
        this.bindMsgOutPut = bindMsgOutPut;
    }

    public RichOutputFormatted getBindMsgOutPut() {
        return bindMsgOutPut;
    }

    public void setBindEmail(RichInputText bindEmail) {
        this.bindEmail = bindEmail;
    }

    public RichInputText getBindEmail() {
        return bindEmail;
    }


    public void setBindPopOtp(RichPopup bindPopOtp) {
        this.bindPopOtp = bindPopOtp;
    }

    public RichPopup getBindPopOtp() {
        return bindPopOtp;
    }


    public void setBindCountryDesc(RichInputComboboxListOfValues bindCountryDesc) {
        this.bindCountryDesc = bindCountryDesc;
    }

    public RichInputComboboxListOfValues getBindCountryDesc() {
        return bindCountryDesc;
    }

    public void setBindCountry(RichInputText bindCountry) {
        this.bindCountry = bindCountry;
    }

    public RichInputText getBindCountry() {
        return bindCountry;
    }

    public void setSaveBtnSignup(RichButton saveBtnSignup) {
        this.saveBtnSignup = saveBtnSignup;
    }

    public RichButton getSaveBtnSignup() {
        return saveBtnSignup;
    }

    public void setBindContactPerson(RichInputText bindContactPerson) {
        this.bindContactPerson = bindContactPerson;
    }

    public RichInputText getBindContactPerson() {
        return bindContactPerson;
    }

    public void setBindVendFlg(RichInputComboboxListOfValues bindVendFlg) {
        this.bindVendFlg = bindVendFlg;
    }

    public RichInputComboboxListOfValues getBindVendFlg() {
        return bindVendFlg;
    }

    public void setBindMobileNo(RichInputText bindMobileNo) {
        this.bindMobileNo = bindMobileNo;
    }

    public RichInputText getBindMobileNo() {
        return bindMobileNo;
    }

    public void setBindPinNo(RichInputText bindPinNo) {
        this.bindPinNo = bindPinNo;
    }

    public RichInputText getBindPinNo() {
        return bindPinNo;
    }

    public void setBindGstNo(RichInputText bindGstNo) {
        this.bindGstNo = bindGstNo;
    }

    public RichInputText getBindGstNo() {
        return bindGstNo;
    }

    public void setBindFirmName(RichInputText bindFirmName) {
        this.bindFirmName = bindFirmName;
    }

    public RichInputText getBindFirmName() {
        return bindFirmName;
    }

    public void setBindPanNo(RichInputText bindPanNo) {
        this.bindPanNo = bindPanNo;
    }

    public RichInputText getBindPanNo() {
        return bindPanNo;
    }


    public void setBindChkBox(RichSelectBooleanCheckbox bindChkBox) {
        this.bindChkBox = bindChkBox;
    }

    public RichSelectBooleanCheckbox getBindChkBox() {
        return bindChkBox;
    }

    public void setBindVendTyp(RichInputComboboxListOfValues bindVendTyp) {
        this.bindVendTyp = bindVendTyp;
    }

    public RichInputComboboxListOfValues getBindVendTyp() {
        return bindVendTyp;
    }

    public void setBindAddress(RichInputText bindAddress) {
        this.bindAddress = bindAddress;
    }

    public RichInputText getBindAddress() {
        return bindAddress;
    }

    public void setBindState(RichInputComboboxListOfValues bindState) {
        this.bindState = bindState;
    }

    public RichInputComboboxListOfValues getBindState() {
        return bindState;
    }

    public void setBindDistrict(RichInputComboboxListOfValues bindDistrict) {
        this.bindDistrict = bindDistrict;
    }

    public RichInputComboboxListOfValues getBindDistrict() {
        return bindDistrict;
    }

    public void setBindPhone(RichInputText bindPhone) {
        this.bindPhone = bindPhone;
    }

    public RichInputText getBindPhone() {
        return bindPhone;
    }

    public void setBindWebsite(RichInputText bindWebsite) {
        this.bindWebsite = bindWebsite;
    }

    public RichInputText getBindWebsite() {
        return bindWebsite;
    }

    public void setBindFax(RichInputText bindFax) {
        this.bindFax = bindFax;
    }

    public RichInputText getBindFax() {
        return bindFax;
    }

    public void setMainPanelHeader(RichPanelHeader mainPanelHeader) {
        this.mainPanelHeader = mainPanelHeader;
    }

    public RichPanelHeader getMainPanelHeader() {
        return mainPanelHeader;
    }

    public void setBindSignUpSuccessMsg(RichOutputFormatted bindSignUpSuccessMsg) {
        this.bindSignUpSuccessMsg = bindSignUpSuccessMsg;
    }

    public RichOutputFormatted getBindSignUpSuccessMsg() {
        return bindSignUpSuccessMsg;
    }

    public void setBindGeneralTab(RichShowDetailItem bindGeneralTab) {
        this.bindGeneralTab = bindGeneralTab;
    }

    public RichShowDetailItem getBindGeneralTab() {
        return bindGeneralTab;
    }

    public void setBindAddressTab(RichShowDetailItem bindAddressTab) {
        this.bindAddressTab = bindAddressTab;
    }

    public RichShowDetailItem getBindAddressTab() {
        return bindAddressTab;
    }

    public void setBindContactTab(RichShowDetailItem bindContactTab) {
        this.bindContactTab = bindContactTab;
    }

    public RichShowDetailItem getBindContactTab() {
        return bindContactTab;
    }

    public void setBindServiceTab(RichShowDetailItem bindServiceTab) {
        this.bindServiceTab = bindServiceTab;
    }

    public RichShowDetailItem getBindServiceTab() {
        return bindServiceTab;
    }

    public void setBindPreviousBtn(RichButton bindPreviousBtn) {
        this.bindPreviousBtn = bindPreviousBtn;
    }

    public RichButton getBindPreviousBtn() {
        return bindPreviousBtn;
    }

    public void setBindNextBtn(RichButton bindNextBtn) {
        this.bindNextBtn = bindNextBtn;
    }

    public RichButton getBindNextBtn() {
        return bindNextBtn;
    }

    public void setDisclosedFirstTab(RichShowDetailItem tabBind) {
        RichPanelTabbed richPanelTabbed = getBindpanelTab();
        for (UIComponent child : richPanelTabbed.getChildren()) {
            RichShowDetailItem sdi = (RichShowDetailItem) child;
            if (sdi.getClientId().equals(tabBind.getClientId())) {
                sdi.setDisclosed(true);
            } else {
                sdi.setDisclosed(false);
            }
        }
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindpanelTab);
    }

    public void nextTabCall(ActionEvent actionEvent) {
        if (bindGeneralTab.isDisclosed()) {
            setDisclosedFirstTab(bindAddressTab);
            bindPreviousBtn.setVisible(true);
            AdfFacesContext.getCurrentInstance().addPartialTarget(bindPreviousBtn);
        } else if (bindAddressTab.isDisclosed()) {
            setDisclosedFirstTab(bindContactTab);
            bindNextBtn.setVisible(true);
            AdfFacesContext.getCurrentInstance().addPartialTarget(bindNextBtn);
        } else if (bindContactTab.isDisclosed()) {
            setDisclosedFirstTab(bindServiceTab);
            bindNextBtn.setVisible(false);
            AdfFacesContext.getCurrentInstance().addPartialTarget(bindNextBtn);
        } else {
            setDisclosedFirstTab(bindGeneralTab);
            bindNextBtn.setVisible(true);
            AdfFacesContext.getCurrentInstance().addPartialTarget(bindNextBtn);
        }
    }

    public void prevTabCall(ActionEvent actionEvent) {
        if (bindAddressTab.isDisclosed()) {
            setDisclosedFirstTab(bindGeneralTab);
            bindPreviousBtn.setVisible(false);
            AdfFacesContext.getCurrentInstance().addPartialTarget(bindPreviousBtn);
        } else if (bindContactTab.isDisclosed()) {
            setDisclosedFirstTab(bindAddressTab);
            bindNextBtn.setVisible(true);
            AdfFacesContext.getCurrentInstance().addPartialTarget(bindNextBtn);
        } else if (bindServiceTab.isDisclosed()) {
            setDisclosedFirstTab(bindContactTab);
            bindNextBtn.setVisible(true);
            AdfFacesContext.getCurrentInstance().addPartialTarget(bindNextBtn);
        } else {
            setDisclosedFirstTab(bindServiceTab);
            bindNextBtn.setVisible(true);
            AdfFacesContext.getCurrentInstance().addPartialTarget(bindNextBtn);
        }
    }

    public void setBindpanelTab(RichPanelTabbed bindpanelTab) {
        this.bindpanelTab = bindpanelTab;
    }

    public RichPanelTabbed getBindpanelTab() {
        return bindpanelTab;
    }

    public void setBindMsgRegId(RichOutputFormatted bindMsgRegId) {
        this.bindMsgRegId = bindMsgRegId;
    }

    public RichOutputFormatted getBindMsgRegId() {
        return bindMsgRegId;
    }

    public void validateTempLogin(ActionEvent actionEvent) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            UIViewRoot root = facesContext.getViewRoot();
            RichInputText inputText = (RichInputText) root.findComponent("it1");
            RichInputText inputText1 = (RichInputText) root.findComponent("it2");

            String temUserId = (String) inputText.getValue();
            String tempPass = (String) inputText1.getValue();
            bindUserLogin.setValue(temUserId);
            OperationBinding op = null;

            System.out.println("Temp User Id : " + temUserId);
            System.out.println("Temp Password : " + tempPass);

            if (temUserId == null) {
                ADFBeanUtils.showFacesMessageInfo("", "Please Enter UserLogin");
                return;
            }

            if (tempPass == null) {
                ADFBeanUtils.showFacesMessageInfo("", "Please Enter Password");
                return;
            }

            op = ADFBeanUtils.findOperation("validateTempId");
            op.getParamsMap().put("user", temUserId);
            op.getParamsMap().put("pass", tempPass);
            op.execute();
            System.out.println("--authenticateUserPassword--Result---::" + op.getResult());
            if (op.getResult().equals("S")) {
                tempLoginPanel.setVisible(false);
                createLoginPanel.setVisible(true);
                bindPassword.setDisabled(false);
                bindCpassword.setDisabled(false);
                bindCreateLoginBtn.setDisabled(false);
                AdfFacesContext.getCurrentInstance().addPartialTarget(tempLoginPanel);
                AdfFacesContext.getCurrentInstance().addPartialTarget(createLoginPanel);
                AdfFacesContext.getCurrentInstance().addPartialTarget(bindPassword);
                AdfFacesContext.getCurrentInstance().addPartialTarget(bindCpassword);
                AdfFacesContext.getCurrentInstance().addPartialTarget(bindCreateLoginBtn);
            } else if (op.getResult().equals("F")) {
                StringBuilder msg =
                    new StringBuilder("<html><body><b>Temporary Id and Password not matched.</b></body></html>");
                ADFBeanUtils.showFacesMessageInfo("", msg.toString());
            } else if (op.getResult().equals("U")) {
                StringBuilder msg = new StringBuilder("<html><body><b>Temporary Id is already used.</b></body></html>");
                ADFBeanUtils.showFacesMessageInfo("", msg.toString());
            }
            AdfFacesContext.getCurrentInstance().addPartialTarget(bindUserLogin);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void setCreateLoginPanel(RichPanelGroupLayout createLoginPanel) {
        this.createLoginPanel = createLoginPanel;
    }

    public RichPanelGroupLayout getCreateLoginPanel() {
        return createLoginPanel;
    }

    public void setTempLoginPanel(RichPanelGroupLayout tempLoginPanel) {
        this.tempLoginPanel = tempLoginPanel;
    }

    public RichPanelGroupLayout getTempLoginPanel() {
        return tempLoginPanel;
    }

    public void gotoCreateLogin(ActionEvent actionEvent) {
        try {
            ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) ectx.getResponse();

            String url = ectx.getRequestContextPath() + "/faces/CreateLogin.jspx";
            System.out.println("Landing page Url is-----:" + url);

            response.sendRedirect(url);
        } catch (IOException e) {
        }
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void createNewLogin(ActionEvent actionEvent) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            UIViewRoot root = facesContext.getViewRoot();
            //            RichInputText user = (RichInputText) root.findComponent("it3");
            RichInputText pass = (RichInputText) root.findComponent("it4");
            RichInputText c_pass = (RichInputText) root.findComponent("it6");
            //            String username = (String) user.getValue();
            String password = (String) pass.getValue();
            String c_password = (String) c_pass.getValue();
            //            if (username == null) {
            //                ADFBeanUtils.showFacesMessageInfo("", "Please Enter Username");
            //                return;
            //            }

            if (password == null) {
                ADFBeanUtils.showFacesMessageInfo("", "Please Enter Password");
            }
            if (c_password == null) {
                ADFBeanUtils.showFacesMessageInfo("", "Please Enter Confirm Password");
                return;
            }
            if (password.equals(c_password)) {
                OperationBinding op = null;

                //                System.out.println("UserName : " + username);
                System.out.println("Password : " + password);


                op = ADFBeanUtils.findOperation("createLogin");
                op.getParamsMap().put("user", bindUserLogin.getValue().toString());
                op.getParamsMap().put("pass", password);
                op.execute();
                System.out.println("--authenticateUserPassword--Result---::" + op.getResult());
                if (op.getResult().equals("S")) {
                    BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();
                    OperationBinding operationBinding =
                        bindings.getOperationBinding("Commit"); // create portalvendormaster Row
                    Object result1 = operationBinding.execute();
                    StringBuilder msg =
                        new StringBuilder("<html><body><b>Congratulations!! Your Login created successfully.</b></body></html>");
                    ADFBeanUtils.showFacesMessageInfo("", msg.toString());
                    //                    user.setDisabled(true);
                    bindPassword.setDisabled(true);
                    bindCpassword.setDisabled(true);
                    bindCreateLoginBtn.setDisabled(true);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(bindPassword);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(bindCpassword);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(createLoginHeader);
                } else if (op.getResult().equals("F")) {
                    StringBuilder msg =
                        new StringBuilder("<html><body><b>Temporary Id and Password not matched.</b></body></html>");
                    ADFBeanUtils.showFacesMessageInfo("", msg.toString());
                } else if (op.getResult().equals("U")) {
                    StringBuilder msg =
                        new StringBuilder("<html><body><b>Temporary Id is already used.</b></body></html>");
                    ADFBeanUtils.showFacesMessageInfo("", msg.toString());
                }
            } else {
                StringBuilder msg =
                    new StringBuilder("<html><body><b>Password and Confirm Password not match.</b></body></html>");
                ADFBeanUtils.showFacesMessageInfo("", msg.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBindCreateLoginBtn(RichButton bindCreateLoginBtn) {
        this.bindCreateLoginBtn = bindCreateLoginBtn;
    }

    public RichButton getBindCreateLoginBtn() {
        return bindCreateLoginBtn;
    }

    public void setCreateLoginHeader(RichPanelHeader createLoginHeader) {
        this.createLoginHeader = createLoginHeader;
    }

    public RichPanelHeader getCreateLoginHeader() {
        return createLoginHeader;
    }

    public static String generate_password(String Name) {

        // Get the first 4 letters of the name and capitalize the first letter
        String initials = Name.substring(0, 1).toUpperCase() + Name.substring(1, Math.min(Name.length(), 4));

        // Generate 4 random numbers
        Random random = new Random();
        int randomNumber = 1000 + random.nextInt(9000);

        // Concatenate initials and random numbers
        String generatedString = initials + randomNumber;

        System.out.println(generatedString); // Output example: Joha3456

        return generatedString;
    }

    public void setBindRegistrationId(RichInputText bindRegistrationId) {
        this.bindRegistrationId = bindRegistrationId;
    }

    public RichInputText getBindRegistrationId() {
        return bindRegistrationId;
    }

    public void setBindContactMiddle(RichInputText bindContactMiddle) {
        this.bindContactMiddle = bindContactMiddle;
    }

    public RichInputText getBindContactMiddle() {
        return bindContactMiddle;
    }

    public void setBindContactLast(RichInputText bindContactLast) {
        this.bindContactLast = bindContactLast;
    }

    public RichInputText getBindContactLast() {
        return bindContactLast;
    }

    public void setBindStateDesc(RichInputText bindStateDesc) {
        this.bindStateDesc = bindStateDesc;
    }

    public RichInputText getBindStateDesc() {
        return bindStateDesc;
    }

    public void setBindDistrictDesc(RichInputText bindDistrictDesc) {
        this.bindDistrictDesc = bindDistrictDesc;
    }

    public RichInputText getBindDistrictDesc() {
        return bindDistrictDesc;
    }

    public void setBindUserLogin(RichInputText bindUserLogin) {
        this.bindUserLogin = bindUserLogin;
    }

    public RichInputText getBindUserLogin() {
        return bindUserLogin;
    }


    public void setBindPassword(RichInputText bindPassword) {
        this.bindPassword = bindPassword;
    }

    public RichInputText getBindPassword() {
        return bindPassword;
    }

    public void setBindCpassword(RichInputText bindCpassword) {
        this.bindCpassword = bindCpassword;
    }

    public RichInputText getBindCpassword() {
        return bindCpassword;
    }

    public void setBindRegFlag(RichInputText bindRegFlag) {
        this.bindRegFlag = bindRegFlag;
    }

    public RichInputText getBindRegFlag() {
        return bindRegFlag;
    }

    public void setBindVendorFlag(RichInputText bindVendorFlag) {
        this.bindVendorFlag = bindVendorFlag;
    }

    public RichInputText getBindVendorFlag() {
        return bindVendorFlag;
    }

    public static Object[] callFunctionForMultiResult(int retValue, String stmt, Object[] inBindVars,
                                                      Object[] outBindVars, Map... map) {
        System.out.println("input parameters are " + Arrays.toString(inBindVars) + " retValue:" + retValue + " stmt:" +
                           stmt + " outBindVars:" + Arrays.toString(outBindVars));
        TermsMainAppAMImpl am = createAM();
        CallableStatement st = null;
        try {

            st = am.getDBTransaction().createCallableStatement("begin ? :=" + stmt + ";end;", 0);
            st.registerOutParameter(1, retValue);
            int inLength = 0;
            if (inBindVars != null) {
                inLength = inBindVars.length;
                System.out.println(" inBindVars.length:" + inBindVars.length);
                for (int z = 1; z <= inBindVars.length; z++) {
                    System.out.println("st.setObject(" + (z + 1) + ")" + " inBindVars position:" + (z - 1) + " value:" +
                                       inBindVars[z - 1]);
                    st.setObject(z + 1, inBindVars[z - 1]);
                }
            }

            ArrayList<Integer> rss = new ArrayList<Integer>();
            if (outBindVars != null) {
                System.out.println(" outBindVars.length:" + outBindVars.length);
                for (int z = 1; z <= outBindVars.length; z++) {
                    Object obj = outBindVars[z - 1];
                    System.out.println("outBindVars position:" + (z - 1) + " value:" + outBindVars[z - 1] +
                                       " (obj instanceof Class):" + (obj instanceof Class));
                    if ((obj instanceof Class) && (ProcedureResult.class.isAssignableFrom((Class) obj))) {
                        //                        System.out.println(" ProcedureResult.class.isAssignableFrom((Class) obj)):"+ProcedureResult.class.isAssignableFrom((Class) obj));
                        System.out.println("Array rss adding:" + (inLength + z + 1));
                        rss.add((Integer) inLength + z + 1);
                        System.out.println("Array Printing:" + rss);
                        System.out.println("st.registerOutParameter(" + (inLength + z + 1) + ")" + " value:" +
                                           OracleTypes.CURSOR);
                        st.registerOutParameter(inLength + z + 1, OracleTypes.CURSOR);
                    } else {
                        System.out.println("st.registerOutParameter(" + (inLength + z + 1) + ")" + " value:" +
                                           Arrays.asList(obj));
                        st.registerOutParameter(inLength + z + 1, (Integer) obj);
                    }
                }
            }
            st.execute();
            System.out.println("List is " + Arrays.toString(rss.toArray()));
            ArrayList list = new ArrayList();
            int collectionPos = 0;
            list.add(st.getObject(1));
            if (outBindVars != null) {
                for (int z = 1; z <= outBindVars.length; z++) {
                    Object obj = st.getObject(inLength + z + 1);
                    if (rss.contains(inLength + z + 1)) {
                        ResultSet rs = (ResultSet) obj;
                        if (rs == null)
                            continue;
                        // System.out.println("For intdex"+z);
                        while (rs.next()) {
                            // System.out.println("Has Next");
                            Object[] ProcResultableArray = new Object[(rs.getMetaData()).getColumnCount()];
                            for (int i = 1; i <= (rs.getMetaData()).getColumnCount(); i++) {
                                Object colVal = rs.getObject(i);
                                System.out.println(" colVal:" + colVal);
                                if (rs.getMetaData().getColumnType(i) == Types.CLOB) {
                                    colVal = converClobToString((Clob) rs.getObject(i));
                                    System.out.println(" clob[" + i + "]:" + colVal);
                                }

                                ProcResultableArray[i - 1] = colVal;
                            }
                            ProcedureResult procObj = null;
                            try {
                                procObj = (ProcedureResult) ((Class) (outBindVars[z - 1])).newInstance();
                                procObj.setValues(ProcResultableArray);
                            } catch (IllegalAccessException | InstantiationException e) {
                                e.printStackTrace();
                            }

                            procObj.addToCollection(map[collectionPos]);

                        }
                        obj = map[collectionPos];
                        collectionPos++;
                        rs.close();
                    }

                    list.add(obj);

                }
            }
            System.out.println("output parameters are " + Arrays.toString(list.toArray()));
            return list.toArray();
        } catch (SQLException e) {
            int end = e.getMessage().indexOf("\n");
            e.printStackTrace();
            throw new JboException(e.getMessage());
        } finally {
            if (st != null) {
                try {
                    st.close();
                    releaseAM(am);
                } catch (SQLException e) {
                    throw new JboException(e.getMessage());
                }
            }
        }
    }

    public static TermsMainAppAMImpl createAM() {
        TermsMainAppAMImpl am =
            (TermsMainAppAMImpl) Configuration.createRootApplicationModule("terms.erp.mainapp.model.services.TermsMainAppAMImpl",
                                                                           "TermsMainAppAMLocal");
        // System.out.println("Creating AM----->>" + am);
        return am;
    }

    public static void releaseAM(TermsMainAppAMImpl am) {

        Configuration.releaseRootApplicationModule(am, true);
        // System.out.println("Releasing AM----->>" + am);
    }

    public static String converClobToString(Clob clob) {
        if (clob == null)
            return null;

        StringBuffer strOut = new StringBuffer();
        String aux;
        try {
            BufferedReader br = new BufferedReader(clob.getCharacterStream());
            while ((aux = br.readLine()) != null) {
                strOut.append(aux);
                //  strOut.append(System.getProperty("line.separator"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String clobStr = strOut.toString();


        return clobStr;
    }

    public void setBindVendorId(RichInputText bindVendorId) {
        this.bindVendorId = bindVendorId;
    }

    public RichInputText getBindVendorId() {
        return bindVendorId;
    }

    public void setBindAadhar(RichInputText bindAadhar) {
        this.bindAadhar = bindAadhar;
    }

    public RichInputText getBindAadhar() {
        return bindAadhar;
    }

    public void setBindCity(RichInputComboboxListOfValues bindCity) {
        this.bindCity = bindCity;
    }

    public RichInputComboboxListOfValues getBindCity() {
        return bindCity;
    }

    public void setBindCityDesc(RichInputText bindCityDesc) {
        this.bindCityDesc = bindCityDesc;
    }

    public RichInputText getBindCityDesc() {
        return bindCityDesc;
    }

    public void gotoForgotPassword(ActionEvent actionEvent) {
        try {
            ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) ectx.getResponse();

            String url = ectx.getRequestContextPath() + "/faces/ForgotPassword.jspx";
            System.out.println("Landing page Url is-----:" + url);

            response.sendRedirect(url);
        } catch (IOException e) {
        }
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void validatePAN(FacesContext facesContext, UIComponent uIComponent, Object object) {
        System.out.println("PAN : " + object.toString().toUpperCase());
        if (isValidPAN(object.toString().toUpperCase())) {
            System.out.println("Valid PAN");
        } else {
            System.out.println("Invalid PAN");
            FacesMessage msg = new FacesMessage("Invalid PAN: " + object.toString().toUpperCase());
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }

    }

    public static boolean isValidPAN(String pan) {
        String panRegex = "[A-Z]{5}[0-9]{4}[A-Z]";
        return pan != null && pan.matches(panRegex);
    }

    public void cityExistVce(ValueChangeEvent vce) {
        vce.getComponent().processUpdates(FacesContext.getCurrentInstance());
        if (vce.getNewValue().toString().equals("false")) {
            System.out.println("not exist");
            bindCity.setValue("");
            bindCityDesc.setValue("");
            bindDistrict.setValue("");
            bindDistrictDesc.setValue("");
            bindCity.setDisabled(true);
            bindCityDesc.setDisabled(true);
            bindDistrict.setDisabled(true);
            bindDistrictDesc.setDisabled(true);
            bindOtherCity.setVisible(true);
            bindOtherCity.setValue("");
            bindPinNo.setValue("");
        } else {
            System.out.println("exist");
            bindOtherCity.setVisible(false);
            bindOtherCity.setValue("");
            bindCity.setDisabled(false);
            bindCityDesc.setDisabled(false);
            bindDistrict.setDisabled(false);
            bindDistrictDesc.setDisabled(false);
        }

        AdfFacesContext.getCurrentInstance().addPartialTarget(bindCity);
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindCityDesc);
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindOtherCity);
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindDistrict);
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindDistrictDesc);
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindPinNo);

    }


    public void setBindOtherCity(RichInputText bindOtherCity) {
        this.bindOtherCity = bindOtherCity;
    }

    public RichInputText getBindOtherCity() {
        return bindOtherCity;
    }

    public void validatorAdhaar(FacesContext facesContext, UIComponent uIComponent, Object object) {
        if (object.toString().length() < 16) {
            FacesMessage msg = new FacesMessage("Adhaar Card must be of 16 digits");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }

    public void chkEmailFormat(FacesContext facesContext, UIComponent uIComponent,
                               Object object) throws ValidatorException {
        String emails = (String) object;
        if (!emails.equals("")) {
            String[] emailArray = emails.split("[;]\\s*");

            String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
            for (String email : emailArray) {
                if (!email.matches(regex)) {
                    FacesMessage msg = new FacesMessage("Invalid email: " + email);
                    msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                    throw new ValidatorException(msg);
                }
            }
        }

    }

    public void contactPersonVce(ValueChangeEvent vce) {
        vce.getComponent().processUpdates(FacesContext.getCurrentInstance());
        if (vce.getNewValue() != null) {
            bindContactPerson.setValue(vce.getNewValue().toString().toUpperCase().replaceAll("\\s+", ""));
            AdfFacesContext.getCurrentInstance().addPartialTarget(bindContactPerson);
        }
    }

    public void midNameVce(ValueChangeEvent vce) {
        vce.getComponent().processUpdates(FacesContext.getCurrentInstance());
        if (vce.getNewValue() != null) {
            bindContactMiddle.setValue(vce.getNewValue().toString().toUpperCase().replaceAll("\\s+", ""));
            AdfFacesContext.getCurrentInstance().addPartialTarget(bindContactMiddle);
        }
    }

    public void lastNameVce(ValueChangeEvent vce) {
        vce.getComponent().processUpdates(FacesContext.getCurrentInstance());
        if (vce.getNewValue() != null) {
            bindContactLast.setValue(vce.getNewValue().toString().toUpperCase().replaceAll("\\s+", ""));
            AdfFacesContext.getCurrentInstance().addPartialTarget(bindContactLast);
        }
    }

    public void passwVce(ValueChangeEvent vce) {
        vce.getComponent().processUpdates(FacesContext.getCurrentInstance());
        if (vce.getNewValue() != null) {
            String pass = vce.getNewValue().toString().replaceAll("\\s", "").trim();
            bindPassword.setValue(pass);
            AdfFacesContext.getCurrentInstance().addPartialTarget(bindPassword);
        }
    }

    public void cPasswVce(ValueChangeEvent vce) {
        vce.getComponent().processUpdates(FacesContext.getCurrentInstance());
        if (vce.getNewValue() != null) {
            String pass = vce.getNewValue().toString().replaceAll("\\s", "").trim();
            bindCpassword.setValue(pass);
            AdfFacesContext.getCurrentInstance().addPartialTarget(bindCpassword);
        }
    }


    public void backToLoginMain(ActionEvent actionEvent) {

        try {
            BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();
            OperationBinding operationBinding =
                bindings.getOperationBinding("Rollback"); // create portalvendormaster Row
            Object result = operationBinding.execute();

            ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();

            System.out.println("In logOut");


            HttpServletResponse response = (HttpServletResponse) ectx.getResponse();
            String url = ectx.getRequestContextPath() + "/faces/login.jspx";
            System.out.println("|On Logout| url is ::::::::::: " + url);

            ResourceBundleManagerRT rt = (ResourceBundleManagerRT) ResourceBundleManagerRT.getResourceBundleManager();
            rt.flush();

            ResourceBundle.clearCache(Thread.currentThread().getContextClassLoader());

            HttpSession session = (HttpSession) ectx.getSession(false);
            session.invalidate();

            try {
                response.sendRedirect(url);
                FacesContext.getCurrentInstance().responseComplete();
            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createKycDocs() {

        BindingContext bcxt = BindingContext.getCurrent();
        DCBindingContainer bc = (DCBindingContainer) bcxt.getCurrentBindingsEntry();
        DCIteratorBinding iter = bc.findIteratorBinding("PortalDocAttachRefDtlVO2Iterator");
        if (iter.getEstimatedRowCount() == 0) {
            OperationBinding op = null;
            String userId = (String) ADFUtils.resolveExpression("#{pageFlowScope.userId}");
            Row r = (Row) adf.utils.bean.ADFBeanUtils.evaluateEL("#{bindings.PortalVendorRegdVO1Iterator.currentRow}");
            Long vendorId = new Long(0);
            vendorId = (Long) r.getAttribute("VendorId");
            System.out.println("Vendor Id : " + vendorId);
            try {
                op = adf.utils.bean.ADFBeanUtils.findOperation("populateDocs");
                op.getParamsMap().put("vendId", vendorId);
                op.getParamsMap().put("usrId", vendorId);
                op.execute();
                System.out.println("--authenticateUserPassword--Result---::" + op.getResult());
                AdfFacesContext.getCurrentInstance().addPartialTarget(bindDocTable);
                //            if (op.getResult().equals("S")) {
                //                this.assignDataToPageFlowScope(obj);
                //                this.assignDataToSession("N");
                //            }

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }


    public void setBindDocTable(RichTable bindDocTable) {
        this.bindDocTable = bindDocTable;
        createKycDocs();
    }

    public RichTable getBindDocTable() {
        return bindDocTable;
    }

    public void uploadDocs(ValueChangeEvent vce) {
        UploadedFile fileVal = (UploadedFile) vce.getNewValue();
        OperationBinding op = null;
        if (vce.getNewValue() != null) {
            op = adf.utils.bean.ADFBeanUtils.findOperation("chkDupFile");
            op.getParamsMap().put("FileName", fileVal.getFilename());
            op.execute();
            String result = op.getResult().toString();
            if (result.equals("Y")) {
                bindInputFile.setValue("");
                AdfFacesContext.getCurrentInstance().addPartialTarget(bindInputFile);
                ADFUtils.showMessage("This document is already attached. Duplicate entries are not allowed.", 2);
                return;
            }
            long fileSize = fileVal.getLength(); // size in bytes

            if (fileSize > 5 * 1024 * 1024) { // 5 MB limit
                FacesMessage msg = new FacesMessage("File size exceeds 5MB limit");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                Row r =
                    (Row) adf.utils.bean.ADFBeanUtils.evaluateEL("#{bindings.PortalDocAttachRefDtlVO2Iterator.currentRow}");
                //            UploadedFile fileVal = (UploadedFile) vce.getNewValue();
                System.out.println("File Value==============<>" + fileVal);
                String path = ADFBeanUtils.uploadFile(fileVal);
                System.out.println(fileVal.getContentType());
                System.out.println("Content Type==>>" + fileVal.getContentType() + "Path==>>" + path + "File name==>>" +
                                   fileVal.getFilename());
                r.setAttribute("Path", path);
                r.setAttribute("DocFileName", fileVal.getFilename());
                r.setAttribute("Remarks", r.getAttribute("RefDocType"));
                ResetUtils.reset(vce.getComponent());
            }
        }
    }

    public void setBindInputFile(RichInputFile bindInputFile) {
        this.bindInputFile = bindInputFile;
    }

    public RichInputFile getBindInputFile() {
        return bindInputFile;
    }

    public void downloadAction(FacesContext facesContext, OutputStream outputStream) throws Exception {
        File filed = new File(pathBinding.getValue().toString());
        FileInputStream fis;
        byte[] b;
        try {
            fis = new FileInputStream(filed);

            int n;
            while ((n = fis.available()) > 0) {
                b = new byte[n];
                int result = fis.read(b);
                outputStream.write(b, 0, b.length);
                if (result == -1)
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        outputStream.flush();

    }

    public void setPathBinding(RichInputText pathBinding) {
        this.pathBinding = pathBinding;
    }

    public RichInputText getPathBinding() {
        return pathBinding;
    }

    public void reviewDataVce(ValueChangeEvent vce) {
        // Add event code here...
    }

    public void setBindReviewFlg(RichSelectBooleanCheckbox bindReviewFlg) {
        this.bindReviewFlg = bindReviewFlg;
    }

    public RichSelectBooleanCheckbox getBindReviewFlg() {
        return bindReviewFlg;
    }


    public void changeLanguage(ValueChangeEvent vce) {
        vce.getComponent().processUpdates(FacesContext.getCurrentInstance());
        if (vce.getNewValue().toString().equals("E")) {
            bindHindiPanel.setVisible(false);
            bindEnglishPanel.setVisible(true);
        } else {
            bindHindiPanel.setVisible(true);
            bindEnglishPanel.setVisible(false);
        }
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindHindiPanel);
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindEnglishPanel);
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindConsentMainHeader);
    }

    public void setBindHindiPanel(RichPanelHeader bindHindiPanel) {
        this.bindHindiPanel = bindHindiPanel;
    }

    public RichPanelHeader getBindHindiPanel() {
        return bindHindiPanel;
    }

    public void setBindEnglishPanel(RichPanelHeader bindEnglishPanel) {
        this.bindEnglishPanel = bindEnglishPanel;
    }

    public RichPanelHeader getBindEnglishPanel() {
        return bindEnglishPanel;
    }

    public void setBindRegFlg(RichSelectOneRadio bindRegFlg) {
        this.bindRegFlg = bindRegFlg;
    }

    public RichSelectOneRadio getBindRegFlg() {
        return bindRegFlg;
    }


    public void gstRegNoValidator(FacesContext facesContext, UIComponent uIComponent, Object object) {


        if (object != null) {
            String gstReqNo = (String) object;
            if (!(gstReqNo.length() == 15)) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                              "GST Reg No. must be 15 Digit.", null));
            }
            if (!object.toString().matches("^[a-zA-Z0-9]*$")) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                              "Gst Reg No. must not contain Special Character.", null));
            }
        }
    }


    public void GstnRegNoVCL(ValueChangeEvent valueChangeEvent) throws MalformedURLException, ProtocolException,
                                                                       IOException {
        valueChangeEvent.getComponent().processUpdates(FacesContext.getCurrentInstance());
        if (valueChangeEvent.getNewValue().toString().length() == 15) {
            adf.utils.bean.ADFBeanUtils.setEL("#{bindings.PanNo.inputValue}",
                                              valueChangeEvent.getNewValue().toString().substring(2, 12));


            String urlString = null;
            String unitCode = "11000";
            String gstRegNo = valueChangeEvent.getNewValue().toString();
            OperationBinding op;
            op = adf.utils.bean.ADFBeanUtils.findOperation("getEmailConfigValue");
            op.getParamsMap().put("keyno", 132);
            op.getParamsMap().put("unitcd", unitCode);
            op.getParamsMap().put("tcode", "MKCU");
            op.execute();

            if (!op.getErrors().isEmpty()) {
                adf.utils.bean.ADFBeanUtils.showFacesMessageError("Issue in parameter key 132 --(" +
                                                                  op.getErrors().toString() + ") ", "");
                return;
            }

            Object resultObj = op.getResult();

            if (resultObj != null) {
                urlString = resultObj.toString();


                if (!"N".equalsIgnoreCase(urlString)) {
                    // System.out.println("Result is===> " + op.getResult());

                    //urlString = op.getResult().toString();
                    urlString = urlString + "?dbunit=" + unitCode + "&gstin=" + gstRegNo;
                    //                urlString = urlString + "SendEmailMKSO?UnitCode=" + unitCode + "&Tcode=MKSO&DocNo="+ackNumber+"&mailType=Page";
                    // urlString = "http://127.0.0.1:7101/WebServiceApp/resources/SendMail?UnitCode=13001&Tcode=MMPO&DocNo="+poId+"";
                    // System.out.println("url--->" + urlString);
                    URL obj = null;
                    try {
                        obj = new URL(urlString);
                    } catch (MalformedURLException e) {
                    }
                    HttpURLConnection con = null;
                    try {
                        con = (HttpURLConnection) obj.openConnection();
                        con.setConnectTimeout(60000); // 1 minute to establish connection
                        con.setReadTimeout(120000); // 2 minutes to read response
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // optional default is GET
                    try {
                        con.setRequestMethod("GET");
                    } catch (ProtocolException e) {
                    }
                    //add request header
                    con.setRequestProperty("User-Agent", "Mozilla/5.0");
                    int responseCode = 0;
                    try {
                        responseCode = con.getResponseCode();

                    } catch (IOException e) {
                    }

                    if (responseCode == 200) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();
                        String status = response.toString();

                        JsonSlurper slurper = new JsonSlurper();


                        Map parsed = (Map) slurper.parseText(status);

                        Boolean isSuccess = (Boolean) parsed.get("success");
                        if (isSuccess != null && isSuccess) {

                            Map result = (Map) parsed.get("result");

                            String gstin = (String) result.get("Gstin") != null ? result.get("Gstin").toString() : "";
                            String tradeName =
                                (String) result.get("TradeName") != null ? result.get("TradeName").toString() : "";
                            String legalName =
                                (String) result.get("LegalName") != null ? result.get("LegalName").toString() : "";
                            String AddrBnm =
                                (String) result.get("AddrBnm") != null ? result.get("AddrBnm").toString() : "";
                            String addrBno =
                                (String) result.get("AddrBno") != null ? result.get("AddrBno").toString() : "";
                            String addrFlno =
                                (String) result.get("AddrFlno") != null ? result.get("AddrFlno").toString() : "";
                            String addrSt =
                                (String) result.get("AddrSt") != null ? result.get("AddrSt").toString() : "";
                            String addrLoc =
                                (String) result.get("AddrLoc") != null ? result.get("AddrLoc").toString() : "";
                            String stateCode =
                                result.get("StateCode") != null ? result.get("StateCode").toString() : "";
                            String addrPncd = result.get("AddrPncd") != null ? result.get("AddrPncd").toString() : "";
                            String status1 =
                                (String) result.get("Status") != null ? result.get("Status").toString() : "";
                            if (status1.equalsIgnoreCase("CNL")) {
                                adf.utils.bean.ADFBeanUtils.showFacesMessageError("Invalid GST Number:" + gstin,
                                                                                  "Status: Cancelled.Please enter a valid GST number");
                                //                                bindingSaveButton.setDisabled(true);
                                //                                AdfFacesContext.getCurrentInstance().addPartialTarget(bindingSaveButton);

                                return;
                            }

                            else {
                                adf.utils.bean.ADFBeanUtils.showFacesMessageInfo("Success",
                                                                                 "GST Number::" + gstin + " " +
                                                                                 "|Status: Active");
                                //                                bindingSaveButton.setDisabled(false);
                                //                                AdfFacesContext.getCurrentInstance().addPartialTarget(bindingSaveButton);
                            }
                            //  String blkStatus = (String) result.get("BlkStatus")!= null ? result.get("BlkStatus").toString() : "";
                            String dtReg = (String) result.get("DtReg") != null ? result.get("DtReg").toString() : "";
                            // String dtDReg = (String) result.get("DtDReg")!= null ? result.get("DtDReg").toString() : "";

                            StringBuilder addressBuilder = new StringBuilder();

                            if (!addrBno.isEmpty()) {
                                addressBuilder.append(addrBno);
                            }
                            if (!addrFlno.isEmpty()) {
                                if (addressBuilder.length() > 0)
                                    addressBuilder.append(", ");
                                addressBuilder.append(addrFlno);
                            }
                            if (!addrSt.isEmpty()) {
                                if (addressBuilder.length() > 0)
                                    addressBuilder.append(", ");
                                addressBuilder.append(addrSt);
                            }
                            if (!addrLoc.isEmpty()) {
                                if (addressBuilder.length() > 0)
                                    addressBuilder.append(", ");
                                addressBuilder.append(addrLoc);
                            }
                            String StateNm = getStateName(stateCode);
                            String fullAddress = addressBuilder.toString();

                            String address1 = fullAddress.length() > 99 ? fullAddress.substring(0, 99) : fullAddress;
                            String otherAddress = fullAddress.length() > 99 ? fullAddress.substring(99) : "";
                            Row row =
                                (Row) ADFBeanUtils.evaluateEL("#{bindings.PortalVendorRegdVO1Iterator.currentRow}");
                            row.setAttribute("GstRegNo", gstin);
                            row.setAttribute("Name", tradeName);
                            row.setAttribute("LegName", legalName);
                            //                        ADFBeanUtils.setEL("#{bindings.GstRegNo.inputValue}", gstin);
                            //                            adf.utils.bean.ADFBeanUtils.setEL("#{bindings.Name.inputValue}",tradeName );
                            adf.utils.bean.ADFBeanUtils.setEL("#{bindings.LglNameOfBs.inputValue}", legalName);
                            adf.utils.bean.ADFBeanUtils.setEL("#{bindings.StateName.inputValue}", StateNm);
                            adf.utils.bean.ADFBeanUtils.setEL("#{bindings.BillAddressLine1.inputValue}", address1);
                            adf.utils.bean.ADFBeanUtils.setEL("#{bindings.BillAddressLine2.inputValue}", otherAddress);
                            //                        ADFBeanUtils.setEL("#{bindings.RegFlag.inputValue}", "R");
                            //                        ADFBeanUtils.setEL("#{bindings.VendorFlag.inputValue}", "I");
                            try {
                                if (dtReg != null && !dtReg.isEmpty()) {
                                    java.sql.Timestamp ts =
                                        new java.sql.Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse(dtReg).getTime());
                                    //                            adf.utils.bean.ADFBeanUtils.setEL("#{bindings.GstRegDt.inputValue}", ts);
                                }
                            } catch (ParseException e) {
                                System.out.println("Invalid date format: " + dtReg);
                            }
                            //                        ADFBeanUtils.setEL("#{bindings.PanNo.inputValue}", addrLoc);
                            //                            adf.utils.bean.ADFBeanUtils.setEL("#{bindings.StateGstCode.inputValue}", stateCode);
                            adf.utils.bean.ADFBeanUtils.setEL("#{bindings.PinCode.inputValue}", addrPncd);

                        } else {
                            adf.utils.bean.ADFBeanUtils.setEL("#{bindings.GstRegNo.inputValue}", "");
                            adf.utils.bean.ADFBeanUtils.showFacesMessageError("Invalid Gst No",
                                                                              "Please Enter Valid  Gst No");
                            //                            bindingSaveButton.setDisabled(true);
                            //                            AdfFacesContext.getCurrentInstance().addPartialTarget(bindingSaveButton);

                        }

                    } else {
                        adf.utils.bean.ADFBeanUtils.showFacesMessageError("Response status codes--(" + responseCode +
                                                                          ") The requested URL was not found.", "");
                        return;
                    }

                }
            }


        }
    }

    public String getStateName(String StateGstCode) {

        try {
            ViewObject vo = getAm().getStateVO1();

            vo.setWhereClause(null);
            vo.setWhereClause("STATE_GST_CODE = :1");
            vo.setWhereClauseParam(0, StateGstCode);
            vo.executeQuery();

            oracle.jbo.Row row = vo.first();

            if (row != null) {
                Object stateName = row.getAttribute("StateName");
                return stateName != null ? stateName.toString() : null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String validateKycDocs(String action) {
        Row row = (Row) ADFBeanUtils.evaluateEL("#{bindings.PortalVendorRegdVO1Iterator.currentRow}");
        Object GstNo = row.getAttribute("GstRegNo");
        Object PanNo = row.getAttribute("PanNo");
        BindingContext bcxt = BindingContext.getCurrent();
        DCBindingContainer bc = (DCBindingContainer) bcxt.getCurrentBindingsEntry();
        DCIteratorBinding iter = bc.findIteratorBinding("PortalDocAttachRefDtlVO2Iterator");
        RowSetIterator rsi = iter.getViewObject().createRowSetIterator(null);
        int count = 0;
        while (rsi.hasNext()) {

            Row docsRow = rsi.next();

            Object pathValue = docsRow.getAttribute("RefDocType");
            Object docName = docsRow.getAttribute("DocFileName");
            if (pathValue.equals("GST REG CERTIFICATE") && GstNo != null && docName == null) {

                FacesContext.getCurrentInstance().addMessage(this.getBindAddress().getClientId(),
                                                             TermsADFUtils.showInfoMessage("Mandatory",
                                                                                           "Please upload Gst Reg Certificate in KYC Document section"));
                return null;
            }

            if (pathValue.equals("PAN CARD") && PanNo != null && docName == null) {

                FacesContext.getCurrentInstance().addMessage(this.getBindAddress().getClientId(),
                                                             TermsADFUtils.showInfoMessage("Mandatory",
                                                                                           "Please upload Pan No. in KYC Document section"));
                return null;
            }
        }


        return action;
    }

    public String checkKycBack() {
        return validateKycDocs("back");
    }

    public String chkKycNext() {
        return validateKycDocs("next");
    }


    public void setBindDataProcessFlg(RichSelectBooleanCheckbox bindDataProcessFlg) {
        this.bindDataProcessFlg = bindDataProcessFlg;
    }

    public RichSelectBooleanCheckbox getBindDataProcessFlg() {
        return bindDataProcessFlg;
    }

    public void setBindTranAlertFlg(RichSelectBooleanCheckbox bindTranAlertFlg) {
        this.bindTranAlertFlg = bindTranAlertFlg;
    }

    public RichSelectBooleanCheckbox getBindTranAlertFlg() {
        return bindTranAlertFlg;
    }

    public void setBindEmailAlertFlg(RichSelectBooleanCheckbox bindEmailAlertFlg) {
        this.bindEmailAlertFlg = bindEmailAlertFlg;
    }

    public RichSelectBooleanCheckbox getBindEmailAlertFlg() {
        return bindEmailAlertFlg;
    }

    public void setBindWAAlertFlg(RichSelectBooleanCheckbox bindWAAlertFlg) {
        this.bindWAAlertFlg = bindWAAlertFlg;
    }

    public RichSelectBooleanCheckbox getBindWAAlertFlg() {
        return bindWAAlertFlg;
    }

    public void setBindCookiesFlg(RichSelectBooleanCheckbox bindCookiesFlg) {
        this.bindCookiesFlg = bindCookiesFlg;
    }

    public RichSelectBooleanCheckbox getBindCookiesFlg() {
        return bindCookiesFlg;
    }

    public void setBindAgeConfirmFlg(RichSelectBooleanCheckbox bindAgeConfirmFlg) {
        this.bindAgeConfirmFlg = bindAgeConfirmFlg;
    }

    public RichSelectBooleanCheckbox getBindAgeConfirmFlg() {
        return bindAgeConfirmFlg;
    }

    public String ConsentAgree() {
        if (bindNoticeLanFlg.getValue().equals("H")) {

            if (Boolean.FALSE.equals(bindDataProcessFlg.getValue()) ||
                Boolean.FALSE.equals(bindTranAlertFlg.getValue()) ||
                Boolean.FALSE.equals(bindAgeConfirmFlg.getValue())) {
                FacesContext.getCurrentInstance().addMessage(this.getBindAddress().getClientId(),
                                                             TermsADFUtils.showInfoMessage("Mandatory",
                                                                                           "????? ????? ?? ??? ??? ???????? ???????? ?? ??? ?????"));
                return null;
            } else {
                Row row = (Row) ADFBeanUtils.evaluateEL("#{bindings.PortalVendorRegdVO1Iterator.currentRow}");
                if (Boolean.TRUE.equals(bindDataProcessFlg.getValue()) ||
                    "true".equalsIgnoreCase(String.valueOf(bindDataProcessFlg.getValue()))) {
                    row.setAttribute("PrcDataFlg", "Y");
                } else {
                    row.setAttribute("PrcDataFlg", "N");
                }
                if (Boolean.TRUE.equals(bindTranAlertFlg.getValue()) ||
                    "true".equalsIgnoreCase(String.valueOf(bindTranAlertFlg.getValue()))) {
                    row.setAttribute("RecTrnAltFlg", "Y");
                } else {
                    row.setAttribute("RecTrnAltFlg", "N");
                }
                if (Boolean.TRUE.equals(bindEmailAlertFlg.getValue()) ||
                    "true".equalsIgnoreCase(String.valueOf(bindEmailAlertFlg.getValue()))) {
                    row.setAttribute("SendMailFlg", "Y");
                } else {
                    row.setAttribute("SendMailFlg", "N");
                }
                if (Boolean.TRUE.equals(bindWAAlertFlg.getValue()) ||
                    "true".equalsIgnoreCase(String.valueOf(bindWAAlertFlg.getValue()))) {
                    row.setAttribute("SendWpFlg", "Y");
                } else {
                    row.setAttribute("SendWpFlg", "N");
                }
                if (Boolean.TRUE.equals(bindCookiesFlg.getValue()) ||
                    "true".equalsIgnoreCase(String.valueOf(bindCookiesFlg.getValue()))) {
                    row.setAttribute("AnalyticCookiesFlg", "Y");
                } else {
                    row.setAttribute("AnalyticCookiesFlg", "N");
                }
                if (Boolean.TRUE.equals(bindAgeConfirmFlg.getValue()) ||
                    "true".equalsIgnoreCase(String.valueOf(bindAgeConfirmFlg.getValue()))) {
                    row.setAttribute("AgeConfFlg", "Y");
                } else {
                    row.setAttribute("AgeConfFlg", "N");
                }
            }
        } else if (bindNoticeLanFlg.getValue().equals("E")) {
            if (Boolean.FALSE.equals(bindDProcessEngFlg.getValue()) ||
                Boolean.FALSE.equals(bindTranAlertEngFlg.getValue()) ||
                Boolean.FALSE.equals(bindAgeAlertEngFlg.getValue())) {
                FacesContext.getCurrentInstance().addMessage(this.getBindAddress().getClientId(),
                                                             TermsADFUtils.showInfoMessage("Mandatory",
                                                                                           "Please select the mandatory options for Consent"));
                return null;
            } else {
                Row row = (Row) ADFBeanUtils.evaluateEL("#{bindings.PortalVendorRegdVO1Iterator.currentRow}");
                if (Boolean.TRUE.equals(bindDProcessEngFlg.getValue()) ||
                    "true".equalsIgnoreCase(String.valueOf(bindDProcessEngFlg.getValue()))) {
                    row.setAttribute("PrcDataFlg", "Y");
                } else {
                    row.setAttribute("PrcDataFlg", "N");
                }
                if (Boolean.TRUE.equals(bindTranAlertEngFlg.getValue()) ||
                    "true".equalsIgnoreCase(String.valueOf(bindTranAlertEngFlg.getValue()))) {
                    row.setAttribute("RecTrnAltFlg", "Y");
                } else {
                    row.setAttribute("RecTrnAltFlg", "N");
                }
                if (Boolean.TRUE.equals(bindEmailAlertEngFlg.getValue()) ||
                    "true".equalsIgnoreCase(String.valueOf(bindEmailAlertEngFlg.getValue()))) {
                    row.setAttribute("SendMailFlg", "Y");
                } else {
                    row.setAttribute("SendMailFlg", "N");
                }
                if (Boolean.TRUE.equals(bindWAEngFlg.getValue()) ||
                    "true".equalsIgnoreCase(String.valueOf(bindWAEngFlg.getValue()))) {
                    row.setAttribute("SendWpFlg", "Y");
                } else {
                    row.setAttribute("SendWpFlg", "N");
                }
                if (Boolean.TRUE.equals(bindCookiesEngFlg.getValue()) ||
                    "true".equalsIgnoreCase(String.valueOf(bindCookiesEngFlg.getValue()))) {
                    row.setAttribute("AnalyticCookiesFlg", "Y");
                } else {
                    row.setAttribute("AnalyticCookiesFlg", "N");
                }
                if (Boolean.TRUE.equals(bindAgeAlertEngFlg.getValue()) ||
                    "true".equalsIgnoreCase(String.valueOf(bindAgeAlertEngFlg.getValue()))) {
                    row.setAttribute("AgeConfFlg", "Y");
                } else {
                    row.setAttribute("AgeConfFlg", "N");
                }
            }
        }


        return "next";
    }


    public String ConsentAgreeTrain() {
        if (bindNoticeLanFlg.getValue().equals("H")) {

            if (Boolean.FALSE.equals(bindDataProcessFlg.getValue()) ||
                Boolean.FALSE.equals(bindTranAlertFlg.getValue()) ||
                Boolean.FALSE.equals(bindAgeConfirmFlg.getValue())) {
                //                FacesContext.getCurrentInstance().addMessage(this.getBindAddress().getClientId(),



                //                                                             TermsADFUtils.showInfoMessage("Mandatory",
                //                                                                                           "????? ????? ?? ??? ??? ???????? ???????? ?? ??? ?????"));
                return null;
            } else {
                Row row = (Row) ADFBeanUtils.evaluateEL("#{bindings.PortalVendorRegdVO1Iterator.currentRow}");
                if (Boolean.TRUE.equals(bindDataProcessFlg.getValue()) ||
                    "true".equalsIgnoreCase(String.valueOf(bindDataProcessFlg.getValue()))) {
                    row.setAttribute("PrcDataFlg", "Y");
                } else {
                    row.setAttribute("PrcDataFlg", "N");
                }
                if (Boolean.TRUE.equals(bindTranAlertFlg.getValue()) ||
                    "true".equalsIgnoreCase(String.valueOf(bindTranAlertFlg.getValue()))) {
                    row.setAttribute("RecTrnAltFlg", "Y");
                } else {
                    row.setAttribute("RecTrnAltFlg", "N");
                }
                if (Boolean.TRUE.equals(bindEmailAlertFlg.getValue()) ||
                    "true".equalsIgnoreCase(String.valueOf(bindEmailAlertFlg.getValue()))) {
                    row.setAttribute("SendMailFlg", "Y");
                } else {
                    row.setAttribute("SendMailFlg", "N");
                }
                if (Boolean.TRUE.equals(bindWAAlertFlg.getValue()) ||
                    "true".equalsIgnoreCase(String.valueOf(bindWAAlertFlg.getValue()))) {
                    row.setAttribute("SendWpFlg", "Y");
                } else {
                    row.setAttribute("SendWpFlg", "N");
                }
                if (Boolean.TRUE.equals(bindCookiesFlg.getValue()) ||
                    "true".equalsIgnoreCase(String.valueOf(bindCookiesFlg.getValue()))) {
                    row.setAttribute("AnalyticCookiesFlg", "Y");
                } else {
                    row.setAttribute("AnalyticCookiesFlg", "N");
                }
                if (Boolean.TRUE.equals(bindAgeConfirmFlg.getValue()) ||
                    "true".equalsIgnoreCase(String.valueOf(bindAgeConfirmFlg.getValue()))) {
                    row.setAttribute("AgeConfFlg", "Y");
                } else {
                    row.setAttribute("AgeConfFlg", "N");
                }
            }
        } else if (bindNoticeLanFlg.getValue().equals("E")) {
            if (Boolean.FALSE.equals(bindDProcessEngFlg.getValue()) ||
                Boolean.FALSE.equals(bindTranAlertEngFlg.getValue()) ||
                Boolean.FALSE.equals(bindAgeAlertEngFlg.getValue())) {
                //                FacesContext.getCurrentInstance().addMessage(this.getBindAddress().getClientId(),



                //                                                             TermsADFUtils.showInfoMessage("Mandatory",
                //                                                                                           "Please select the mandatory options for Consent"));
                return null;
            } else {
                Row row = (Row) ADFBeanUtils.evaluateEL("#{bindings.PortalVendorRegdVO1Iterator.currentRow}");
                if (Boolean.TRUE.equals(bindDProcessEngFlg.getValue()) ||
                    "true".equalsIgnoreCase(String.valueOf(bindDProcessEngFlg.getValue()))) {
                    row.setAttribute("PrcDataFlg", "Y");
                } else {
                    row.setAttribute("PrcDataFlg", "N");
                }
                if (Boolean.TRUE.equals(bindTranAlertEngFlg.getValue()) ||
                    "true".equalsIgnoreCase(String.valueOf(bindTranAlertEngFlg.getValue()))) {
                    row.setAttribute("RecTrnAltFlg", "Y");
                } else {
                    row.setAttribute("RecTrnAltFlg", "N");
                }
                if (Boolean.TRUE.equals(bindEmailAlertEngFlg.getValue()) ||
                    "true".equalsIgnoreCase(String.valueOf(bindEmailAlertEngFlg.getValue()))) {
                    row.setAttribute("SendMailFlg", "Y");
                } else {
                    row.setAttribute("SendMailFlg", "N");
                }
                if (Boolean.TRUE.equals(bindWAEngFlg.getValue()) ||
                    "true".equalsIgnoreCase(String.valueOf(bindWAEngFlg.getValue()))) {
                    row.setAttribute("SendWpFlg", "Y");
                } else {
                    row.setAttribute("SendWpFlg", "N");
                }
                if (Boolean.TRUE.equals(bindCookiesEngFlg.getValue()) ||
                    "true".equalsIgnoreCase(String.valueOf(bindCookiesEngFlg.getValue()))) {
                    row.setAttribute("AnalyticCookiesFlg", "Y");
                } else {
                    row.setAttribute("AnalyticCookiesFlg", "N");
                }
                if (Boolean.TRUE.equals(bindAgeAlertEngFlg.getValue()) ||
                    "true".equalsIgnoreCase(String.valueOf(bindAgeAlertEngFlg.getValue()))) {
                    row.setAttribute("AgeConfFlg", "Y");
                } else {
                    row.setAttribute("AgeConfFlg", "N");
                }
            }
        }


        return "next";
    }


    public void setBindDProcessEngFlg(RichSelectBooleanCheckbox bindDProcessEngFlg) {
        this.bindDProcessEngFlg = bindDProcessEngFlg;
    }

    public RichSelectBooleanCheckbox getBindDProcessEngFlg() {
        return bindDProcessEngFlg;
    }

    public void setBindTranAlertEngFlg(RichSelectBooleanCheckbox bindTranAlertEngFlg) {
        this.bindTranAlertEngFlg = bindTranAlertEngFlg;
    }

    public RichSelectBooleanCheckbox getBindTranAlertEngFlg() {
        return bindTranAlertEngFlg;
    }

    public void setBindEmailAlertEngFlg(RichSelectBooleanCheckbox bindEmailAlertEngFlg) {
        this.bindEmailAlertEngFlg = bindEmailAlertEngFlg;
    }

    public RichSelectBooleanCheckbox getBindEmailAlertEngFlg() {
        return bindEmailAlertEngFlg;
    }

    public void setBindWAEngFlg(RichSelectBooleanCheckbox bindWAEngFlg) {
        this.bindWAEngFlg = bindWAEngFlg;
    }

    public RichSelectBooleanCheckbox getBindWAEngFlg() {
        return bindWAEngFlg;
    }

    public void setBindCookiesEngFlg(RichSelectBooleanCheckbox bindCookiesEngFlg) {
        this.bindCookiesEngFlg = bindCookiesEngFlg;
    }

    public RichSelectBooleanCheckbox getBindCookiesEngFlg() {
        return bindCookiesEngFlg;
    }

    public void setBindAgeAlertEngFlg(RichSelectBooleanCheckbox bindAgeAlertEngFlg) {
        this.bindAgeAlertEngFlg = bindAgeAlertEngFlg;
    }

    public RichSelectBooleanCheckbox getBindAgeAlertEngFlg() {
        return bindAgeAlertEngFlg;
    }

    public void setBindNoticeLanFlg(RichSelectOneChoice bindNoticeLanFlg) {
        this.bindNoticeLanFlg = bindNoticeLanFlg;
        if (bindNoticeLanFlg.getValue() != null)
            ConsentAgreeTrain();
    }

    public RichSelectOneChoice getBindNoticeLanFlg() {
        return bindNoticeLanFlg;
    }

    public String chkConsentAgreement() {
        if (bindNoticeLanR.getValue() != null) {
            if (bindNoticeLanR.getValue().equals("H")) {

                if ("N".equals(bindProDataR.getValue()) || "N".equals(bindRecTranR.getValue()) ||
                    "N".equals(bindAgeConfirmFlg.getValue())) {
                    FacesContext.getCurrentInstance().addMessage(this.getBindAddress().getClientId(),
                                                                 TermsADFUtils.showInfoMessage("Mandatory",
                                                                                               "????? ????? ?? ??? ??? ???????? ???????? ?? ??? ?????"));
                    return null;
                }

            } else if (bindNoticeLanR.getValue().equals("E")) {
                if ("N".equals(bindProDataR.getValue()) || "N".equals(bindRecTranR.getValue()) ||
                    "N".equals(bindAgeConfirmFlg.getValue())) {
                    FacesContext.getCurrentInstance().addMessage(this.getBindAddress().getClientId(),
                                                                 TermsADFUtils.showInfoMessage("Mandatory",
                                                                                               "Please select the mandatory options for Consent"));
                    return null;
                }

            }
        }

        return "success";
    }


    public String ReviewBack() {
        Row row = (Row) ADFBeanUtils.evaluateEL("#{bindings.PortalVendorRegdVO1Iterator.currentRow}");
        if (row.getAttribute("NoticeLanguage").toString().equals("E")) {
            bindHindiPanel.setVisible(false);
            bindEnglishPanel.setVisible(true);
        } else {
            bindHindiPanel.setVisible(true);
            bindEnglishPanel.setVisible(false);
        }
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindHindiPanel);
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindEnglishPanel);
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindConsentMainHeader);

        return "back";
    }

    public void setBindConsentMainHeader(RichPanelHeader bindConsentMainHeader) {
        this.bindConsentMainHeader = bindConsentMainHeader;
    }

    public RichPanelHeader getBindConsentMainHeader() {
        return bindConsentMainHeader;
    }

    public void setBindNoticeLanR(RichInputText bindNoticeLanR) {
        this.bindNoticeLanR = bindNoticeLanR;
    }

    public RichInputText getBindNoticeLanR() {
        return bindNoticeLanR;
    }

    public void setBindProDataR(RichInputText bindProDataR) {
        this.bindProDataR = bindProDataR;
    }

    public RichInputText getBindProDataR() {
        return bindProDataR;
    }

    public void setBindRecTranR(RichInputText bindRecTranR) {
        this.bindRecTranR = bindRecTranR;
    }

    public RichInputText getBindRecTranR() {
        return bindRecTranR;
    }

    public void setBindEmailR(RichInputText bindEmailR) {
        this.bindEmailR = bindEmailR;
    }

    public RichInputText getBindEmailR() {
        return bindEmailR;
    }

    public void setBindWpR(RichInputText bindWpR) {
        this.bindWpR = bindWpR;
    }

    public RichInputText getBindWpR() {
        return bindWpR;
    }

    public void setBindCookFlgR(RichInputText bindCookFlgR) {
        this.bindCookFlgR = bindCookFlgR;
    }

    public RichInputText getBindCookFlgR() {
        return bindCookFlgR;
    }

    public void setBindAgeFlgR(RichInputText bindAgeFlgR) {
        this.bindAgeFlgR = bindAgeFlgR;
    }

    public RichInputText getBindAgeFlgR() {
        return bindAgeFlgR;
    }

    public void proDataHVce(ValueChangeEvent vce) {
        vce.getComponent().processUpdates(FacesContext.getCurrentInstance());
        Row row = (Row) ADFBeanUtils.evaluateEL("#{bindings.PortalVendorRegdVO1Iterator.currentRow}");
        if (Boolean.TRUE.equals(vce.getNewValue().toString()) ||
            "true".equalsIgnoreCase(String.valueOf(vce.getNewValue().toString())))
            row.setAttribute("PrcDataFlg", "Y");
        else
            row.setAttribute("PrcDataFlg", "N");
    }

    public void TranAlertH(ValueChangeEvent vce) {
        vce.getComponent().processUpdates(FacesContext.getCurrentInstance());
        Row row = (Row) ADFBeanUtils.evaluateEL("#{bindings.PortalVendorRegdVO1Iterator.currentRow}");
        if (Boolean.TRUE.equals(vce.getNewValue().toString()) ||
            "true".equalsIgnoreCase(String.valueOf(vce.getNewValue().toString())))
            row.setAttribute("RecTrnAltFlg", "Y");
        else
            row.setAttribute("RecTrnAltFlg", "N");
    }

    public void EmailHVce(ValueChangeEvent vce) {
        vce.getComponent().processUpdates(FacesContext.getCurrentInstance());
        Row row = (Row) ADFBeanUtils.evaluateEL("#{bindings.PortalVendorRegdVO1Iterator.currentRow}");
        if (Boolean.TRUE.equals(vce.getNewValue().toString()) ||
            "true".equalsIgnoreCase(String.valueOf(vce.getNewValue().toString())))
            row.setAttribute("SendMailFlg", "Y");
        else
            row.setAttribute("SendMailFlg", "N");
    }

    public void WPHVce(ValueChangeEvent vce) {
        vce.getComponent().processUpdates(FacesContext.getCurrentInstance());
        Row row = (Row) ADFBeanUtils.evaluateEL("#{bindings.PortalVendorRegdVO1Iterator.currentRow}");
        if (Boolean.TRUE.equals(vce.getNewValue().toString()) ||
            "true".equalsIgnoreCase(String.valueOf(vce.getNewValue().toString())))
            row.setAttribute("SendWpFlg", "Y");
        else
            row.setAttribute("SendWpFlg", "N");
    }

    public void CookHVce(ValueChangeEvent vce) {
        vce.getComponent().processUpdates(FacesContext.getCurrentInstance());
        Row row = (Row) ADFBeanUtils.evaluateEL("#{bindings.PortalVendorRegdVO1Iterator.currentRow}");
        if (Boolean.TRUE.equals(vce.getNewValue().toString()) ||
            "true".equalsIgnoreCase(String.valueOf(vce.getNewValue().toString())))
            row.setAttribute("AnalyticCookiesFlg", "Y");
        else
            row.setAttribute("AnalyticCookiesFlg", "N");
    }

    public void AgeFlgHVce(ValueChangeEvent vce) {
        vce.getComponent().processUpdates(FacesContext.getCurrentInstance());
        Row row = (Row) ADFBeanUtils.evaluateEL("#{bindings.PortalVendorRegdVO1Iterator.currentRow}");
        if (Boolean.TRUE.equals(vce.getNewValue().toString()) ||
            "true".equalsIgnoreCase(String.valueOf(vce.getNewValue().toString())))
            row.setAttribute("AgeConfFlg", "Y");
        else
            row.setAttribute("AgeConfFlg", "N");
    }

    public void makeComponentHierarchyReadOnly(UIComponent component, boolean valueComponent) {
        try {
            Method method1 = component.getClass().getMethod("setDisabled", boolean.class);
            if (method1 != null) {
                method1.invoke(component, valueComponent);
            }
        } catch (NoSuchMethodException e) {
            try {
                Method method = component.getClass().getMethod("setReadOnly", boolean.class);
                if (method != null) {
                    method.invoke(component, valueComponent);
                }
            } catch (Exception e1) {
                // e.printStackTrace();//silently eat this exception.
            }
        } catch (Exception e) {
            // e.printStackTrace();//silently eat this exception.
        }
        List<UIComponent> childComponents = component.getChildren();
        for (UIComponent comp : childComponents) {
            makeComponentHierarchyReadOnly(comp, valueComponent);
        }
    }

    public void setBindKycMainPanel(RichPanelHeader bindKycMainPanel) {
        this.bindKycMainPanel = bindKycMainPanel;
    }

    public RichPanelHeader getBindKycMainPanel() {
        return bindKycMainPanel;
    }

    public void setBindBussDtlMainHeader(RichPanelHeader bindBussDtlMainHeader) {
        this.bindBussDtlMainHeader = bindBussDtlMainHeader;
    }

    public RichPanelHeader getBindBussDtlMainHeader() {
        return bindBussDtlMainHeader;
    }

    public void setBindPersonalDtlMainHdr(RichPanelHeader bindPersonalDtlMainHdr) {
        this.bindPersonalDtlMainHdr = bindPersonalDtlMainHdr;
    }

    public RichPanelHeader getBindPersonalDtlMainHdr() {
        return bindPersonalDtlMainHdr;
    }

    public void setBindReviewCancelBtn(RichCommandButton bindReviewCancelBtn) {
        this.bindReviewCancelBtn = bindReviewCancelBtn;
    }

    public RichCommandButton getBindReviewCancelBtn() {
        return bindReviewCancelBtn;
    }

    public void setBindReviewMainPanelHdr(RichPanelHeader bindReviewMainPanelHdr) {
        this.bindReviewMainPanelHdr = bindReviewMainPanelHdr;
    }

    public RichPanelHeader getBindReviewMainPanelHdr() {
        return bindReviewMainPanelHdr;
    }

    public void loginMode(ValueChangeEvent vce) {
        vce.getComponent().processUpdates(FacesContext.getCurrentInstance());
        if (vce.getNewValue().toString().equals("E")) {
            bindEmailOtpForm.setVisible(true);
            bindUserNameForm.setVisible(false);
            bindUserName.setValue("");
        } else if (vce.getNewValue().toString().equals("P")) {
            bindEmailOtpForm.setVisible(false);
            bindUserNameForm.setVisible(true);
            bindRegEmailId.setValue("");
        }
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindUserNameForm);
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindEmailOtpForm);

    }

    public void setBindRegEmailId(RichInputText bindRegEmailId) {
        this.bindRegEmailId = bindRegEmailId;
    }

    public RichInputText getBindRegEmailId() {
        return bindRegEmailId;
    }

    public void setBindEmailOtp(RichInputText bindEmailOtp) {
        this.bindEmailOtp = bindEmailOtp;
    }

    public RichInputText getBindEmailOtp() {
        return bindEmailOtp;
    }

    public void setBindUserNameForm(RichPanelFormLayout bindUserNameForm) {
        this.bindUserNameForm = bindUserNameForm;
    }

    public RichPanelFormLayout getBindUserNameForm() {
        return bindUserNameForm;
    }

    public void setBindEmailOtpForm(RichPanelFormLayout bindEmailOtpForm) {
        this.bindEmailOtpForm = bindEmailOtpForm;
    }

    public RichPanelFormLayout getBindEmailOtpForm() {
        return bindEmailOtpForm;
    }

    public void setBindLoginMode(RichSelectOneRadio bindLoginMode) {
        this.bindLoginMode = bindLoginMode;
    }

    public RichSelectOneRadio getBindLoginMode() {
        return bindLoginMode;
    }

    private static final long OTP_VALIDITY = 120 * 1000; // 2 mins

    private static final SecureRandom RANDOM = new SecureRandom();

    public void loginEmailVce(ValueChangeEvent vce) {
        vce.getComponent().processUpdates(FacesContext.getCurrentInstance());
                                                     
        if (chkEmailExist(vce.getNewValue().toString()).equals("N")) {
            ADFUtils.showMessage("Registered Email* does not exist!", 2);
            bindRegEmailId.setValue("");
            return ;
        }
        
        if (bindRegEmailId.getValue().toString() != null) {
            int otpLen = 6;
            char[] val = OTP(otpLen);
            String.format("%06d", RANDOM.nextInt(1000000));
            otpCode = new String(val);
            System.out.println("OTP generated at: " + new Date(otpGeneratedTime));
            otpGeneratedTime = System.currentTimeMillis();
            System.out.println(otpCode);
            System.out.println("OTP generated at: " + new Date(otpGeneratedTime));
        }
    }


    public boolean verifyCaptcha() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);

        if (session != null) {
            String expectedCaptcha = (String) session.getAttribute("SESSION_CAPTCHA");

            if (expectedCaptcha != null && expectedCaptcha.equalsIgnoreCase(verificationCode)) {
                captchaVerified = true;
                //                context.addMessage(null,
                //                                   new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
                //                                                    "CAPTCHA verified successfully!"));
            } else {
                captchaVerified = false;
                context.addMessage(null,
                                   new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                                                    "Invalid Verification Code. Please try again."));
            }
        }
        return captchaVerified;
    }

    // Getters and Setters
    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public boolean isCaptchaVerified() {
        return captchaVerified;
    }

    private long captchaTimestamp = System.currentTimeMillis();

    public void refreshCaptcha(ActionEvent actionEvent) {
        // Generate a new unique timestamp to invalidate any cached image
        this.captchaTimestamp = System.currentTimeMillis();
    }

    public long getCaptchaTimestamp() {
        return captchaTimestamp;
    }

    public void setCaptchaTimestamp(long captchaTimestamp) {
        this.captchaTimestamp = captchaTimestamp;
    }

}
