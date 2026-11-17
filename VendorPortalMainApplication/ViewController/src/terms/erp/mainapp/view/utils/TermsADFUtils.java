package terms.erp.mainapp.view.utils;

import adf.utils.log.LogUtil;
import adf.utils.model.ADFModelUtils;

import java.io.File;
import java.io.IOException;

import java.sql.Types;
import java.io.File;

import java.net.InetAddress;
import java.net.UnknownHostException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import java.util.Collection;

import java.util.Iterator;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;

import javax.naming.InitialContext;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.sql.DataSource;

import oracle.adf.model.BindingContext;
import oracle.adf.model.DataControlFrame;
import oracle.adf.model.binding.DCDataControl;
import oracle.adf.share.logging.ADFLogger;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.jbo.JboWarning;
import oracle.jbo.Row;
import oracle.jbo.ViewCriteria;
import oracle.jbo.server.ViewObjectImpl;

public class TermsADFUtils {
    static ADFLogger _logger = ADFLogger.createADFLogger(TermsADFUtils.class);

    public TermsADFUtils() {
        super();
    }

    public static FacesMessage showErrorMessage(String header, String text) {
        return new FacesMessage(FacesMessage.SEVERITY_ERROR, header, text);
    }

    public static FacesMessage showInfoMessage(String header, String text) {
        return new FacesMessage(FacesMessage.SEVERITY_INFO, header, text);
    }


