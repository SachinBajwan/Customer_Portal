package adf.utils.bean;

import java.math.BigDecimal;

import java.sql.SQLException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Calendar;

import oracle.adf.share.logging.ADFLogger;

import oracle.jbo.domain.Date;
import oracle.jbo.domain.Number;
import oracle.jbo.domain.Timestamp;

/**
 *  This class contains that static values that can be used in Applications.
 *  This contains methods for formatting and truncating Timestamp to proper proper format.
 */
public class Constants {
    public final static Number NUMBER_HUNDRED = new Number(100);
    public final static Number NUMBER_ZERO = new Number(0);
    public final static BigDecimal BIG_HUNDRED = new BigDecimal(100);
    public final static BigDecimal BIG_ZERO = new BigDecimal(0);
    public final static BigDecimal BIG_ONE = new BigDecimal(1);
    public final static String SUCCESS= "Y";
    public final static String FAIL= "Y";
    private static ADFLogger _log = ADFLogger.createADFLogger(Constants.class);

    
    public Constants() {
        super();
    }
    
    public static java.sql.Timestamp convertStringToTimeStamp(String dateTime)
    {
        DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.from(formatDateTime.parse(dateTime));
        java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(localDateTime);
        return timestamp;
    }
    
    public static boolean isValidFormat(String format, String value) {
        System.out.println("Inside valid format function!");
            java.util.Date date = null;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                date = sdf.parse(value);
                if (!value.equals(sdf.format(date))) {
                    date = null;
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
            return date != null;
        }
    
    public static oracle.jbo.domain.Date convertStringToJboDate1(String datestring) {
        System.out.println("Inside ADFUtils for date!");
      
        if (!datestring.equals("-")) {
            System.out.println("inside if condition!");
           Boolean t= isValidFormat("dd.MM.yyyy",datestring);
           if(t){
               System.out.println("Inside if TRUE! (dot format)");
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd.MM.yyyy");
            java.text.SimpleDateFormat sdfsql = new java.text.SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = null;
            try {
                System.out.println("Inside try block!");
                date = sdf.parse(datestring);
            } catch (Exception exc) {
                System.out.println("Date error");
            }
            return new oracle.jbo.domain.Date(sdfsql.format(date));
           }
           else{
               System.out.println("inside else FALSE !(Slash format)");
                   java.text.SimpleDateFormat sdf1 = new java.text.SimpleDateFormat("dd/MM/yyyy");
                   java.text.SimpleDateFormat sdfsql = new java.text.SimpleDateFormat("yyyy-MM-dd");
                   java.util.Date date = null;
                   try {
                       System.out.println("Inside try block!");
                       date = sdf1.parse(datestring);
                   } catch (Exception exc) {
                       System.out.println("Date error");
                   }
                   return new oracle.jbo.domain.Date(sdfsql.format(date));
               }
        } else {
            oracle.jbo.domain.Date date1 = null;
            System.out.println("Date is not entered!");
            return (oracle.jbo.domain.Date) date1;

        }
    }
    
    public static oracle.jbo.domain.Date convertStringToJboDate(String datestring) 
    {
      java.text.SimpleDateFormat sdf =
        new java.text.SimpleDateFormat("dd.MM.yyyy");
      java.text.SimpleDateFormat sdfsql =
        new java.text.SimpleDateFormat("yyyy-MM-dd");
      java.util.Date date = null;
      try {
        date = sdf.parse(datestring);
      } catch (Exception exc) {
        System.out.println("Date error");
      }
      return new oracle.jbo.domain.Date(
        sdfsql.format(date));
    }
    
    public static oracle.jbo.domain.Date getCurrentOracleJboDate()
    {
      return new oracle.jbo.domain.Date(oracle.jbo.domain.Date.getCurrentDate());
    }
    
    
    /** function will return current date and time as an oracle Date object */
     public static oracle.jbo.domain.Date getCurrentOracleJboDateTime()
     {
       return new oracle.jbo.domain.Date(new java.sql.Timestamp(System.currentTimeMillis()));
     }
    
    public static Timestamp getTruncatedCurrDt()
    {
        Timestamp t = new Timestamp(System.currentTimeMillis());
        try {
            t = new Timestamp(t.dateValue() + " 00:00:00");
        } catch (SQLException e) {
            _log.severe("Unable to get Current Date Timestamp !"+e.getMessage());
        }
        return t;
    }
    
    /**
     * Method to fetch new truncated date with proper current Timestamp as hh:mm 00:00:00).
     * @return
     */
    public static Timestamp getCurrDtWidTimestamp()
    {
        Timestamp t = new Timestamp(System.currentTimeMillis());
        t = new Timestamp(t.toString().substring(0,19));
        return t;
    }
    
    /**
     * Method to fetch new truncated Date with no current Timestamp as hh:mm 00:00:00).
     * @param t
     * @return
     */
    public static Timestamp getTruncatedDt(Timestamp t){
        try {
            t = new Timestamp(t.dateValue() + " 00:00:00");
        } catch (SQLException e) {
            _log.severe("Unable to get Current Date Timestamp !"+e.getMessage());
        }
        return t;
    }
    
    /**
     * Method to fetch new truncated date with proper currrent Timestamp with hh:mm 00:00:00)
     */
    public static Timestamp getDtWithTimestamp(Timestamp t){
        return new Timestamp(t.toString().substring(0,19));
    }
    
   
    public static Timestamp addOrSubDaysWithTimestamp(Timestamp ts,String opType, Integer noOfDays) {
        java.sql.Date stDt = null;
        try {
            stDt = ts.dateValue();
        } catch (SQLException e) {
        }
        Calendar calStDate = Calendar.getInstance();
        calStDate.setTime(stDt);
        if("A".equals(opType)){
            calStDate.add(Calendar.DATE, noOfDays);
        }else if("S".equals(opType)){
            calStDate.add(Calendar.DATE, -noOfDays);
        }
        return getDtWithTimestamp(new Timestamp(calStDate.getTime()));

    }

}
