package UserMaster.view.beans;

import UserMaster.model.module.UserMasAMImpl;


import java.math.BigDecimal;

import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
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

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.input.RichInputComboboxListOfValues;
import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.component.rich.layout.RichPanelHeader;

import oracle.adf.view.rich.component.rich.nav.RichButton;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.binding.OperationBinding;

import oracle.jbo.Row;
import oracle.jbo.ViewObject;
import oracle.jbo.server.ViewObjectImpl;

public class UserB {
    private RichInputText bindUserName;
    private RichInputText bindFname;
    private RichInputText bindMName;
    private RichInputText bindLName;
    private RichInputText bindEmail;
    private RichPanelHeader bindPanelHdr;
    private RichButton bindEditBtn;
    private RichInputComboboxListOfValues bindDeptName;
    private RichInputText bindContactP;
    private RichSelectOneChoice bindCustRole;
    private RichPanelGroupLayout bindMainPanelHdr;

    public UserB() {
    }

    public void chkMobileNo(FacesContext facesContext, UIComponent uIComponent, Object object) {
        if (!chkIndianMobile(object.toString())) {
            FacesMessage msg = new FacesMessage("Invalid Mobile No: " + object.toString());
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }

    }

    public static boolean chkIndianMobile(String s) {

        Pattern p = Pattern.compile("^\\d{10}$");
        Matcher m = p.matcher(s);

        return (m.matches());
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

    public static Object resolveExpression(String expression) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application app = facesContext.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        ValueExpression valueExp = elFactory.createValueExpression(elContext, expression, Object.class);
        return valueExp.getValue(elContext);
    }

