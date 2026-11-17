package terms.erp.mainapp.view.bean;

import adf.utils.model.ADFModelUtils;

import java.io.Serializable;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

import java.util.HashMap;
import java.util.Map;

import java.util.Random;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;

import oracle.adf.view.rich.component.rich.RichDialog;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.component.rich.nav.RichButton;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.DialogEvent;
import oracle.adf.view.rich.event.PopupCanceledEvent;

import oracle.binding.OperationBinding;

import oracle.jbo.JboException;
import oracle.jbo.RowSetIterator;

import terms.erp.mainapp.model.services.TermsMainAppAMImpl;
import terms.erp.mainapp.view.utils.ADFBeanUtils;
import terms.erp.mainapp.view.utils.AMConfigUtils;
import terms.erp.mainapp.view.utils.DBConfiguration;
import terms.erp.mainapp.view.utils.TermsADFUtils;

public class ResetPasswordBean implements Serializable {
    private RichInputText userNameBind;
    private RichDialog pwdDialogBind;
    private RichPanelGroupLayout userNamePanelBind;
    private RichPanelGroupLayout otpPanelFormBind;
    private RichInputText otpFieldBind;
    private RichInputText passwordBind;
    private RichInputText confirmPasswordBind;
    private RichPanelGroupLayout pwdChangePanelFormBind;
    private Integer OTP = null;
    private String userId = null;
    private RichButton bindResetBtn;

    public ResetPasswordBean() {
        super();
    }

    private String pswdMode = "U";
    
   
    public void setPswdMode(String pswdMode) {
        this.pswdMode = pswdMode;
    }

    public String getPswdMode() {
        return pswdMode;
    }
    
