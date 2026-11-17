package adf.utils.com;

import adf.utils.bean.Constants;
import adf.utils.log.LogUtil;
import adf.utils.model.ADFModelUtils;

//import ebiz.data.itmdtls.ItmDtlsDC;

import java.lang.annotation.Documented;

import java.math.BigDecimal;

import java.sql.SQLException;
import java.sql.Types;

import javax.faces.application.FacesMessage;

import oracle.adf.share.logging.ADFLogger;

import oracle.jbo.domain.Number;
import oracle.jbo.domain.Timestamp;
import oracle.jbo.server.ApplicationModuleImpl;

/**
 * Created By:Aakash Kumar
 * Created On:10-08-2021
 * This class basically contains the Standard parameters.
 */
public class GlobalParams {
    private static ADFLogger log = ADFLogger.createADFLogger(GlobalParams.class);

    public GlobalParams() 
    {
        super();
    }

    /**
     * Created By:Aakash Kumar
     * Created On:25-08-2021
     * Remarks: It will return User Id:
     */
    public static String UserId() 
    {
        Object elExp = ADFModelUtils.resolvEl("#{pageFlowScope.UserId}");
        if (elExp != null) 
        {
            if (LogUtil.enableLogger) 
            {
                log.fine("----Global UserId Returned--->>:"+elExp);
            }
            return elExp.toString();
        } 
        else 
        {
            log.severe("Warning : Method | UserId() | Global User Id not defined.");
            if (LogUtil.enableLogger) {
                log.info("----Global UserId Returned Null----->>:"+elExp);
            }
            return null;
        }
    }

    /**
     * Created By:Aakash Kumar
     * Created On:27-08-2021
     * Remarks: It will return User Name:
     */
    public static String UserName() 
    {
        Object elExp = ADFModelUtils.resolvEl("#{pageFlowScope.UserName}");
        if (elExp != null) 
        {
            if (LogUtil.enableLogger) 
            {
                log.fine("----Global UserName Returned--->>:"+elExp);
            }
            return elExp.toString();
        } 
        else 
        {
            log.severe("Warning : Method | UserName() | Global User Id not defined.");
            if (LogUtil.enableLogger) {
                log.info("----Global UserName Returned Null----->>:"+elExp);
            }
            return null;
        }
    }
    /**
     * Created By:Aakash Kumar
     * Created On:25-08-2021
     * Remarks: It will return User Id:
     */
    public static String EmpCode() 
    {
        Object elExp = ADFModelUtils.resolvEl("#{pageFlowScope.EmpCode}");
        if (elExp != null) 
        {
            if (LogUtil.enableLogger) 
            {
                log.fine("----Global EmpCode Returned--->>:"+elExp);
            }
            return elExp.toString();
        } 
        else 
        {
            log.severe("Warning : Method | EmpCode() | Global User Id not defined.");
            if (LogUtil.enableLogger) {
                log.info("----Global EmpCode Returned Null----->>:"+elExp);
            }
            return null;
        }
    }
    
    
    /**
     * Created By:Aakash Kumar
     * Created On:25-08-2021
     * Remarks: It will return Employee Name.
     */
    public static String EmpName() 
    {
        Object elExp = ADFModelUtils.resolvEl("#{pageFlowScope.EmpName}");
        if (elExp != null) 
        {
            if (LogUtil.enableLogger) 
            {
                log.fine("----Global EmpName Returned--->>:"+elExp);
            }
            return elExp.toString();
        } 
        else 
        {
            log.severe("Warning : Method | EmpName() | Global Employee Name not defined.");
            if (LogUtil.enableLogger) {
                log.info("----Global EmpName Returned Null----->>:"+elExp);
            }
            return null;
        }
    }
    
    
    /**
     * Created By:Aakash Kumar
     * Created On:25-08-2021
     * Remarks: It will return Unit Code.
     */
    public static String UnitCode() 
    {
        Object elExp = ADFModelUtils.resolvEl("#{pageFlowScope.UnitCode}");
        if (elExp != null) 
        {
            if (LogUtil.enableLogger) 
            {
                log.fine("----Global UnitCode Returned--->>:"+elExp);
            }
            return elExp.toString();
        } 
        else 
        {
            log.severe("Warning : Method | UnitCode | Global Unit Code is not defined.");
            if (LogUtil.enableLogger) {
                log.info("----Global UnitCode Returned Null----->>:"+elExp);
            }
            return null;
        }
    }

    /**
     * Created By:Aakash Kumar
     * Created On:25-08-2021
     * Remarks: It will return Unit Name.
     */
    public static String UnitName() 
    {
        Object elExp = ADFModelUtils.resolvEl("#{pageFlowScope.UnitName}");
        if (elExp != null) 
        {
            if (LogUtil.enableLogger) 
            {
                log.fine("----Global Unit Name Returned--->>:"+elExp);
            }
            return elExp.toString();
        } 
        else 
        {
            log.severe("Warning : Method | UnitCode | Global Unit Name is not defined.");
            if (LogUtil.enableLogger) {
                log.info("----Global Unit Name Returned Null----->>:"+elExp);
            }
            return null;
        }
    }
    
