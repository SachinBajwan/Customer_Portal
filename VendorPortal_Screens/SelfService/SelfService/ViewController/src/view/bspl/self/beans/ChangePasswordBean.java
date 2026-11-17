package view.bspl.self.beans;


import UserMaster.view.beans.ADFUtils;

import adf.utils.bean.ADFBeanUtils;

import java.io.Serializable;

import java.util.regex.Pattern;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;

import oracle.binding.OperationBinding;



public class ChangePasswordBean implements Serializable {

    private static final long serialVersionUID = 1L;


    /* =========================================
       PASSWORD FIELDS
    ========================================= */

    private String currentPassword;
    private String newPassword;
    private String confirmPassword;


    /* =========================================
       CHANGE PASSWORD ACTION
    ========================================= */

    public String changePassword() {

        FacesContext fc = FacesContext.getCurrentInstance();

        try {

            /* ---------------------------------
               1. VALIDATE EMPTY FIELDS
            --------------------------------- */

            if (isEmpty(currentPassword)) {

                showError("Please enter your current password.");
                return null;
            }

            if (isEmpty(newPassword)) {

                showError("Please enter your new password.");
                return null;
            }

            if (isEmpty(confirmPassword)) {

                showError("Please confirm your new password.");
                return null;
            }


            /* ---------------------------------
               2. VALIDATE NEW PASSWORD
            --------------------------------- */

            if (!isValidPassword(newPassword)) {

                showError(
                    "Password must contain at least 8 characters, " +
                    "one uppercase letter, one lowercase letter, " +
                    "one number and one special character."
                );

                return null;
            }


            /* ---------------------------------
               3. CHECK CURRENT AND NEW PASSWORD
            --------------------------------- */

            if (currentPassword.equals(newPassword)) {

                showError(
                    "New password must be different from current password."
                );

                return null;
            }


            /* ---------------------------------
               4. CONFIRM PASSWORD
            --------------------------------- */

            if (!newPassword.equals(confirmPassword)) {

                showError(
                    "New password and confirm password do not match."
                );

                return null;
            }


            /* ---------------------------------
               5. CALL ADF APPLICATION MODULE
            --------------------------------- */
            String userId = (String) resolveExpression("#{pageFlowScope.userId}");
            DCBindingContainer bindings =
                (DCBindingContainer) BindingContext
                    .getCurrent()
                    .getCurrentBindingsEntry();
            
            /*
                       * Replace "ChangePassword" with the exact
                       * operation name exposed by your AM.
                       */

//                      OperationBinding operation =
//                          bindings.getOperationBinding(
//                              "changePassword"
//                          );


          
            OperationBinding operation = null;
            operation = ADFUtils.findOperation("changePassword");
//            operation.getParamsMap().put("userId", userId);
//            operation.getParamsMap().put("oldPass", currentPassword);
//            operation.getParamsMap().put("newPass", confirmPassword);
//            operation.execute();
//            System.out.println("--authenticateUserPassword--Result---::" + operation.getResult());
//            if(operation.getResult().toString().equals("S"))
//            {
//                    ADFBeanUtils.showFacesMessageInfo("", "Pass"); 
//                }
//            else
//            {
//                
//                }
         

            if (operation == null) {

                showError(
                    "changePassword operation is not configured in PageDef."
                );

                return null;
            }


            /*
             * SET PARAMETERS
             *
             * These parameter names must match your
             * Application Module method parameters.
             */

            operation.getParamsMap().put("userId", userId);
            operation.getParamsMap().put("oldPass", currentPassword);
            operation.getParamsMap().put("newPass", confirmPassword);


            operation.execute();


            /* ---------------------------------
               6. CHECK OPERATION RESULT
            --------------------------------- */

            if (operation.getResult().toString().equals("S")) {

                showSuccess(
                    "Password changed successfully."
                );

                clearFields();

                return null;

            } 
            else if (operation.getResult().toString().equals("U")) {

                showSuccess(
                    "User not found."
                );

                clearFields();

                return null;

            } 
            else if (operation.getResult().toString().equals("M")) {

                showSuccess(
                    "Current password does not match."
                );

                clearFields();

                return null;

            } 
            
            else {

                showError(
                    "Unable to change password. " +
                    "Please verify your current password."
                );

                return null;
            }


        } catch (Exception e) {

            e.printStackTrace();

            showError(
                "An unexpected error occurred while changing password."
            );

            return null;
        }
    }


    /* =========================================
       PASSWORD VALIDATION
    ========================================= */

    private boolean isValidPassword(String password) {

        if (password == null || password.length() < 8) {
            return false;
        }

        /*
         * At least one uppercase letter
         */

        boolean hasUppercase =
            Pattern.compile("[A-Z]")
                   .matcher(password)
                   .find();


        /*
         * At least one lowercase letter
         */

        boolean hasLowercase =
            Pattern.compile("[a-z]")
                   .matcher(password)
                   .find();


        /*
         * At least one digit
         */

        boolean hasNumber =
            Pattern.compile("[0-9]")
                   .matcher(password)
                   .find();


        /*
         * At least one special character
         */

        boolean hasSpecial =
            Pattern.compile("[^a-zA-Z0-9]")
                   .matcher(password)
                   .find();


        return hasUppercase &&
               hasLowercase &&
               hasNumber &&
               hasSpecial;
    }


    /* =========================================
       EMPTY VALIDATION
    ========================================= */

    private boolean isEmpty(String value) {

        return value == null ||
               value.trim().isEmpty();
    }


    /* =========================================
       SUCCESS MESSAGE
    ========================================= */

    private void showSuccess(String message) {

        FacesContext.getCurrentInstance()
                    .addMessage(
                        null,
                        new FacesMessage(
                            FacesMessage.SEVERITY_INFO,
                            message,
                            null
                        )
                    );
    }


    /* =========================================
       ERROR MESSAGE
    ========================================= */

    private void showError(String message) {

        FacesContext.getCurrentInstance()
                    .addMessage(
                        null,
                        new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            message,
                            null
                        )
                    );
    }


    /* =========================================
       CLEAR PASSWORD FIELDS
    ========================================= */

    private void clearFields() {

        currentPassword = null;
        newPassword = null;
        confirmPassword = null;
    }


    /* =========================================
       CANCEL ACTION
    ========================================= */

    public String cancel() {

        clearFields();

        return "cancel";
    }


    /* =========================================
       GETTERS AND SETTERS
    ========================================= */

    public String getCurrentPassword() {

        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {

        this.currentPassword = currentPassword;
    }


    public String getNewPassword() {

        return newPassword;
    }

    public void setNewPassword(String newPassword) {

        this.newPassword = newPassword;
    }


    public String getConfirmPassword() {

        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {

        this.confirmPassword = confirmPassword;
    }

    public static Object resolveExpression(String expression) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application app = facesContext.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        ValueExpression valueExp = elFactory.createValueExpression(elContext, expression, Object.class);
        return valueExp.getValue(elContext);
    }
    
}
