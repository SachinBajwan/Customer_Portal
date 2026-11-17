package view.bspl.orders.beans;

import java.math.BigDecimal;

import java.math.RoundingMode;

import java.util.Date;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.faces.event.ValueChangeEvent;

import model.bspl.orders.views.OrderDetailVORowImpl;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;

import oracle.adf.model.binding.DCDataControl;
import oracle.adf.model.binding.DCIteratorBinding;

import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.adf.view.rich.component.rich.output.RichOutputText;

import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import oracle.jbo.ApplicationModule;
import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewObject;

import org.apache.myfaces.trinidad.component.UIXEditableValue;

public class OrderBean {
    private RichInputText bindProdCd;
    private RichInputText bindUom;
    private RichInputText bindQty;
    private RichInputText bindRate;
    private RichInputText bindGst;
    private RichOutputText bindSubTotal;
    private RichOutputText bindCgst;
    private RichOutputText bindSgst;
    private RichOutputText bindGrandTotal;
    private RichOutputText gstTotal;

    public OrderBean() {
    }

    public Date getToday() {
        return new Date();
    }


    public void createDtl(ActionEvent actionEvent) {
        DCBindingContainer bc = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
        OperationBinding operationbinding = bc.getOperationBinding("CreateInsert");
        operationbinding.execute();
    }