    /**
     * Created By:Aakash Kumar
     * Created On:25-08-2021
     * Remarks: It will return Financial Year.
     */
    public static String FinYear() 
    {
        Object elExp = ADFModelUtils.resolvEl("#{pageFlowScope.FinYear}");
        if (elExp != null) 
        {
            if (LogUtil.enableLogger) 
            {
                log.fine("----Global Fin Year Returned--->>:"+elExp);
            }
            return elExp.toString();
        } 
        else 
        {
            log.severe("Warning : Method | UnitCode | Global Fin Year is not defined.");
            if (LogUtil.enableLogger) {
                log.info("----Global Fin Year Returned Null----->>:"+elExp);
            }
            return null;
        }
    }

    /**
     * Created By:Aakash Kumar
     * Created On:25-08-2021
     * Remarks:If DateFormat returns null.It will return 'dd-MM-yyyy'.
     */
    public static String DateFormat()
    {
        StringBuilder i = new StringBuilder("dd-MM-yyyy");
        Object elExp = ADFModelUtils.resolvEl("#{pageFlowScope.DateFormat}");
        if (elExp != null) {
            i = new StringBuilder(elExp.toString());
        } else {
            log.severe("Warning : Method | DateFormat() | DateFormat not defined.");
        }
        if (LogUtil.enableLogger) 
        {
            log.fine("<-----DateFormat() | Returned ---->>: "+i);
        }
        return i.toString();
    }
    
    /**
     * Created By:Aakash Kumar
     * Created On:20-SEP-2021
     * Remarks: It will return Default Currency:
     */
    public static String DefaultCurrureny() 
    {
        Object elExp = ADFModelUtils.resolvEl("#{pageFlowScope.DefCurrency}");
        if (elExp != null) 
        {
            if (LogUtil.enableLogger) 
            {
                log.fine("----Global DefaultCurrureny Returned--->>:"+elExp);
            }
            return elExp.toString();
        } 
        else 
        {
            log.severe("Warning : Method | DefaultCurrureny() | Global User Id not defined.");
            if (LogUtil.enableLogger) {
                log.info("----Global DefaultCurrureny Returned Null----->>:"+elExp);
            }
            return null;
        }
    }
    
    /**
     * Created By:Aakash Kumar
     * Created On:26-08-2021
     * Remarks:AmountDigit is null,it returns null otherwise It will return 2.
     */
    public static Integer AmountDigit() 
    {
        Integer i = 2;
        Object elExp = ADFModelUtils.resolvEl("#{pageFlowScope.AmountDigit}");
        if (elExp != null) {
            i = Integer.parseInt(elExp.toString());
        } else {
            log.severe("Warning : Method | AmountDigit() | Amount Digit not defined.");
        }
        if (LogUtil.enableLogger) {
            log.fine("---AmountDigit() called with following parameters :"+i);
        }
        return i;
    }

    /**
     * Created By:Aakash Kumar
     * Created On:26-08-2021
     * Remarks:RateDigit is null,it returns null otherwise It will return 2.
     */
    public static Integer RateDigit() 
    {
        Integer i = 2;
        Object elExp = ADFModelUtils.resolvEl("#{pageFlowScope.RateDigit}");
        if (elExp != null) {
            i = Integer.parseInt(elExp.toString());
        } else {
            log.severe("Warning : Method | RateDigit() | Rate Digit not defined.");
        }
        if (LogUtil.enableLogger) {
            log.fine("---RateDigit() called with following parameters :"+i);
        }
        return i;
    }
    
     /**
      * Created By:Aakash Kumar
      * Created On:26-08-2021
      * Remarks:CurrencyDigit is null,it returns null otherwise It will return 2.
      */
     public static Integer CurrencyDigit() 
     {
         Integer i = 2;
         Object elExp = ADFModelUtils.resolvEl("#{pageFlowScope.CurrencyDigit}");
         if (elExp != null) {
             i = Integer.parseInt(elExp.toString());
         } else {
             log.severe("Warning : Method | CurrencyDigit() | Rate Digit not defined.");
         }
         if (LogUtil.enableLogger) {
             log.fine("---CurrencyDigit() called with following parameters :"+i);
         }
         return i;
     }

    /**
     * Created By:Aakash Kumar
     * Created On:26-08-2021
     * Remarks:QuantityDigit is null,it returns null otherwise It will return 2.
     */
    public static Integer QuantityDigit() 
    {
        Integer i = 2;
        Object elExp = ADFModelUtils.resolvEl("#{pageFlowScope.QuantityDigit}");
        if (elExp != null) {
            i = Integer.parseInt(elExp.toString());
        } else {
            log.severe("Warning : Method | QuantityDigit() | Quantity Digit not defined.");
        }
        if (LogUtil.enableLogger) {
            log.fine("---QuantityDigit() called with following parameters :"+i);
        }
        return i;
    }

    /**
     * Created By:Aakash Kumar
     * Created On:26-08-2021
     * Remarks:DiscountDigit is null,it returns null otherwise It will return 2.
     */
    public static Integer DiscountDigit() 
    {
        Integer i = 2;
        Object elExp = ADFModelUtils.resolvEl("#{pageFlowScope.DiscountDigit}");
        if (elExp != null) {
            i = Integer.parseInt(elExp.toString());
        } else {
            log.severe("Warning : Method | DiscountDigit() | Discount Digit not defined.");
        }
        if (LogUtil.enableLogger) {
            log.fine("---Discount Digit called with following parameters :"+i);
        }
        return i;
    }
  

}
