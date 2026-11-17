package bspl.sec.passresetapp.bean;

import javax.faces.event.ActionEvent;

import oracle.binding.OperationBinding;
import adf.utils.bean.ADFBeanUtils;

public class PassResetAppBean {
    public PassResetAppBean() {
    }

    public void onClickSubmit(ActionEvent actionEvent) {
    try {
        System.out.println(ADFBeanUtils.resolveExpression("#{pageFlowScope.empCode}"));
        String empID = ADFBeanUtils.resolveExpression("#{pageFlowScope.empCode}").toString();
        String decryptPassword = "N";

        OperationBinding ob = ADFBeanUtils.findOperation("userPassordDecrypt");
        ob.getParamsMap().put("EMP_ID", empID);
        ob.execute();
        decryptPassword = ob.getResult().toString();
        String newPassword = "";
        String confrimPassword = "";
        String oldpassword = "";

        if (null == ADFBeanUtils.evaluateEL("#{bindings.OldPassword.inputValue}")) {
            oldpassword = "Y";
            ADFBeanUtils.showFacesMessageError("Please Enter OldPassword ", "");
            return;
        } else {
            oldpassword = ADFBeanUtils.evaluateEL("#{bindings.OldPassword.inputValue}").toString();
        }

        if (null == ADFBeanUtils.evaluateEL("#{bindings.NewPassword.inputValue}") || ""==ADFBeanUtils.evaluateEL("#{bindings.NewPassword.inputValue}")) {
            newPassword = "Y";
            ADFBeanUtils.showFacesMessageError("Please Enter New Password ", "");
            return;
        } else {
            newPassword = ADFBeanUtils.evaluateEL("#{bindings.NewPassword.inputValue}").toString();
        }
        if (null == ADFBeanUtils.evaluateEL("#{bindings.ConfirmPassword.inputValue}")) {
            confrimPassword = "N";
            ADFBeanUtils.showFacesMessageError("Please Enter Confirm Password ", "");
            return;
        } else {
            confrimPassword = ADFBeanUtils.evaluateEL("#{bindings.ConfirmPassword.inputValue}").toString();
        }

        boolean res = validatepassword(newPassword);
        if (res == false) {
            ADFBeanUtils.showFacesMessageError("Password Must Have ",
                                               "One non alphanumeric character " + "\n" + ",One Digit('0','9')" +
                                               "\n" + ",One Uppercase ('A','Z')" + "\n" + ",Atleast 8 digit" +
                                               "\n" + ",Atleat One Special Character");
            return;
        }
        //to compair old and confrim password
        
        if (newPassword.equals(confrimPassword)) {
            if (oldpassword.equals(decryptPassword)) {
                try{
               OperationBinding updatePassword =   ADFBeanUtils.findOperation("updatePassword");
               updatePassword.getParamsMap().put("password", newPassword);
               updatePassword.getParamsMap().put("emp_ID", empID);
               updatePassword.execute();
                }
                catch(Exception ex1){
                        ex1.printStackTrace();
                    ADFBeanUtils.showFacesMessageError("Unable to Update Password ", ex1.toString());
                    }
                
                ADFBeanUtils.showFacesMessageInfo("Password Reset Successfully", "");
            } else {
                ADFBeanUtils.showFacesMessageError("Old Password Is Not Correct", "Try Again");

            }
        } else {
            ADFBeanUtils.showFacesMessageError("New Password and Confirm Password doesn't Match", "");
            return;
        }
    } catch (Exception ex) {
        ex.printStackTrace();

        ADFBeanUtils.showFacesMessageError("Error in onClickSubmit ", ex.toString());
    }    
}

    private boolean validatepassword(String newPassword) {
        return newPassword.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
    }
}