    public void saveRecord(ActionEvent actionEvent) {
        String mode = (String) resolveExpression("#{pageFlowScope.mode}");
        String vendorId = (String) resolveExpression("#{pageFlowScope.vendorId}");
        ViewObject userVO = getAm().getUserMasterVO1();
        Row r = (Row) ADFUtils.evaluateEL("#{bindings.PortalVendorUserVO2Iterator.currentRow}");
        try {
            if (mode.equals("C")) {
                String midName = null;
//                if (null == bindMName.getValue()) {
//                    midName = null;
//                } else {
//                    midName = bindMName.getValue().toString();
//                }
//                
                r.setAttribute("Loginname", r.getAttribute("Email"));
                r.setAttribute("UserRole", r.getAttribute("CustRole"));
//                System.out.println("Login Name : " + r.getAttribute("Email"));
                OperationBinding opu = ADFUtils.findOperation("createUser");
//                Object resu = opu.execute();
                ViewObjectImpl vo = getAm().getUserMasterVO1();
                Row cr = vo.createRow();
                cr.setAttribute("UserName", r.getAttribute("Loginname"));
                String password = generate_password(r.getAttribute("Loginname").toString());
                OperationBinding op = ADFUtils.findOperation("encryptedPassword");
                op.getParamsMap().put("password", password);
                Object res = op.execute();
                String encrypt_password = res.toString();
                cr.setAttribute("UserPass", encrypt_password);                
                cr.setAttribute("PartyId", vendorId);
                String userId = (String) ADFUtils.findOperation("findMaxUserIdInUserMaster").execute();
                cr.setAttribute("UserId", userId);
                cr.setAttribute("UserType", "C");
                cr.setAttribute("UserRole", r.getAttribute("CustRole"));
                cr.setAttribute("RefLineId", r.getAttribute("LineId"));
                vo.insertRow(cr);
//                
//
//                ADFUtils.findOperation("findUserIdInUserMaster").execute();
                ADFUtils.findOperation("Commit").execute();
                ADFContext.getCurrent().getPageFlowScope().put("mode", "V");
                AdfFacesContext.getCurrentInstance().addPartialTarget(bindMainPanelHdr);
                ADFUtils.showMessage("User created successfully.", 2);
                ADFUtils.showMessage("Your LoginName is : "+r.getAttribute("Loginname"), 2);
                /*send email to Vendor for password change*/
                String emailId = getBindEmail().getValue().toString();

//                if (emailId != null) {
//                    String MsgBody = "ServiceDesc";
//                    String MsgSubject = "New User Registration";
//                    String Recipient = emailId;
//                    StringBuilder MsgBody1 =
//                        new StringBuilder("<html><body><b><font color='#004d99'>New Registration <font color='#008000'>'" +
//                                          resu.toString() + "'");
//                    StringBuilder MsgBody2 =
//                        new StringBuilder("<html><body><br><br><font color='#990000'><b>Suggest you to change your credentials for safety purpose</b></font></body></html>");
//                    StringBuilder MsgBody3 =
//                        new StringBuilder("<html><body><b><br><br><font color='#008000'>UserLogin : </font><font color='#004d99'>'" +
//                                          resu.toString() +
//                                          "'</font> <font color='#008000'>Password : </font><font color='#004d99'>'" +
//                                          password + "'</font></b></html></body><html>");
//                    StringBuilder MsgBody4 =
//                        new StringBuilder("<html><body><b><br><br><font color='#008000'>Congratulations! You can login on Vendor Portal using provided credentials. Thanks! </font></b></html></body><html>");
//                    String finalmsgbody = (MsgBody4.toString() + MsgBody3.toString() + MsgBody2.toString());
//                    String mailStatus = sendMailMethod(finalmsgbody, MsgSubject, Recipient, "Y", "N", "N");
//                    System.out.println("Email sent Status :" + mailStatus);
//                }
            }
            if (mode.equals("E")) {
                ADFUtils.findOperation("Commit").execute();
//                bindEditBtn.setDisabled(true);
                ADFContext.getCurrent().getPageFlowScope().put("mode", "V");
                AdfFacesContext.getCurrentInstance().addPartialTarget(bindMainPanelHdr);
                ADFUtils.showMessage("Record save successfully.", 2);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

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

    public static String sendMailMethod(String bodyMsg, String subject, String toUser, String isAnyAtchmnt,
                                        String fileNameNPath, String mailFilename) {

        OperationBinding op1 = ADFUtils.findOperation("InfoFunction");
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

    public void setBindUserName(RichInputText bindUserName) {
        this.bindUserName = bindUserName;
    }

    public RichInputText getBindUserName() {
        return bindUserName;
    }

    public void setBindFname(RichInputText bindFname) {
        this.bindFname = bindFname;
    }

    public RichInputText getBindFname() {
        return bindFname;
    }

    public void setBindMName(RichInputText bindMName) {
        this.bindMName = bindMName;
    }

    public RichInputText getBindMName() {
        return bindMName;
    }

    public void setBindLName(RichInputText bindLName) {
        this.bindLName = bindLName;
    }

    public RichInputText getBindLName() {
        return bindLName;
    }

    public void setBindEmail(RichInputText bindEmail) {
        this.bindEmail = bindEmail;
    }

    public RichInputText getBindEmail() {
        return bindEmail;
    }

    public void editRecord(ActionEvent actionEvent) {
        ADFContext.getCurrent().getPageFlowScope().put("mode", "E");
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindPanelHdr);
    }

    public void setBindPanelHdr(RichPanelHeader bindPanelHdr) {
        this.bindPanelHdr = bindPanelHdr;
    }

    public RichPanelHeader getBindPanelHdr() {
        return bindPanelHdr;
    }

    public void setBindEditBtn(RichButton bindEditBtn) {
        this.bindEditBtn = bindEditBtn;
    }

    public RichButton getBindEditBtn() {
        return bindEditBtn;
    }

    public void setBindDeptName(RichInputComboboxListOfValues bindDeptName) {
        this.bindDeptName = bindDeptName;
    }

    public RichInputComboboxListOfValues getBindDeptName() {
        return bindDeptName;
    }

    public void chkEmailDuplicacy(ValueChangeEvent vce) {
        vce.getComponent().processUpdates(FacesContext.getCurrentInstance());
        String email=vce.getNewValue().toString();
        BigDecimal count = new BigDecimal(0);
        BigDecimal count1 = new BigDecimal(0);
        ViewObject emailVO = getAm().getCheckEmailDupVO1();
        emailVO.setNamedWhereClauseParam("bindEmail", email);
        emailVO.executeQuery();
        while (emailVO.hasNext()) {
            Row row = emailVO.next();
            count = (BigDecimal) row.getAttribute("Val");
            System.out.println("Count Exist: " + count);
        }
        if (count.compareTo(BigDecimal.ZERO) > 0) {
            ADFUtils.showMessage("Email Id already exist.", 2);
            bindEmail.setValue("");
            AdfFacesContext.getCurrentInstance().addPartialTarget(bindEmail);
            return;
        }
        
        ViewObject useremailVO = getAm().getCheckUserEmailDupVO1();
        useremailVO.setNamedWhereClauseParam("bindEmail", email);
        useremailVO.executeQuery();
        while (useremailVO.hasNext()) {
            Row row = useremailVO.next();
            count1 = (BigDecimal) row.getAttribute("Val");
            System.out.println("Count Exist: " + count1);
        }
        
        if (count1.compareTo(BigDecimal.ZERO) > 0) {
            ADFUtils.showMessage("Email Id already exist.", 2);
            bindEmail.setValue("");
            AdfFacesContext.getCurrentInstance().addPartialTarget(bindEmail);
            return;
        }
    }
    
    

    public void createDtl(ActionEvent actionEvent) {
        DCBindingContainer bc = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
        OperationBinding operationbinding = bc.getOperationBinding("CreateInsert");
        operationbinding.execute();
    }

    public void onDeleteItem(ActionEvent actionEvent) {
        DCBindingContainer bc = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
        OperationBinding operationbinding = bc.getOperationBinding("Delete");
        operationbinding.execute();
    }

    public void setBindContactP(RichInputText bindContactP) {
        this.bindContactP = bindContactP;
    }

    public RichInputText getBindContactP() {
        return bindContactP;
    }

    public void setBindCustRole(RichSelectOneChoice bindCustRole) {
        this.bindCustRole = bindCustRole;
    }

    public RichSelectOneChoice getBindCustRole() {
        return bindCustRole;
    }

    public void setBindMainPanelHdr(RichPanelGroupLayout bindMainPanelHdr) {
        this.bindMainPanelHdr = bindMainPanelHdr;
    }

    public RichPanelGroupLayout getBindMainPanelHdr() {
        return bindMainPanelHdr;
    }
}
