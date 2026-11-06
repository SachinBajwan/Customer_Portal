package view.bspl.self.beans;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import oracle.adf.view.rich.context.AdfFacesContext;

public class selfServiceBean {
    public selfServiceBean() {
    }

    /* =========================================
         MY PROFILE
         ========================================= */

      public String openProfile() {

          System.out.println("Opening My Profile");
//          AdfFacesContext.getCurrentInstance()
//                            .getPageFlowScope()
//                            .put("vendorId", new Long("200007768824"));

          return "profile";
      }


      /* =========================================
         CHANGE PASSWORD
         ========================================= */

      public String changePassword() {

          System.out.println("Opening Change Password");

          return "changePassword";
      }


      /* =========================================
         MANAGE USERS
         ========================================= */

      public String manageUsers() {

          System.out.println("Opening Manage Users");

          return "manageUsers";
      }


      /* =========================================
         DELIVERY ADDRESSES
         ========================================= */

      public String deliveryAddresses() {

          System.out.println("Opening Delivery Addresses");

          return "deliveryAddresses";
      }


      /* =========================================
         ACCOUNT STATEMENT
         ========================================= */

      public String accountStatement() {

          System.out.println("Opening Account Statement");

          return "accountStatement";
      }


      /* =========================================
         RAISE TICKET
         ========================================= */

      public String raiseTicket() {

          System.out.println("Opening Raise a Ticket");

          return "raiseTicket";
      }


      /* =========================================
         FAQ
         ========================================= */

      public String faqHelp() {

          System.out.println("Opening FAQs & Help");

          return "faqHelp";
      }


      /* =========================================
         ACTIVE SESSIONS
         ========================================= */

      public String activeSessions() {

          System.out.println("Opening Active Sessions");

          return "activeSessions";
      }


      /* =========================================
         OPTIONAL MESSAGE
         ========================================= */

      private void showMessage(String message) {

        FacesContext context = FacesContext.getCurrentInstance();

        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, null
              )
          );
      }
}
