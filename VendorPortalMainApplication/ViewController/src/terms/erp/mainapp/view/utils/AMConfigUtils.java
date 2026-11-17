package terms.erp.mainapp.view.utils;


import javax.servlet.http.HttpSession;

import oracle.jbo.client.Configuration;

import terms.erp.mainapp.model.services.TermsMainAppAMImpl;

public class AMConfigUtils {
    static String amDef = "terms.erp.mainapp.model.services.TermsMainAppAMImpl";
    static String config = "TermsMainAppAMLocal";

    public AMConfigUtils() {
        super();
    }


    public TermsMainAppAMImpl getAMInstance() {
        return createAM();
    }

    public void releaseAMInstance(TermsMainAppAMImpl am) {
        releaseAM(am);
    }


    /**
     * Creating and releasing AM for JSF Application
     *
     * **/
    public static TermsMainAppAMImpl getAM() {
        return getAM(TermsADFUtils.getSession());
    }


    public static void releaseAM() {
        releaseAM(TermsADFUtils.getSession());

    }

    /**
     * Creating and releasing AM For Session
     *
     * **/

    public static TermsMainAppAMImpl getAM(HttpSession session) {

        Object obj = session.getAttribute("AmConn");
        TermsMainAppAMImpl am = null;
        if (obj == null) {
            am = createAM();
            session.setAttribute("AmConn", am);
        } else {
            am = (TermsMainAppAMImpl) obj;
        }


        return am;
    }

    public static void releaseAM(HttpSession session) {
        Object obj = session.getAttribute("AmConn");
        TermsMainAppAMImpl am = null;
        if (obj == null) {
        } else {
            am = (TermsMainAppAMImpl) obj;
            releaseAM(am);
            session.removeAttribute("AmConn");
        }
    }

    /**
     * Creating and releasing AM
     *
     * **/


    private static TermsMainAppAMImpl createAM() {
        TermsMainAppAMImpl am = (TermsMainAppAMImpl) Configuration.createRootApplicationModule(amDef, config);
        // System.out.println("Creating am " + am);
        return am;
    }


    private static void releaseAM(TermsMainAppAMImpl am) {

        Configuration.releaseRootApplicationModule(am, true);
        // System.out.println("releasing " + am);
    }
}