    public void popUpCancelListener(PopupCanceledEvent popupCanceledEvent) 
    {
        this.pswdMode = "U";
        this.getPasswordBind().setValue(null);
        this.getOtpFieldBind().setValue(null);
        this.getConfirmPasswordBind().setValue(null);
        
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPasswordBind());
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getOtpFieldBind());
        
        this.refreshPanelGroups();
    }

    public void passwordDialogDL(DialogEvent dialogEvent) 
    {
      if (dialogEvent.getOutcome().toString().equalsIgnoreCase("Ok")) {
          String message = this.changePassWordIn();
          if (message.equalsIgnoreCase("Y")) 
          {
              FacesContext.getCurrentInstance().addMessage(null,TermsADFUtils.showInfoMessage("Success",
                                                                    "Your Password has been reset Successfully."));
          } else 
          {
              FacesContext.getCurrentInstance().addMessage(null,
                                                           TermsADFUtils.showErrorMessage("Error",message));
              return;
          }
        }
      
        this.pswdMode = "U";
        this.getPasswordBind().setValue(null);
        this.getOtpFieldBind().setValue(null);
        this.getConfirmPasswordBind().setValue(null);
        
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPasswordBind());
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getOtpFieldBind());
        
        this.refreshPanelGroups();
    }

    public void validateUserAL(ActionEvent actionEvent) 
    {
        if (this.getUserNameBind().getValue() == null || this.getUserNameBind().getValue().toString().trim().length() == 0)
        {
            FacesContext.getCurrentInstance().addMessage(this.getUserNameBind().getClientId(),
                                                         TermsADFUtils.showErrorMessage("Mandatory",
                                                                                       "Please Enter User Name"));
            //Manadatory, Please enter your name
            return;
        }

        String msg = this.validateUserDetails(this.getUserNameBind().getValue().toString());

        if (msg.equalsIgnoreCase("Y")) 
        {
            this.userId = TermsADFUtils.findUserId(this.getUserNameBind().getValue().toString());

            msg = this.setOTP(this.userId);

            if (msg.equalsIgnoreCase("Y"))
            {
                this.pswdMode = "A";
            } 
            else 
            {
                FacesContext.getCurrentInstance().addMessage(null,TermsADFUtils.showErrorMessage("Error",msg));
            }

        } 
        else 
        {
            FacesContext.getCurrentInstance().addMessage(null,TermsADFUtils.showErrorMessage("Error",msg));
        }
        
        this.refreshPanelGroups();
    }
    

    private String validateUserDetails(String userName)
    {
        Object obj = null;
        String msg="";
        if(userName!=null)
        {
            try
            {
                obj = DBConfiguration.callFunction(Types.VARCHAR, "UX_FIND_USER(?)", new Object[]{userName});
                
                System.out.println(" result of UX_FIND_USER:"+obj);   
                if(obj!=null)
                {
                    String res = obj.toString().substring(0, 1);
                    msg =obj.toString().substring(obj.toString().indexOf("-") +1);
                    
                    if(res.equalsIgnoreCase("Y"))
                    {
                        return "true";
                    }
                }
            } catch (Exception e) 
            {
                e.printStackTrace();
                msg=e.fillInStackTrace().toString();
                FacesContext.getCurrentInstance().addMessage(null,TermsADFUtils.showErrorMessage("Error",msg));
                return "E";
            }
        }
        FacesContext.getCurrentInstance().addMessage(null,TermsADFUtils.showErrorMessage("Error",msg));
        
        return "T";
    }
    
        
    public void setPwdDialogBind(RichDialog pwdDialogBind) {
        this.pwdDialogBind = pwdDialogBind;
    }

    public RichDialog getPwdDialogBind() {
        return pwdDialogBind;
    }

    public void setUserNameBind(RichInputText userNameBind) {
        this.userNameBind = userNameBind;
    }

    public RichInputText getUserNameBind() {
        return userNameBind;
    }


    public void setUserNamePanelBind(RichPanelGroupLayout userNamePanelBind) {
        this.userNamePanelBind = userNamePanelBind;
    }

    public RichPanelGroupLayout getUserNamePanelBind() {
        return userNamePanelBind;
    }

    public void setOtpPanelFormBind(RichPanelGroupLayout otpPanelFormBind) {
        this.otpPanelFormBind = otpPanelFormBind;
    }

    public RichPanelGroupLayout getOtpPanelFormBind() {
        return otpPanelFormBind;
    }

    public void setOtpFieldBind(RichInputText otpFieldBind) {
        this.otpFieldBind = otpFieldBind;
    }

    public RichInputText getOtpFieldBind() {
        return otpFieldBind;
    }

    public void sendOTPActionAL(ActionEvent actionEvent)
    {
        String msg = this.setOTP(this.userId);

        if (msg.equalsIgnoreCase("Y")) 
        {
            FacesContext.getCurrentInstance().addMessage(null,TermsADFUtils.showInfoMessage("Error",
                                                                    "OTP has been sent to your verified Email Id"));
        } 
        else 
        {
            FacesContext.getCurrentInstance().addMessage(null,TermsADFUtils.showErrorMessage("Error",msg));
        }
    }

    public void goToChangePasswordAL(ActionEvent actionEvent) 
    {
        if (this.getOtpFieldBind().getValue() == null || this.getOtpFieldBind().getValue().toString().trim().length() == 0)
        {
            FacesContext.getCurrentInstance().addMessage(this.getOtpFieldBind().getClientId(),
                        TermsADFUtils.showErrorMessage("Error","Please Enter Otp."));;

            return;
        }

        if (!this.getOtpFieldBind().getValue().toString().equalsIgnoreCase(this.OTP.toString())) 
        {
            FacesContext.getCurrentInstance().addMessage(null,
                        TermsADFUtils.showErrorMessage("Validation","OTP is not correct"));;
            //"Validation", "OTP is not correct"
            return;
        }

        this.pswdMode = "O";

        this.refreshPanelGroups();
    }

    public void setPasswordBind(RichInputText passwordBind) {
        this.passwordBind = passwordBind;
    }

    public RichInputText getPasswordBind() {
        return passwordBind;
    }

    public void setConfirmPasswordBind(RichInputText confirmPasswordBind) {
        this.confirmPasswordBind = confirmPasswordBind;
    }

    public RichInputText getConfirmPasswordBind() {
        return confirmPasswordBind;
    }

    public void validatePassword(FacesContext facesContext, UIComponent uIComponent, Object object) 
    {
        if (this.pswdMode != "O")
            return;

        if (object == null || object.toString().trim().length() == 0) 
        {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,"Mandatory",
                                                          "Please enter your Password")); 
        }
        
        int spc = this.findSpaceCount(object.toString());

        if (spc > 0) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,"Validation Failed",
                                                         "Password can not contain space"));
        }
        if (object.toString().trim().length() < 8 || object.toString().trim().length() > 20)
        {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,"Validation Failed",
                                           "Length should be more than 7 and less than 21"));
        }
    }
    
    public void validateConfirmPassword(FacesContext facesContext, UIComponent uIComponent, Object object) 
    {
        if (this.pswdMode != "O")
            return;

        if (this.getPasswordBind().getValue() == null || this.getPasswordBind().getValue().toString().trim().length() == 0) 
        {
            FacesContext.getCurrentInstance().addMessage(this.getPasswordBind().getClientId(),
                        TermsADFUtils.showErrorMessage("Mandatory","Please enter Password"));;
            return;
        }

        if (object == null || object.toString().trim().length() == 0) 
        {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,"Mandatory",
                                                          "Please enter Password"));
        }

        if (object.toString().length() != object.toString().trim().length()) 
        {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,"Validation Failed",
                    "Password can not contain space character"));
        }

        if (!object.toString().trim().equalsIgnoreCase(this.getPasswordBind().getValue().toString().trim())) 
        {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,"Validation Failed",
                                        "Password does not match."));
        }
    }
    /**
     *method to check spaces in password
     * @param s
     * @return
     */
    public int findSpaceCount(String s) {
        int i, count = 0, res;
        for (i = 0, count = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == ' ')
                count++;
        }
        return count;
    }


    /**
     *This Fucntion set the OTP in instance variable
     * @param userId
     * @return
     */
    public String setOTP(String userId) {
        String message = "Y";

        this.getOtp();
        message = this.sendMail(userId);


        return message;
    }
    
    /**
      * This Logic Will generate Random OTP
      */
     public void getOtp() 
    {
         Random randomGenerator = new Random();
         Integer random = randomGenerator.nextInt(1000) + 1000 * (randomGenerator.nextInt(9) + 1);
         System.out.println("OTP For password change ::::" + random);
         this.OTP = random;

         return;
     }
    
    /**
     *This function will send Mail To USer with OTP
     */

    private String sendMail(String userId) 
    {
        String message = "Y";
//        TermsMainAppAMImpl am = AMConfigUtils.getAM();
//        
//        System.out.println("VALUE OF AM IN change password bean"+am);
//        String UserName = this.getUserNameBind().getValue().toString();
//
//        Map<String, String> config = null;
//        try 
//        {
//            config = this.getConfig("UX_MAIL_DETAIL_FOR_USER(?,?,?,?,?,?,?,?,?)", am, userId, userId);
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            message = "Default Mail ID is not configured or uncheck Send Otp to Others from Utilities";
//            return message;
//        }
//        if (config != null) 
//        {
//            MailFactory fc = new MailFactory();
//
//            fc.setAsSender();
//            MailService ms = fc.getMailService(config.get("Domain"), config.get("Port"), config.get("Security"));
//
//            ms.setSender(config.get("From"));
//            ms.setReciever(config.get("Rcv"));
//            ms.setSubject("OTP For Password Update for - "+UserName);
//
//            ms.addLine("Your OTP is : " + this.OTP);
//
//            ms.send(config.get("From"), config.get("Password"));
//
//        }
//        //  Configuration.releaseRootApplicationModule(am, true);
//        AMConfigUtils.releaseAM();

        return message;
    }
    
    
    /**
     *This Function will retun the Mail Related configration for the user
     * @param stmt
     * @param am
     * @return
     * @throws Exception
     */
    private Map<String, String> getConfig(String stmt, TermsMainAppAMImpl am, Integer userId,
                                          Integer usrId) throws Exception {
        try (CallableStatement sql = am.getDBTransaction().createCallableStatement("begin  " + stmt + ";end;", 0)) {

            Map<String, String> map = new HashMap<>();


            sql.setObject(1, userId);
            sql.setObject(2, null);
            sql.setObject(3, usrId);
            sql.registerOutParameter(4, Types.VARCHAR);
            sql.registerOutParameter(5, Types.VARCHAR);
            sql.registerOutParameter(6, Types.VARCHAR);
            sql.registerOutParameter(7, Types.VARCHAR);
            sql.registerOutParameter(8, Types.VARCHAR);
            sql.registerOutParameter(9, Types.VARCHAR);
            sql.execute();
            map.put("Domain", sql.getString(4));
            map.put("Port", sql.getString(5));
            map.put("Security", sql.getString(6));
            map.put("From", sql.getString(7));
            map.put("Password", sql.getString(8));
            map.put("Rcv", sql.getString(9));
            return map;
        } catch (SQLException e) {
            e.printStackTrace();

            throw new JboException(e.getMessage());
        }
    }
    
    
    /**
     *This Will update password in databse and weblogic
     * @return
     */

    public String changePassWordIn() 
    {
        String message = "Y";

        Boolean valid = true;

        if (this.getPasswordBind().getValue() == null || this.getPasswordBind().getValue().toString().trim().length() == 0) 
        {
            message = "Please enter your Password";
            return message;
        }
        if (this.getConfirmPasswordBind().getValue() == null || this.getConfirmPasswordBind().getValue().toString().trim().length() == 0) 
        {
            message = "Please enter your Confirm Password";
            return message;
        }
        String val=null;
        TermsMainAppAMImpl am = AMConfigUtils.getAM();
//        try (CallableStatement stmt =
//             am.getDBTransaction().createCallableStatement("begin ?:=" + "UX_UPDATE_PWD(?,?,?,?)" + ";end;", 0)) {
//            stmt.registerOutParameter(1, Types.VARCHAR);
//            stmt.setString(2,"0000");
//            stmt.setInt(3, this.slocId);
//            stmt.setInt(4, -1);
//            stmt.setInt(5, this.userId);
//            stmt.setString(6, this.getBindPwd().getValue().toString());
//            stmt.execute();
//            val = stmt.getString(1);
//        } catch (Exception e) {
//            valid = false;
//            e.printStackTrace();
//            message = e.getMessage();
//
//        }
//        if(val !=null)
//        {
//            if(val.equals("N")){
//              return "Password is not as per policy.";
//            }
//            else if(!val.equals("Y")){
//                return val;
//            }
//        }
//        try (CallableStatement stmt =
//             am.getDBTransaction().createCallableStatement("begin ?:=" + "UX_UPDATE_PWD(?,?,?)" + ";end;", 0)) 
//        {
//
//
//            stmt.registerOutParameter(1, Types.VARCHAR);
//            stmt.setInt(2, this.userId);
//            stmt.setInt(3, this.empCode);
//            stmt.setString(4, this.getBindPwd().getValue().toString());
//            stmt.execute();
//            valid=true;
//        } catch (Exception e) {
//            valid = false;
//            e.printStackTrace();
//            message = e.getMessage();
//
//        }
        
        if (valid) 
        {
            am.getDBTransaction().commit();

        } else {
            am.getDBTransaction().rollback();
        }

    //        try {
    //            WeblogicUser.closeConnection();
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
        AMConfigUtils.releaseAM();
        return message;
    }
    
    public void setPwdChangePanelFormBind(RichPanelGroupLayout pwdChangePanelFormBind) {
        this.pwdChangePanelFormBind = pwdChangePanelFormBind;
    }

    public RichPanelGroupLayout getPwdChangePanelFormBind() {
        return pwdChangePanelFormBind;
    }
    
    public void refreshPanelGroups() 
    {
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getUserNamePanelBind());
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getOtpPanelFormBind());
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPwdChangePanelFormBind());
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPwdDialogBind());
    }
    
    
    public void validateResetPassword(ActionEvent actionEvent) {
        try {
            TermsMainAppAMImpl am = AMConfigUtils.getAM();
            FacesContext facesContext = FacesContext.getCurrentInstance();
            UIViewRoot root = facesContext.getViewRoot();
            RichInputText inputText = (RichInputText) root.findComponent("it1");
            RichInputText inputText1 = (RichInputText) root.findComponent("it2");
            RichInputText inputText2 = (RichInputText) root.findComponent("it3");
            RichInputText inputText3 = (RichInputText) root.findComponent("it4");
            
            String regNo = (String) inputText.getValue();
            String pan = (String) inputText3.getValue();
            String pass = (String) inputText1.getValue();
            String confirmPass = (String) inputText2.getValue();
            
            OperationBinding op = null;

            System.out.println("Registraion No : " + regNo);
            System.out.println("PAN : " + pan);
            System.out.println("Password : " + pass);
            System.out.println("Confirm Password : " + confirmPass);

            if (regNo == null) {
                ADFBeanUtils.showFacesMessageInfo("", "Please Enter Customer Code");
                return;
            }
            
            if (pan == null) {
                ADFBeanUtils.showFacesMessageInfo("", "Please Enter PAN");
                return;
            }

            if (pass == null) {
                ADFBeanUtils.showFacesMessageInfo("", "Please Enter Password");
                return;
            }
            if (confirmPass == null) {
                ADFBeanUtils.showFacesMessageInfo("", "Please Enter Confirm Password");
                return;
            }
            if (confirmPass.equals(pass)) {
                op = ADFBeanUtils.findOperation("resetPassword");
                op.getParamsMap().put("regno", regNo);
                op.getParamsMap().put("pan", pan);
                op.getParamsMap().put("pass", pass);
                op.execute();
                if(op.getResult().equals("Y"))
                {
                        am.getDBTransaction().commit();
                        inputText.setDisabled(true);
                        inputText1.setDisabled(true);
                        inputText2.setDisabled(true);
                        inputText3.setDisabled(true);
//                        bindResetBtn.setDisabled(true);
                        AdfFacesContext.getCurrentInstance().addPartialTarget(inputText);
                        AdfFacesContext.getCurrentInstance().addPartialTarget(inputText1);
                        AdfFacesContext.getCurrentInstance().addPartialTarget(inputText2);
                        AdfFacesContext.getCurrentInstance().addPartialTarget(inputText3);
//                        AdfFacesContext.getCurrentInstance().addPartialTarget(bindResetBtn);
                        
                        ADFBeanUtils.showFacesMessageInfo("", "Password updated successfully.");
                    }
                else
                {
                        ADFBeanUtils.showFacesMessageInfo("", "Registration No. or PAN is not correct. Please check.");
                    return;
                    }
            }
            else{
                ADFBeanUtils.showFacesMessageInfo("", "Password and Confirm Password not match");
                return;
            }
            
//            System.out.println("--authenticateUserPassword--Result---::" + op.getResult());
//            if (op.getResult().equals("S")) {
//                tempLoginPanel.setVisible(false);
//                createLoginPanel.setVisible(true);
//                bindPassword.setDisabled(false);
//                bindCpassword.setDisabled(false);
//                bindCreateLoginBtn.setDisabled(false);
//                AdfFacesContext.getCurrentInstance().addPartialTarget(tempLoginPanel);
//                AdfFacesContext.getCurrentInstance().addPartialTarget(createLoginPanel);
//                AdfFacesContext.getCurrentInstance().addPartialTarget(bindPassword);
//                AdfFacesContext.getCurrentInstance().addPartialTarget(bindCpassword);
//                AdfFacesContext.getCurrentInstance().addPartialTarget(bindCreateLoginBtn);
//            } else if (op.getResult().equals("F")) {
//                StringBuilder msg =
//                    new StringBuilder("<html><body><b>Temporary Id and Password not matched.</b></body></html>");
//                ADFBeanUtils.showFacesMessageInfo("", msg.toString());
//            } else if (op.getResult().equals("U")) {
//                StringBuilder msg = new StringBuilder("<html><body><b>Temporary Id is already used.</b></body></html>");
//                ADFBeanUtils.showFacesMessageInfo("", msg.toString());
//            }
//            AdfFacesContext.getCurrentInstance().addPartialTarget(bindUserLogin);
        } catch (Exception e) {
            e.printStackTrace();
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


    public void validatePAN(FacesContext facesContext, UIComponent uIComponent, Object object) {
        System.out.println("PAN : "+object.toString().toUpperCase());
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

    public void PanVce(ValueChangeEvent e) {
        e.getComponent().processUpdates(FacesContext.getCurrentInstance());
        if(e.getNewValue()!=null){
            try {
            TermsMainAppAMImpl am = AMConfigUtils.getAM();
            FacesContext facesContext = FacesContext.getCurrentInstance();
            UIViewRoot root = facesContext.getViewRoot();
            String pan = e.getNewValue().toString().toUpperCase();
            RichInputText inputText3 = (RichInputText) root.findComponent("it4");
            inputText3.setValue(pan);
            AdfFacesContext.getCurrentInstance().addPartialTarget(inputText3);
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
                }
            
        }
    }

    public void setBindResetBtn(RichButton bindResetBtn) {
        this.bindResetBtn = bindResetBtn;
    }

    public RichButton getBindResetBtn() {
        return bindResetBtn;
    }
}