    public static FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    public static ExternalContext getExternalContext() {
        return getFacesContext().getExternalContext();
    }

    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) getExternalContext().getRequest();
    }

    public static HttpServletResponse getResponse() {
        return (HttpServletResponse) getExternalContext().getResponse();
    }

    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    public static void setValueToSession(String key, Object value) {
        _logger.info("setting value for....................... " + key + "=" + value);
        getRequest().getSession().setAttribute(key, value);
    }

    public static Object getValueFromSession(String key) {
        Object obj = getRequest().getSession().getAttribute(key);
        if (obj == null || obj.toString().trim().length() == 0) {
            return null;
        } else {
            return obj;
        }
    }

    public static void sendForward(HttpServletRequest request, HttpServletResponse response, String forwardUrl) {

        FacesContext ctx = FacesContext.getCurrentInstance();

        RequestDispatcher dispatcher = request.getRequestDispatcher(forwardUrl);
        try {
            dispatcher.forward(request, response);
        } catch (ServletException se) {
            se.printStackTrace();
            showErrorMessage("Servlet Exception", se.getMessage());
        } catch (IOException ie) {
            ie.printStackTrace();
            showErrorMessage("IO Exception", ie.getMessage());
        }
        ctx.responseComplete();
    }

    public static void sendForward(String forwardUrl) {
        sendForward(getRequest(), getResponse(), forwardUrl);
    }

    public static void refreshCompoenent(UIComponent compoenent) {
        AdfFacesContext.getCurrentInstance().addPartialTarget(compoenent);
    }

    public static MethodBinding getMethodBinding(String exp, Class[] params) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Application app = fc.getApplication();
        MethodBinding mb = app.createMethodBinding(exp, params);
        return mb;
    }

    public static Object getBean(String bean) {
        //System.out.println("Get bean Called for : "+bean);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application application = facesContext.getApplication();
        return facesContext.getApplication().getVariableResolver().resolveVariable(facesContext, bean);
    }


    public static String reloadThePage() {
        rollbackPartialTransactions();
        FacesContext fContext = FacesContext.getCurrentInstance();
        String viewId = fContext.getViewRoot().getViewId();
        String actionUrl = fContext.getApplication().getViewHandler().getActionURL(fContext, viewId);
        try {
            ExternalContext eContext = fContext.getExternalContext();
            String resourceUrl = actionUrl; //eContext.encodeResourceURL(actionUrl);
            // Use the action URL directly since the encoding a resource URL will NPE in isEmailablePage()
            eContext.redirect(resourceUrl);

        } catch (IOException ioe) {

            ioe.printStackTrace();
        }
        return null;
    }


    public static String rollbackPartialTransactions() {
        BindingContext bc = BindingContext.getCurrent();
        DataControlFrame dcf = bc.findDataControlFrame(bc.getCurrentDataControlFrame());
        Collection<DCDataControl> dcCol = dcf.datacontrols();
        //System.out.println("dcCol.size() = " + dcCol.size());
        for (DCDataControl dc : dcCol) {
            // System.out.println("dc = " + dc);
            if (dc.isTransactionDirty()) {
                //  System.out.println("dc.isTransactionDirty() = " + dc.isTransactionDirty());
                try {
                    dc.rollbackTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    /**
     * Method to create expression for taskflow calling on the basis of taskflow name
     * @param tfName
     * @return
     */
    public static String getTaskFlowId(String tfName) 
    {
        return "/WEB-INF/" + tfName + ".xml#" + tfName;
    }


    /**
     *This funtion will get Defalut getUserId
     * @param am
     * @return
     */
    public static String findUserId(String userName) 
    {
        Object obj = null;
        String usrId = null;
//        try 
//        {
//            obj = DBConfiguration.callFunction(Types.VARCHAR, "UX_FIND_USER_ID(?)", new Object[] { userName });
//
//            System.out.println(" result of UX_FIND_USER:" + obj);
//            if (obj != null) {
//                System.out.println("User Id is----->>" + obj.toString());
//                usrId = obj.toString();
//                return usrId;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return usrId;
    }

    /**Created By:Aakash Kumar
     * Created On:24-FEB-2020
     * Remarks: Validate the user password length and its charactors.
     */
    public static String checkUserPassword(String password) 
    {
        String result="F";
        try 
        {
            if(password!=null)
            {
                Object res = DBConfiguration.callFunction(Types.VARCHAR, "TERMS.GET_PWD_COMPLEX(?)", new Object[] {password});
                _logger.info("res value checkUserPassword--->>"+res);
                if("TRUE".equalsIgnoreCase(res.toString()))
                {
                    result= "T"; 
                }
                else{
                    result=res.toString();
                }
            }
        } catch (Exception e) 
        {
            e.printStackTrace();
            //ADFModelUtils.showFormattedFacesMessage("", e.getMessage(), FacesMessage.SEVERITY_INFO);
            return e.getMessage();
        }
        return result;
    }
    
    /**Created By:Aakash Kumar
     * Created On:24-FEB-2020
     * Remarks:Method to Encrypt Password.
     */
    public static String encryptedPassword(String password) 
    {
        String encryptPass=null;
        if(password!=null)
        {
            try
            {   
                Object rs = DBConfiguration.callFunction(Types.VARCHAR, "TERMS.ENCRYP_PWD(?)", new Object[] {password});
                _logger.info("The Value of encrypedPass Password--->>"+rs);
                    if(rs!=null)
                    {
                        encryptPass= rs.toString();
                        _logger.info("The encrypedPass---->>"+encryptPass);
                    }
            }
            catch(Exception e)
            {
                e.printStackTrace();
                //ADFModelUtils.showFormattedFacesMessage("", e.getMessage(), FacesMessage.SEVERITY_INFO);
                return e.getMessage();
            } 
        }
    return encryptPass;
    }
    
    /**Created By:Aakash Kumar
     * Created On:24-FEB-2020
     * Remarks: Method to Decrypt Password.
     */
    public static String decryptedPassword(String password) 
    {
        String decryptPass=null;
        if(password!=null)
        {
            try
            {   
                Object rs = DBConfiguration.callFunction(Types.VARCHAR, "TERMS.GET_DECRUP_PWD(?)", new Object[] {password});
               // System.out.println("The Function Value of Decrpted Password--->>"+rs);
                if(rs!=null)
                {
                    decryptPass= rs.toString();
                    //logger.info("The decrypPass---->>"+decrypPass);
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
                //ADFModelUtils.showFormattedFacesMessage("", e.getMessage(), FacesMessage.SEVERITY_INFO);
                return e.getMessage();
            }
        }
        return decryptPass;
    }
    
    
    public static String updateChangePasswordHistory(String userName,String newpassword, String oldpassword,String userId) 
    {
        String flag="N";
        String encrpypass=null;
        if(userId!=null && newpassword!=null)
        {   
           Object obj = null;
           try
           {
               String terminal = getClientIpAddress() + "/" +getClientCompName();
               obj = DBConfiguration.callFunction(Types.VARCHAR, "UX_UPDATE_PWD_DTL(?,?,?,?,?)",
                                       new Object[]{userId,userName,newpassword,oldpassword,terminal});
               
               System.out.println("|UX_UPDATE_PWD_DTL| result of UX_UPDATE_PWD_DTL:"+obj);
               if(obj!=null)
               {
                   if(obj.toString().equalsIgnoreCase("Y"))
                   {
                        return "Y";
                   }
               }
           } catch (Exception e) 
           {
               e.printStackTrace();
               flag= "N";
           }
       }
        return "N";
    }
    
    
    public static Object resolveExpression(String expression) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application app = facesContext.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        ValueExpression valueExp =
            elFactory.createValueExpression(elContext, expression,
                                            Object.class);
        return valueExp.getValue(elContext);
    }
    
    /**
    * Programmatic evaluation of EL.
    *
    * @param el EL to evaluate
    * @return Result of the evaluation
    */
    public static Object evaluateEL(String el) {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    ELContext elContext = facesContext.getELContext();
    ExpressionFactory expressionFactory =
    facesContext.getApplication().getExpressionFactory();
    ValueExpression exp =
    expressionFactory.createValueExpression(elContext, el,
    Object.class);

    return exp.getValue(elContext);
    }
    
    /**
       * Locate an UIComponent in view root with its component id. Use a recursive way to achieve this.
       * Taken from http://www.jroller.com/page/mert?entry=how_to_find_a_uicomponent
       * @param id UIComponent id
       * @return UIComponent object
       */

    public static UIComponent findComponentInRoot(String id)
    {
      UIComponent component = null;
      FacesContext facesContext = FacesContext.getCurrentInstance();
      if (facesContext != null) 
      {
          UIComponent root = facesContext.getViewRoot();
          component = findComponent(root, id);
      }
      return component;
    }
    
    /**
        * Locate an UIComponent from its root component.
        * Taken from http://www.jroller.com/page/mert?entry=how_to_find_a_uicomponent
        * @param base root Component (parent)
        * @param id UIComponent id
        * @return UIComponent object
        */

    public static UIComponent findComponent(UIComponent base, String id) 
    {
       if (id.equals(base.getId()))
           return base;
       UIComponent children = null;
       UIComponent result = null;
       Iterator<UIComponent> childrens = base.getFacetsAndChildren();

       while (childrens.hasNext() && (result == null)) {
           children = (UIComponent)childrens.next();
           if (id.equals(children.getId())) {
               result = children;
               break;
           }
           result = findComponent(children, id);

           if (result != null) {
               break;
           }
       }
       return result;
    }

    
    public static void setValueToPageFlowScope(String key, Object value){
        AdfFacesContext.getCurrentInstance().getPageFlowScope().put(key, value);    
    }
    
    public static Object getValueFromPageFlowScope(String key) {
        Object obj = AdfFacesContext.getCurrentInstance().getPageFlowScope().get(key);
        if (obj == null || obj.toString().trim().length() == 0) {
            return null;
        } else {
            return obj;
        }
    }
    
    public static String gblLocalServer() 
    {
        Object elExp = evaluateEL("#{pageFlowScope.G_LOCAL_SERVER}");
        if (elExp != null) 
        {
            System.out.println("------G_LOCAL_SERVER()---->>Returned-->>:"+elExp);
            if (LogUtil.enableLogger) 
            {
                _logger.fine("------G_LOCAL_SERVER()---->>Returned-->>:"+elExp);
            }
            return elExp.toString();
        } 
        else 
        {
            _logger.severe("Warning : Method | gblLocalServer() | gblLocalServer not defined.");
            if (LogUtil.enableLogger) {
                _logger.info("Info------G_LOCAL_SERVER()---->>Returned Null-->>:"+elExp);
            }
            return null;
        }
    }
    
    public static String getClientIpAddress() {
        try {
            String clientIpAddress =getRequest().getRemoteAddr();
            return clientIpAddress;
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return null;
    }

    public static String getClientCompName() {
        try {
            
            InetAddress i = InetAddress.getByName(getRequest().getRemoteAddr());
            //InetAddress i = InetAddress.getLocalHost();
            String computerName = i.getHostName();
            _logger.info("Hostname : " + computerName + "  " + i.getCanonicalHostName());
            return computerName;
        } catch (UnknownHostException uhe) {
            // TODO: Add catch code
            uhe.printStackTrace();
        }
        return null;
    }
    
    public static String getSessionId() {
        try {
            String sessionId =getRequest().getSession(false).getId();
            return sessionId;
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return null;
    }
    
    public static String getUserId() {
        try {
            String userId =TermsADFUtils.getValueFromPageFlowScope("userId").toString();
            return userId;
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return null;
    }
    
    
    
    public static String getVSession() {

        String vSession = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = getConnection();

            String httpSessionId =
                TermsADFUtils.getSessionId();

            if (httpSessionId == null || httpSessionId.trim().isEmpty()) {
                TermsADFUtils.showErrorMessage(
                    "SESSION ID",
                    "HTTP SESSION ID IS NULL"
                );
                return null;
            }

            String query =
                "SELECT V_SESSIONID " +
                "FROM USER_SESSION " +
                "WHERE HTTP_SESSION_NO = ?";

            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, httpSessionId);

            rs = pstmt.executeQuery();

            if (rs.next()) {

                vSession = rs.getString("V_SESSIONID");

                if (vSession != null && !vSession.trim().isEmpty()) {
                    return vSession;
                }

                TermsADFUtils.showErrorMessage(
                    "SESSION ID",
                    "V_SESSIONID NOT FOUND IN USER_SESSION"
                );

                return null;
            }

            TermsADFUtils.showErrorMessage(
                "SESSION ID",
                "SESSION ID NOT FOUND IN USER_SESSION"
            );

        } catch (Exception ex) {

            ex.printStackTrace();

            TermsADFUtils.showErrorMessage(
                "Exception",
                ex.getMessage()
            );

        } finally {

            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
    
    
    
        public static Connection getConnection() throws Exception {
        InitialContext initialContext = new InitialContext();
        DataSource ds = (DataSource) initialContext.lookup("java:comp/env/jdbc/APPLICATIONDBDS");
        java.sql.Connection conn = ds.getConnection();
        return conn;
    }
    
}
