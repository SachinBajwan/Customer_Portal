package terms.erp.mainapp.view.utils;

import java.sql.Connection;

import javax.servlet.http.HttpSession;

import terms.erp.mainapp.model.services.TermsMainAppAMImpl;

public class CustomLocalThread 
{
    private static ThreadLocal<TermsMainAppAMImpl> am = new ThreadLocal<TermsMainAppAMImpl>();

    public static void setAmConnTo(ThreadLocal<TermsMainAppAMImpl> am) {
        CustomLocalThread.am.set(null);
    }

    public static ThreadLocal<TermsMainAppAMImpl> getAm() {
        return am;
    }


    private CustomLocalThread() {
        super();
    }


    public static TermsMainAppAMImpl getApplicationModule() {
        TermsMainAppAMImpl localAM = am.get();


        if (localAM == null) {
            am.set(AMConfigUtils.getAM());
        }
        return am.get();
    }

    public static TermsMainAppAMImpl getApplicationModule(HttpSession session) {
        TermsMainAppAMImpl localAM = am.get();

        if (localAM == null) {
            am.set(AMConfigUtils.getAM(session));
        }
        return am.get();
    }


    public static void releaseAM() 
    {
        TermsMainAppAMImpl localAM = am.get();

        if (localAM != null) {
            try {
                AMConfigUtils.releaseAM();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void releaseAM(HttpSession session) {
        TermsMainAppAMImpl localAM = am.get();
        if (localAM != null) {
            try {
                AMConfigUtils.releaseAM(session);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
