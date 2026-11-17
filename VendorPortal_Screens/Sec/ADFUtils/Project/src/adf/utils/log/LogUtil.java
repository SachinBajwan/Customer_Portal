package adf.utils.log;

import oracle.adf.share.logging.ADFLogger;

/**
 * Showing Log
 */
public class LogUtil 
{
    @SuppressWarnings("oracle.jdeveloper.java.unrestricted-field-access")
    /**
    * Set enableLogger to true when you want to see the log.
    * This it to enable and disable logger for development and Production server
    **/
    public static Boolean enableLogger = true;

    /**
     * Method to show info log in this Utility
     * @param _log
     * @param methodNm
     * @param paramNm
     * @param paramVal
     */
    public static void showInfoLog(ADFLogger _log, String methodNm, Object[] paramNm, Object[] paramVal,
                                   Object returned) {
        StringBuilder log = new StringBuilder("=======> ");
        log.append(methodNm);
        log.append("(");
        int i = paramNm.length;
        for (Object o : paramNm) {
            i = i - 1;
            log.append(o);
            if (i != 0)
                log.append(",");
        }
        log.append(")");
        _log.info(log.toString());

        log = new StringBuilder("=======> ");
        log.append(methodNm);
        log.append("(");

        i = paramVal.length;
        for (Object o : paramVal) {
            i = i - 1;
            log.append(o);
            if (i != 0)
                log.append(",");
        }
        log.append(") | Returned : " + returned);
        _log.info(log.toString());
    }

    /**
     * Method to show severe log in this Utility
     * @param _log
     * @param methodNm
     * @param paramNm
     * @param paramVal
     */
    public static void showSevereLog(ADFLogger _log, String methodNm, Object[] paramNm, Object[] paramVal,
                                     Object returned) {
        StringBuilder log = new StringBuilder("=======> ");
        log.append(methodNm);
        log.append("(");
        int i = paramNm.length;
        for (Object o : paramNm) {
            i = i - 1;
            log.append(o);
            if (i != 0)
                log.append(",");
        }
        log.append(")");
        _log.severe(log.toString());

        log = new StringBuilder("=======> ");
        log.append(methodNm);
        log.append("(");

        i = paramVal.length;
        for (Object o : paramVal) {
            i = i - 1;
            log.append(o);
            if (i != 0)
                log.append(",");
        }
        log.append(") | Returned : " + returned);
        _log.severe(log.toString());
    }

    /**
     * Method to show fine log in this Utility
     * @param _log
     * @param methodNm
     * @param paramNm
     * @param paramVal
     */
    public static void showFineLog(ADFLogger _log, String methodNm, Object[] paramNm, Object[] paramVal,
                                   Object returned) {
        StringBuilder log = new StringBuilder("=======> ");
        log.append(methodNm);
        log.append("(");
        int i = paramNm.length;
        for (Object o : paramNm) {
            i = i - 1;
            log.append(o);
            if (i != 0)
                log.append(",");
        }
        log.append(")");
        _log.severe(log.toString());

        log = new StringBuilder("=======> ");
        log.append(methodNm);
        log.append("(");

        i = paramVal.length;
        for (Object o : paramVal) {
            i = i - 1;
            log.append(o);
            if (i != 0)
                log.append(",");
        }
        log.append(") | Returned : " + returned);
        _log.severe(log.toString());
    }


}
