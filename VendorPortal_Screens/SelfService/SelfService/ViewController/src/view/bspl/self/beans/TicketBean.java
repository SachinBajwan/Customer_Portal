package view.bspl.self.beans;

import javax.faces.event.ActionEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.view.rich.component.rich.RichPopup;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

public class TicketBean {


    private RichPopup bindTicketPop;
    private RichTable bndTable;

    public TicketBean() {
    }


    public void setBindTicketPop(RichPopup bindTicketPop) {
        this.bindTicketPop = bindTicketPop;
    }

    public RichPopup getBindTicketPop() {
        return bindTicketPop;
    }

    public void createTicket(ActionEvent actionEvent) {

        BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();
        OperationBinding operationBinding = bindings.getOperationBinding("CreateInsert");
        Object result = operationBinding.execute();

        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        getBindTicketPop().show(hints);
    }

    public void saveTicket(DialogEvent dialogEvent) {
        System.out.println(dialogEvent.getOutcome().name());
        if (dialogEvent.getOutcome().name().equals("yes")) {
            BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();
            OperationBinding operationBinding = bindings.getOperationBinding("Commit");
            Object result = operationBinding.execute();
        } else if (dialogEvent.getOutcome().name().equals("no")) {
            BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();
            OperationBinding operationBinding = bindings.getOperationBinding("Rollback");
            Object result = operationBinding.execute();
        }
        AdfFacesContext.getCurrentInstance().addPartialTarget(bndTable);
    }

    public void setBndTable(RichTable bndTable) {
        this.bndTable = bndTable;
    }

    public RichTable getBndTable() {
        return bndTable;
    }
}
