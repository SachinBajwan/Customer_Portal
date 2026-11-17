package adf.utils.model;

import adf.utils.exception.InvalidArgumentException;
import adf.utils.exception.ADFUtilsException;
import adf.utils.log.LogUtil;
import adf.utils.model.dbprocedure.ProcParams;
import javax.naming.Context;

import javax.naming.InitialContext;
import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;

import java.util.List;

import javax.el.ELContext;
import javax.el.ValueExpression;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import oracle.adf.share.ADFContext;
import oracle.adf.share.logging.ADFLogger;
import oracle.jbo.ApplicationModule;
import oracle.jbo.JboException;
import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewCriteria;
import oracle.jbo.ViewCriteriaItem;
import oracle.jbo.ViewCriteriaRow;
import oracle.jbo.server.ApplicationModuleImpl;
import oracle.jbo.server.DBTransaction;
import oracle.jbo.server.ViewObjectImpl;

/**
 * This class basically contains the Methods that are commonly used in Model Part of ADF Application.
 * Although these methods can be called from anywhere.
 */
public class ADFModelUtils
{
    private static String dbDriverName = null;

    private static ADFLogger logger = ADFLogger.createADFLogger(ADFModelUtils.class);

    public ADFModelUtils() {
        super();
    }

    public static Object resolvEl(String expression) 
    {
        Object o = null;
        if (expression != null) 
        {
            ADFContext adfCtx = ADFContext.getCurrent();
            ELContext elContext = adfCtx.getELContext();
            ValueExpression valueExp =
                adfCtx.getExpressionFactory().createValueExpression(elContext, expression, Object.class);
            o = valueExp.getValue(elContext);
        } else {
            logger.severe("Warning : Method | resolvEl(StringBuilder expression) | called with parameter as null.");
        }
        if (LogUtil.enableLogger) {
            logger.fine("Method | resolvEl(StringBuilder expression) | called with following parameters ");
            logger.fine("=======> resolvEl(" + expression + ") | Returned : " + o);
        }
        return o;
    }

    /**
     * Method to Call Function or Procedure for ADF.
     * @param am  ApplicationModuleImpl instance
     * @param sqlStatmnt  Sql Statement that need to be executed
     * @param params  Parameters that need to be put in the fucntion
     * @param returnTyp  Object which is the result
     */
    public static Object callDBFunction(ApplicationModuleImpl am, int sqlReturnType, String stmt, Object[] bindVars) 
    {
        CallableStatement cst = null;
        try {
            cst = am.getDBTransaction().createCallableStatement("begin ? := " + stmt + ";end;", 0);
            cst.registerOutParameter(1, sqlReturnType);
            if (bindVars != null) {
                for (int z = 0; z < bindVars.length; z++) {
                    cst.setObject(z + 2, bindVars[z]);
                }
            }
            cst.executeUpdate();
            return cst.getObject(1);
            } catch (SQLException e) 
            {
                e.printStackTrace();
                showMsgOnFnError(e);
                throw new JboException(e.getMessage());
            } finally 
              {
                if (cst != null) {
                    try {
                        cst.close();
                    } catch (SQLException e) {
                        logger.info(e);
                    }
                }
            }
    }
    
