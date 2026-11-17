package view.bspl.docs.bean;

import adf.utils.bean.ADFBeanUtils;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.input.RichInputFile;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.util.ResetUtils;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import oracle.jbo.Row;

import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewObject;

import org.apache.myfaces.trinidad.model.UploadedFile;


public class DocsUplaodBean {
    private RichInputFile bindInputFile;

    public DocsUplaodBean() {
    }


    public void uploadDocs(ValueChangeEvent vce) {
        UploadedFile fileVal = (UploadedFile) vce.getNewValue();
        OperationBinding op = null;
        if (vce.getNewValue() != null) {
            op = adf.utils.bean.ADFBeanUtils.findOperation("chkDupFile");
            op.getParamsMap().put("FileName", fileVal.getFilename());
            op.execute();
            String result = op.getResult().toString();
            if (result.equals("Y")) {
                bindInputFile.setValue("");
                AdfFacesContext.getCurrentInstance().addPartialTarget(bindInputFile);
                ADFUtils.showMessage("This document is already attached. Duplicate entries are not allowed.", 2);
                return;
            }
            long fileSize = fileVal.getLength(); // size in bytes

            if (fileSize > 5 * 1024 * 1024) { // 5 MB limit
                FacesMessage msg = new FacesMessage("File size exceeds 5MB limit");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                Row r =
                    (Row) adf.utils.bean.ADFBeanUtils.evaluateEL("#{bindings.PortalDocAttachRefDtlVO2Iterator.currentRow}");
                //            UploadedFile fileVal = (UploadedFile) vce.getNewValue();
                System.out.println("File Value==============<>" + fileVal);
                String path = ADFBeanUtils.uploadFile(fileVal);
                System.out.println(fileVal.getContentType());
                System.out.println("Content Type==>>" + fileVal.getContentType() + "Path==>>" + path + "File name==>>" +
                                   fileVal.getFilename());
                r.setAttribute("Path", path);
                r.setAttribute("DocFileName", fileVal.getFilename());
                r.setAttribute("Remarks", r.getAttribute("RefDocType"));
                ResetUtils.reset(vce.getComponent());
            }
        }
    }


    public void saveDocs(ActionEvent actionEvent) {

        BindingContext bc = BindingContext.getCurrent();

        DCBindingContainer bindings = (DCBindingContainer) bc.getCurrentBindingsEntry();
//
//        DCIteratorBinding iterator = bindings.findIteratorBinding("PortalDocAttachRefDtlVO2Iterator");
//
//        ViewObject vo = iterator.getViewObject();
//
//        RowSetIterator rsi = vo.createRowSetIterator(null);

//        try {
//            while (rsi.hasNext()) {
//
//                Row row = rsi.next();
//
//                System.out.println("RefDocNo = " + row.getAttribute("RefDocNo"));
//
//                System.out.println("Remarks = " + row.getAttribute("Remarks"));
//                
//                System.out.println("Path = " + row.getAttribute("Path"));
//            }
//        } finally {
//            rsi.closeRowSetIterator();
//        }

        OperationBinding operationBinding = bindings.getOperationBinding("Commit");

        operationBinding.execute();

        System.out.println("Commit errors = " + operationBinding.getErrors());
    }


    public void setBindInputFile(RichInputFile bindInputFile) {
        this.bindInputFile = bindInputFile;
    }

    public RichInputFile getBindInputFile() {
        return bindInputFile;
    }
}