    public void onDeleteItem(ActionEvent actionEvent) {
        DCBindingContainer bc = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
        OperationBinding operationbinding = bc.getOperationBinding("Delete");
        operationbinding.execute();
        calculateTotals();
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindSubTotal);
        AdfFacesContext.getCurrentInstance().addPartialTarget(gstTotal);
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindCgst);
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindSgst);
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindGrandTotal);
    }

    public void calculateOrderLine() {

        try {

            BindingContext bc = BindingContext.getCurrent();

            DCBindingContainer bindings = (DCBindingContainer) bc.getCurrentBindingsEntry();

            DCIteratorBinding iterator = bindings.findIteratorBinding("OrderDetailVO2Iterator");

            if (iterator == null) {
                return;
            }

            Row row = iterator.getCurrentRow();

            if (row == null) {
                return;
            }

            // Get values from current row
            BigDecimal quantity = (BigDecimal) row.getAttribute("Quantity");

            BigDecimal rate = (BigDecimal) row.getAttribute("Rate");

            BigDecimal gst = (BigDecimal) row.getAttribute("Gst");


            // -------------------------
            // NULL HANDLING
            // -------------------------

            if (quantity == null) {
                quantity = BigDecimal.ZERO;
            }

            if (rate == null) {
                rate = BigDecimal.ZERO;
            }

            if (gst == null) {
                gst = BigDecimal.ZERO;
            }


            // -------------------------
            // SUB TOTAL
            // -------------------------

            BigDecimal subTotal = quantity.multiply(rate).setScale(2, RoundingMode.HALF_UP);


            // -------------------------
            // CGST
            // -------------------------

            BigDecimal cgst = subTotal.multiply(gst).divide(new BigDecimal("200"), 2, RoundingMode.HALF_UP);


            // -------------------------
            // SGST
            // -------------------------

            BigDecimal sgst = subTotal.multiply(gst).divide(new BigDecimal("200"), 2, RoundingMode.HALF_UP);


            // -------------------------
            // SET TRANSIENT VALUES
            // -------------------------

            row.setAttribute("SubTotal", subTotal);

            row.setAttribute("Cgst", cgst);

            row.setAttribute("Sgst", sgst);


            // -------------------------
            // TOTAL AMOUNT
            // -------------------------

            BigDecimal totalAmount = subTotal.add(cgst).add(sgst).setScale(2, RoundingMode.HALF_UP);

            row.setAttribute("TotalAmount", totalAmount);


            System.out.println("Quantity  : " + quantity);
            System.out.println("Rate      : " + rate);
            System.out.println("GST       : " + gst);
            System.out.println("SubTotal  : " + subTotal);
            System.out.println("CGST      : " + cgst);
            System.out.println("SGST      : " + sgst);
            System.out.println("Total     : " + totalAmount);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void GstVce(ValueChangeEvent vce) {
        vce.getComponent().processUpdates(FacesContext.getCurrentInstance());
        calculateOrderLine();
        calculateTotals();
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindSubTotal);
        AdfFacesContext.getCurrentInstance().addPartialTarget(gstTotal);
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindCgst);
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindSgst);
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindGrandTotal);
    }

    public void setBindProdCd(RichInputText bindProdCd) {
        this.bindProdCd = bindProdCd;
    }

    public RichInputText getBindProdCd() {
        return bindProdCd;
    }

    public void setBindUom(RichInputText bindUom) {
        this.bindUom = bindUom;
    }

    public RichInputText getBindUom() {
        return bindUom;
    }

    public void setBindQty(RichInputText bindQty) {
        this.bindQty = bindQty;
    }

    public RichInputText getBindQty() {
        return bindQty;
    }

    public void setBindRate(RichInputText bindRate) {
        this.bindRate = bindRate;
    }

    public RichInputText getBindRate() {
        return bindRate;
    }

    public void setBindGst(RichInputText bindGst) {
        this.bindGst = bindGst;
    }

    public RichInputText getBindGst() {
        return bindGst;
    }


    private double subTotal = 0.0;
    private double cgst = 0.0;
    private double sgst = 0.0;
    private double gst = 0.0;
    private double freight = 0.0;
    private double grandTotal = 0.0;

    public void onLineItemValueChanged(ValueChangeEvent event) {
        // 1. Force the new value into the component model immediately
        UIXEditableValue source = (UIXEditableValue) event.getComponent();
        //            source.validate();
        source.updateModel(FacesContext.getCurrentInstance());

        // 2. Recalculate totals
        calculateTotals();

        // 3. Clear component local values so fresh text renders
        source.setSubmittedValue(null);
    }

    public void calculateTotals() {
        subTotal = 0.0;
        gst = 0.0;
        try {
            BindingContext bctx = BindingContext.getCurrent();
            BindingContainer bindings = bctx.getCurrentBindingsEntry();
            DCDataControl dc = (DCDataControl) bindings.get("OrderAMDataControl"); // Verify your Data Control name

            if (dc != null) {
                oracle.jbo.ApplicationModule am = (oracle.jbo.ApplicationModule) dc.getDataProvider();
                ViewObject vo = am.findViewObject("OrderDetailVO2"); // Verify your VO instance name

                if (vo != null) {
                    subTotal = OrderDetailVORowImpl.calculateSubTotalFromRows(vo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        cgst = subTotal * 0.09;
        sgst = subTotal * 0.09;
        grandTotal = subTotal + cgst + sgst + freight;
    }


    //        // Getters for display
    //        public String getSubTotal() { return String.format("₹ %.2f", subTotal); }
    //        public String getCgst() { return String.format("₹ %.2f", cgst); }
    //        public String getSgst() { return String.format("₹ %.2f", sgst); }
    //        public String getFreight() { return String.format("₹ %.2f", freight); }
    //        public String getGrandTotal() { return String.format("₹ %.2f", grandTotal); }

    public void setBindSubTotal(RichOutputText bindSubTotal) {
        this.bindSubTotal = bindSubTotal;
    }

    public RichOutputText getBindSubTotal() {
        return bindSubTotal;
    }

    public void setBindCgst(RichOutputText bindCgst) {
        this.bindCgst = bindCgst;
    }

    public RichOutputText getBindCgst() {
        return bindCgst;
    }

    public void setBindSgst(RichOutputText bindSgst) {
        this.bindSgst = bindSgst;
    }

    public RichOutputText getBindSgst() {
        return bindSgst;
    }

    public void setBindGrandTotal(RichOutputText bindGrandTotal) {
        this.bindGrandTotal = bindGrandTotal;
    }

    public RichOutputText getBindGrandTotal() {
        return bindGrandTotal;
    }

    // 1. Calculate Sub-Total from all rows
    public BigDecimal getSubTotal() {
        BigDecimal subTotal = BigDecimal.ZERO;
        try {
            BindingContext bctx = BindingContext.getCurrent();
            BindingContainer bindings = bctx.getCurrentBindingsEntry();
            DCDataControl dc = (DCDataControl) bindings.get("OrderAMDataControl");
            if (dc != null) {
                ApplicationModule am = (ApplicationModule) dc.getDataProvider();
                ViewObject vo = am.findViewObject("OrderDetailVO2");
                if (vo != null) {
                    Row[] rows = vo.getAllRowsInRange();
                    for (Row r : rows) {
                        if (r instanceof OrderDetailVORowImpl) {
                            Object val = r.getAttribute("TaxableAmount");
                            if (val != null) {
                                BigDecimal amt = BigDecimal.ZERO;
                                if (val instanceof BigDecimal) {
                                    amt = (BigDecimal) val;
                                } else if (val instanceof oracle.jbo.domain.Number) {
                                    amt = ((oracle.jbo.domain.Number) val).bigDecimalValue();
                                } else {
                                    amt = new BigDecimal(val.toString());
                                }
                                subTotal = subTotal.add(amt);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subTotal;
    }

    // 2. Calculate CGST (9% of Sub-total)
    public BigDecimal getCgst() {

        //            BigDecimal cgstTotal = BigDecimal.ZERO;
        //                    try {
        //                        BindingContext bctx = BindingContext.getCurrent();
        //                        BindingContainer bindings = bctx.getCurrentBindingsEntry();
        //                        DCDataControl dc = (DCDataControl) bindings.get("OrderAMDataControl");
        //                        if (dc != null) {
        //                            ApplicationModule am = (ApplicationModule) dc.getDataProvider();
        //                            ViewObject vo = am.findViewObject("OrderDetailVO2");
        //                            if (vo != null) {
        //                                cgstTotal = OrderDetailVORowImpl.getCgstFromRows(vo);
        //                            }
        //                        }
        //                    } catch (Exception e) {
        //                        e.printStackTrace();
        //                    }
        //
        //
        //            BigDecimal cgst = getSubTotal().multiply(cgstTotal.setScale(2, BigDecimal.ROUND_HALF_UP));
        //            cgst = cgst.divide(new BigDecimal(100));
        //            return cgst;
        return getSubTotal().multiply(new BigDecimal("0.09")).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    // 3. Calculate SGST (9% of Sub-total)
    public BigDecimal getSgst() {
        //            BigDecimal sgstTotal = BigDecimal.ZERO;
        //                    try {
        //                        BindingContext bctx = BindingContext.getCurrent();
        //                        BindingContainer bindings = bctx.getCurrentBindingsEntry();
        //                        DCDataControl dc = (DCDataControl) bindings.get("OrderAMDataControl");
        //                        if (dc != null) {
        //                            ApplicationModule am = (ApplicationModule) dc.getDataProvider();
        //                            ViewObject vo = am.findViewObject("OrderDetailVO2");
        //                            if (vo != null) {
        //                                sgstTotal = OrderDetailVORowImpl.getSgstFromRows(vo);
        //                            }
        //                        }
        //                    } catch (Exception e) {
        //                        e.printStackTrace();
        //                    }
        //
        //
        //            BigDecimal sgst = getSubTotal().multiply(sgstTotal.setScale(2, BigDecimal.ROUND_HALF_UP));
        //            sgst = sgst.divide(new BigDecimal(100));
        //              return sgst;
        return getSubTotal().multiply(new BigDecimal("0.09")).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    // 4. Freight charges (Hardcoded or fetched from a field)
    public BigDecimal getFreight() {
        return new BigDecimal("0.00");
    }

    // 5. Grand Total = Sub-total + CGST + SGST + Freight
    public BigDecimal getGrandTotal() {
        BigDecimal sub = getSubTotal();
        BigDecimal cgst = getCgst();
        BigDecimal sgst = getSgst();
        BigDecimal gst = getGstAmountTotal();
        //        BigDecimal grandTotal = sub.add(cgst).add(sgst);
        BigDecimal grandTotal = sub.add(gst);

        return grandTotal;
    }

    public BigDecimal getGstAmountTotal() {
        BigDecimal totalGst = BigDecimal.ZERO;
        try {
            BindingContext bctx = BindingContext.getCurrent();
            BindingContainer bindings = bctx.getCurrentBindingsEntry();
            DCDataControl dc = (DCDataControl) bindings.get("OrderAMDataControl"); // Ensure matches your DC name
            if (dc != null) {
                ApplicationModule am = (ApplicationModule) dc.getDataProvider();
                ViewObject vo = am.findViewObject("OrderDetailVO2");


                if (vo != null) {
                    RowSetIterator rsi = vo.createRowSetIterator(null);
                    while (rsi.hasNext()) {
                        Row r = rsi.next();
                        if (r instanceof OrderDetailVORowImpl) {
                            Object val = r.getAttribute("GstAmount");
                            totalGst = totalGst.add(convertToBigDecimal(val));
                        }
                    }
                    rsi.closeRowSetIterator();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalGst;
    }


    public void saveOrder(ActionEvent actionEvent) {

        try {
            BindingContext bctx = BindingContext.getCurrent();
            BindingContainer bindings = bctx.getCurrentBindingsEntry();
            DCDataControl dc = (DCDataControl) bindings.get("OrderAMDataControl");
            if (dc != null) {
                ApplicationModule am = (ApplicationModule) dc.getDataProvider();
                ViewObject vo = am.findViewObject("OrderDetailVO2");
                if (vo.getEstimatedRowCount() > 0) {
                    //                DCBindingContainer bc = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
                    //                OperationBinding operationbinding = bc.getOperationBinding("Commit");
                    //                operationbinding.execute();
                    boolean result = commit();
                    if (result) {
                        ADFUtils.showMessage("Order created with Order Id : " +
                                             vo.getCurrentRow().getAttribute("OrderId"), 2);
                    } else {
                        ADFUtils.showMessage("Problem in commit!", 2);
                    }
                } else {
                    ADFUtils.showMessage("Please Add Items in Order Details.", 2);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean commit() {
        OperationBinding ob = ADFUtils.findOperation("Commit");
        ob.execute();
        if (!ob.getErrors().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public void setGstTotal(RichOutputText gstTotal) {
        this.gstTotal = gstTotal;
    }

    public RichOutputText getGstTotal() {
        return gstTotal;
    }

    private BigDecimal convertToBigDecimal(Object val) {
        if (val == null)
            return BigDecimal.ZERO;
        if (val instanceof BigDecimal)
            return (BigDecimal) val;
        if (val instanceof oracle.jbo.domain.Number) {
            return ((oracle.jbo.domain.Number) val).bigDecimalValue();
        }
        try {
            return new BigDecimal(val.toString());
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}