    /**
     * Method to Call Function or Procedure for ADF.
     * This Returns only one parameter.
     * @param am  ApplicationModuleImpl instance
     * @param sqlStatmnt  Sql Statement that need to be executed
     * @param params  Parameters that need to be put in the fucntion
     * @param returnTyp  Object which is the result
     */
    public static Object callDBFunctionWithFunName(ApplicationModuleImpl am, StringBuilder sqlStatmnt, Object[] params,
                                      int returnTyp) {
        Object o = null;
        long init = System.currentTimeMillis();
        CallableStatement st = null;
        if (LogUtil.enableLogger) 
        {
            StringBuilder logO = new StringBuilder("Object [");
            int j = 0;
            for (Object i : params) {
                j = j + 1;
                logO.append(i);
                if (params.length > j) {
                    logO.append(",");
                }
            }
            logO.append("]");
            logger.info("===> callFunction(" + am + "," + sqlStatmnt + "," + logO + "," + returnTyp + ") ");
        }
        try {
            st = am.getDBTransaction().createCallableStatement("begin ? := " + sqlStatmnt + ";end;", 0);
            st.registerOutParameter(1, returnTyp);
            if (params != null) {
                for (int z = 0; z < params.length; z++) {
                    st.setObject(z + 2, params[z]);
                }
            }
            st.executeUpdate();
            o = st.getObject(1);
        } catch (SQLException e) {
            e.printStackTrace();
            showMsgOnFnError(e);
            throw new JboException(e.getMessage());
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    logger.info(e);
                }
            }
        }
        if (LogUtil.enableLogger) {
            logger.info(" | Returned : " + o);
            long lst = System.currentTimeMillis();
            logger.info(" Time taken to call the above function is : " + (lst - init));
        }
        return o;
    }


    public static void showFacesMessageInfo(String header, String detail) 
    {
        StringBuilder saveMsg = new StringBuilder("<html><body><b><p style='color:blue'>");
        saveMsg.append(header);
        saveMsg.append("</p></b>");
        saveMsg.append("<b>");
        saveMsg.append(detail);
        saveMsg.append("</b></body></html>");
        FacesMessage msg = new FacesMessage(saveMsg.toString());
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public static void showFacesMessageWarn(String header, String detail) 
    {
        StringBuilder saveMsg = new StringBuilder("<html><body><b><p style='color:#FABE01'>");
        saveMsg.append(header);
        saveMsg.append("</p></b>");
        saveMsg.append("<b>");
        saveMsg.append(detail);
        saveMsg.append("</b></body></html>");
        FacesMessage msg = new FacesMessage(saveMsg.toString());
        msg.setSeverity(FacesMessage.SEVERITY_WARN);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public static void showFacesMessageError(String header, String detail) 
    {
        StringBuilder saveMsg = new StringBuilder("<html><body><b><p style='color:red'>");
        saveMsg.append(header);
        saveMsg.append("</p></b>");
        saveMsg.append("<b>");
        saveMsg.append(detail);
        saveMsg.append("</b></body></html>");
        FacesMessage msg = new FacesMessage(saveMsg.toString());
        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    //Globel Message for Util purpose
    public static void showFormattedMessage(String header, String detail,
                                                 javax.faces.application.FacesMessage.Severity severity) 
    {
        FacesMessage msg = new FacesMessage(header, detail);
        msg.setSeverity(severity);
        FacesContext.getCurrentInstance().addMessage(null, msg);
        msg.setSeverity(severity);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }


    /**
     * Method to Call Procedure for ADF.
     * This Returns only one parameter.
     * @param am ApplicationModuleImpl instance
     * @param sqlStmt
     * @param procParams Parameters that need to be put in the function
     */
    @SuppressWarnings("unchecked")
    public static ArrayList callDbProcedure(ApplicationModuleImpl am, StringBuilder sqlStmt,
                                            ProcParams[] procParams) throws SQLException {
        long init = System.currentTimeMillis();
        if (LogUtil.enableLogger) 
        {
            logger.info("===> callDbProcedure(" + am + "," + sqlStmt + "," + procParams);
        }
        ArrayList al = new ArrayList();
        CallableStatement st = null;
        try {
            st = am.getDBTransaction().createCallableStatement("begin  " + sqlStmt + ";end;", 0);
            for (ProcParams p : procParams) {
                if (p.getParamType() == ProcParams.IN) {
                    //System.out.println("in Parameter" + p.getSrNo() + " is " + p.getValue());
                    st.setObject(p.getSrNo(), p.getValue());
                } else if (p.getParamType() == ProcParams.OUT) {
                    //System.out.println("out Parameter" + p.getSrNo() + " is " + p.getDataType().toString());
                    st.registerOutParameter(p.getSrNo(), p.getDataType());
                }
            }
            st.executeUpdate();

            for (ProcParams p : procParams) {
                if (p.getParamType() == ProcParams.OUT) {
                    al.add(st.getObject(p.getSrNo()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showMsgOnFnError(e);
            //throw new JboException(e.getMessage());
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    //logger.info(e);
                }
            }
        }
        if (LogUtil.enableLogger) {
            logger.info(" | Returned : " + al);
            long lst = System.currentTimeMillis();
            logger.info(" Time taken to call the above function is : " + (lst - init));
        }
        return al;
    }


    /**
     * Method to get the name of Current Database connection driver.
     * @param am - ApplicationModule
     * @return
     */
    public static String getDbDriverName(ApplicationModuleImpl am) {
        if (dbDriverName == null) {
            DBTransaction dbTransaction = (DBTransaction) am.getTransaction();
            PreparedStatement prepStatement = dbTransaction.createPreparedStatement("select * from dual", 0);
            try {
                dbDriverName = prepStatement.getConnection().getMetaData().getDriverName();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dbDriverName;
    }


    public static String resourceExpression(String res)
    {
        Object el = null;
        if (res != null) {
            StringBuilder s = new StringBuilder("#{bundle['");
            s.append(res);
            s.append("']}");
            el = resolvEl(s.toString());
        }
        if (LogUtil.enableLogger) {
            LogUtil.showInfoLog(logger, "resolvRsrc", new Object[] { "res" }, new Object[] { res }, el);
        }
        return (el == null ? "<Resource does not Exist>" : el.toString());
    }


    private static void showMsgOnFnError(Exception e) {
        String errDesc = (e.getMessage() == null ? "" : e.getMessage());
        int i = errDesc.indexOf("\n");
        int j = errDesc.indexOf(":");
        if (i == -1 || j == -1) {
            ADFModelUtils.showFormattedMessage("", errDesc, FacesMessage.SEVERITY_ERROR);
        } else {
            ADFModelUtils.showFormattedMessage("", errDesc.substring(j + 2, i), FacesMessage.SEVERITY_ERROR);
        }
    }

    public static ViewCriteria supressStartsWithClauseForLov(ViewCriteria vc) {
        if (vc != null && vc.getName().contains("__lov__filterlist__vcr__")) {
            ViewCriteriaRow currentRow = (ViewCriteriaRow) vc.getCurrentRow();
            if (currentRow != null) {
                List criteriaItems = currentRow.getCriteriaItems();
                for (int i = 0; i < criteriaItems.size(); i++) {
                    ViewCriteriaItem object = (ViewCriteriaItem) criteriaItems.get(i);
                    if (object != null) 
                    {
                        if ("STARTSWITH".equals(object.getOperator())) 
                        {
                            object.setOperator("CONTAINS");
                        }
                    }
                }
            }
        }
        return vc;
    }

    public static BigDecimal executeBigDecimalQuery(ApplicationModuleImpl am,String query)
    {
        ResultSet rs;
        BigDecimal code = null;
        try {
            rs = am.getDBTransaction().createStatement(0).executeQuery(query);
            if (rs.next()) {
                code = ((BigDecimal) rs.getObject(1));
            }

            rs.close();
            return code;

        } catch (SQLException e) {
            throw new JboException(e);
        }
    }
    
        public static Integer executeQueryNumber(ApplicationModuleImpl am,String query) {
        ResultSet rs;
        Integer code = null;
        try {
            rs = am.getDBTransaction().createStatement(0).executeQuery(query);
            if (rs.next()) {
                code = ((BigDecimal)rs.getObject(1)).intValue();
            }

            rs.close();
            return code;

        } catch (SQLException e) {
            throw new JboException(e);
        }
    }
    
    public static String executeQueryString(ApplicationModuleImpl am,String query) {
        ResultSet rs;
        String code = null;
        try {
            rs = am.getDBTransaction().createStatement(0).executeQuery(query);
            if (rs.next()) {
                code = ((String) rs.getString(1));
            }
            rs.close();
            return code;
        } catch (SQLException e) {
            throw new JboException(e);
        }
    }
    
    public static oracle.jbo.domain.Date addDaysToDate(Integer i) {
      oracle.jbo.domain.Date tmp =
        new oracle.jbo.domain.Date(
          oracle.jbo.domain.Date.getCurrentDate());
      return new oracle.jbo.domain.Date(
        tmp.addJulianDays(i,0));
    }
    
    public static oracle.jbo.domain.Date subDaysToDate(Integer i) {
      oracle.jbo.domain.Date tmp =
        new oracle.jbo.domain.Date(
          oracle.jbo.domain.Date.getCurrentDate());
      return new oracle.jbo.domain.Date(
        tmp.addJulianDays(-i,0));
    }
}
