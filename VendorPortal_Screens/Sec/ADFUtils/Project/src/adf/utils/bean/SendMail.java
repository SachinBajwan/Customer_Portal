package adf.utils.bean;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.Map;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.sql.DataSource;

import oracle.binding.OperationBinding;

import oracle.jbo.Row;
import oracle.jbo.server.ViewObjectImpl;

public class SendMail {
    public static Connection openDataSourceConnection() throws Exception {
        Connection conn = null;
        Context ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/APPLICATIONDBDS");
        conn = ds.getConnection();

        return conn;
    }

    public static void closeDataSourceConnection(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public Map<String, String> InfoFunction() {
        PreparedStatement ps = null;
        ResultSet mailInfo = null;
        Connection conn = null;

        HashMap<String, String> m = new HashMap<String, String>();
        try {
            conn = openDataSourceConnection();
            ps =
                conn.prepareStatement("select HOST,PORT,auth,starttls,username,password,subject,body,signature  from PAYSLIP_MAIL_INFO where key_no=1");
            mailInfo = ps.executeQuery();
            while (mailInfo.next()) {
                m.put("Host", (String) mailInfo.getString(1));
                m.put("Port", (String) mailInfo.getString(2));
                m.put("Auth", (String) mailInfo.getString(3));
                m.put("Starttls", (String) mailInfo.getString(4));
                m.put("Username", (String) mailInfo.getString(5));
                m.put("Password", (String) mailInfo.getString(6));
                m.put("Subject", (String) mailInfo.getString(7));
                m.put("Body", (String) mailInfo.getString(8));
                m.put("Signature", (String) mailInfo.getString(9));


            }
        } catch (Exception e) {
            if (conn != null) {
                closeDataSourceConnection(conn);
            }
        } finally {
            if (conn != null) {
                closeDataSourceConnection(conn);
            }
        }

        return m;
    }


    public static String sendMailMethod(String bodyMsg, String subject, String toUser, String isAnyAtchmnt,
                                  String fileNameNPath,String mailFilename) {

        SendMail objSendMail = new SendMail();
        Map<String, String> mn = (Map<String, String>) objSendMail.InfoFunction();

        System.out.println("Hello" + mn);

        String mailhost = mn.get("Host");

        String mailUsername = mn.get("Username");

        String emailPassword = mn.get("Password");
        ;

        // Setting Properties
        Properties emailProperties = new Properties();
        emailProperties.put("mail.smtp.host", mailhost);
        emailProperties.put("mail.smtp.auth", mn.get("Auth"));
        emailProperties.put("mail.smtp.starttls.enable", mn.get("Starttls"));


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
            if ("Y".equalsIgnoreCase(isAnyAtchmnt)) {
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

            StringBuilder msgEmail = new StringBuilder("<html><body><b>Email Sent</b></body></html>");
            FacesMessage pageMsg = new FacesMessage(msgEmail.toString());
            pageMsg.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("No Network Error !", pageMsg);
            System.out.println("Email sent successfully.");
            return "Y";
        } catch (MessagingException e) {
            System.out.println("somthing went wrong-->Messaging Exception" + e);
            StringBuilder msgEmail = new StringBuilder("<html><body><b>Something went wrong !</b>");
            msgEmail.append("<p><b>please provide correct parameters</b></p>");
            msgEmail.append("</body></html>");
            FacesMessage pageMsg = new FacesMessage(msgEmail.toString());
            pageMsg.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage("No Network Error !", pageMsg);
            return "N";
        }

    }


}
