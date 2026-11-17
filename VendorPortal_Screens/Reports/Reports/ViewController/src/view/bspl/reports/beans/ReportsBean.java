package view.bspl.reports.beans;


import adf.utils.bean.ADFBeanUtils;

import com.itextpdf.text.BaseColor;

import java.sql.Date;

import java.text.SimpleDateFormat;

import java.util.HashMap;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.event.ActionEvent;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.faces.event.ValueChangeEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCDataControl;
import oracle.adf.model.binding.DCIteratorBinding;

import oracle.adf.view.rich.component.rich.data.RichTable;

import oracle.adf.view.rich.component.rich.input.RichSelectOneRadio;

import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;

import oracle.binding.BindingContainer;

import oracle.binding.OperationBinding;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewObject;

import org.apache.myfaces.trinidad.context.RequestContext;

import java.io.Serializable;
import java.io.OutputStream;

import java.util.Iterator;

import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;

import oracle.jbo.Row;
import oracle.jbo.ViewObject;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Element;

import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.adf.view.rich.context.AdfFacesContext;

import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;

public class ReportsBean {

    // Manage current selected report type
    private String activeReportType = "SALE_ORDERS"; // Default

    // Filter properties
    private Date fromDate;
    private Date toDate;
    private String DocNo;
    private String statusFilter = "ALL"; // Default
    private String shipToFilter = "ALL"; // Default
    private String quickFilter;
    private String jasperReportName;
    private RichSelectOneRadio bindReportRadio;
    private RichPanelGroupLayout bindSOPanel;
    private RichPanelGroupLayout bindDAPanel;
    private RichPanelGroupLayout bindMainPanel;
    private RichPanelGroupLayout bindINVPanel;

    public ReportsBean() {
    }

    /**
     * Action Listener for 'Apply' button. Executes view object queries with criteria.
     */
    public void applyFiltersListener(ActionEvent actionEvent) {
        System.out.println(DocNo);
        if ((fromDate == null || toDate == null) && (DocNo == null || DocNo == "")) {
            FacesMessage message =
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Input Error", "From and To dates are required.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        // Logical check: From date must be before To date
        if (fromDate != null && toDate !=null) {
        if (fromDate.after(toDate)) {
            FacesMessage message =
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Input Error", "From Date cannot be later than To Date.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        }
        // Logic to apply filter to the currently active View Object.
        try {
            BindingContext bctx = BindingContext.getCurrent();
            BindingContainer bindings = bctx.getCurrentBindingsEntry();
            DCDataControl dc = (DCDataControl) bindings.get("ReportsAMDataControl"); // Verify your Data Control name
            /*--- For SO-----*/
            if (dc != null && bindReportRadio.getValue().toString().equals("S")) {
                oracle.jbo.ApplicationModule am = (oracle.jbo.ApplicationModule) dc.getDataProvider();
                ViewObject activeVO = am.findViewObject("SaleOrdersVO1"); // Verify your VO instance name
                if (activeVO != null) {
                    String vendorId = (String) resolveExpression("#{pageFlowScope.vendorId}");
                    OperationBinding ob = ADFUtils.findOperation("getCustomerCode");
                    ob.getParamsMap().put("vendorId", vendorId);
                    String custCd = ob.execute().toString();
                    if (custCd != null) {
                        activeVO.setWhereClause(null); // Reset VC
                        System.out.println(fromDate);
                        System.out.println(convertDate(fromDate));
                        activeVO.setNamedWhereClauseParam("P_FROM_DATE", convertDate(fromDate));
                        activeVO.setNamedWhereClauseParam("P_TO_DATE", convertDate(toDate));
                        activeVO.setNamedWhereClauseParam("P_CUST_CD", custCd);
                        activeVO.setNamedWhereClauseParam("P_ACK_NUMBER", DocNo);
                        // Apply Status and Ship-To filters as required, possibly using a ViewCriteria

                        activeVO.executeQuery();
                        System.out.println("Total Rows found : " + activeVO.getEstimatedRowCount());
                        System.out.println("Filters applied for " + activeReportType);
                    } else {
                        FacesMessage message =
                            new FacesMessage(FacesMessage.SEVERITY_WARN, "Customer Code", "Customer not found!");
                        FacesContext.getCurrentInstance().addMessage(null, message);

                    }
                }
                bindSOPanel.setVisible(true);
                
            }
            /*--- For DA-----*/
            if (dc != null && bindReportRadio.getValue().toString().equals("D")) {
                oracle.jbo.ApplicationModule am = (oracle.jbo.ApplicationModule) dc.getDataProvider();
                ViewObject activeVO = am.findViewObject("DispatchOrdersVO1"); // Verify your VO instance name
                if (activeVO != null) {
                    String vendorId = (String) resolveExpression("#{pageFlowScope.vendorId}");
                    OperationBinding ob = ADFUtils.findOperation("getCustomerCode");
                    ob.getParamsMap().put("vendorId", vendorId);
                    String custCd = ob.execute().toString();
                    if (custCd != null) {
                        activeVO.setWhereClause(null); // Reset VC
                        System.out.println(fromDate);
                        System.out.println(convertDate(fromDate));
                        activeVO.setNamedWhereClauseParam("P_FROM_DATE", convertDate(fromDate));
                        activeVO.setNamedWhereClauseParam("P_TO_DATE", convertDate(toDate));
                        activeVO.setNamedWhereClauseParam("P_CUST_CD", custCd);
                        activeVO.setNamedWhereClauseParam("P_DOC_NO", DocNo);
                        

                        // Apply Status and Ship-To filters as required, possibly using a ViewCriteria

                        activeVO.executeQuery();
                        System.out.println("Total Rows found : " + activeVO.getEstimatedRowCount());
                        System.out.println("Filters applied for " + activeReportType);
                    } else {
                        FacesMessage message =
                            new FacesMessage(FacesMessage.SEVERITY_WARN, "Customer Code", "Customer not found!");
                        FacesContext.getCurrentInstance().addMessage(null, message);

                    }
                }
            }
            
            /*--- For INV-----*/
            if (dc != null && bindReportRadio.getValue().toString().equals("I")) {
                oracle.jbo.ApplicationModule am = (oracle.jbo.ApplicationModule) dc.getDataProvider();
                ViewObject activeVO = am.findViewObject("InvoicesVO1"); // Verify your VO instance name
                if (activeVO != null) {
                    String vendorId = (String) resolveExpression("#{pageFlowScope.vendorId}");
                    OperationBinding ob = ADFUtils.findOperation("getCustomerCode");
                    ob.getParamsMap().put("vendorId", vendorId);
                    String custCd = ob.execute().toString();
                    if (custCd != null) {
                        activeVO.setWhereClause(null); // Reset VC
                        System.out.println(fromDate);
                        System.out.println(convertDate(fromDate));
                        activeVO.setNamedWhereClauseParam("P_FROM_DATE", convertDate(fromDate));
                        activeVO.setNamedWhereClauseParam("P_TO_DATE", convertDate(toDate));
                        activeVO.setNamedWhereClauseParam("P_CUST_CODE", custCd);
                        activeVO.setNamedWhereClauseParam("P_INV_NO", DocNo);
                        

                        // Apply Status and Ship-To filters as required, possibly using a ViewCriteria

                        activeVO.executeQuery();
                        System.out.println("Total Rows found : " + activeVO.getEstimatedRowCount());
                        System.out.println("Filters applied for " + activeReportType);
                    } else {
                        FacesMessage message =
                            new FacesMessage(FacesMessage.SEVERITY_WARN, "Customer Code", "Customer not found!");
                        FacesContext.getCurrentInstance().addMessage(null, message);

                    }
                }
            }
            
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindMainPanel);

    }

    public static Object resolveExpression(String expression) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application app = facesContext.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        ValueExpression valueExp = elFactory.createValueExpression(elContext, expression, Object.class);
        return valueExp.getValue(elContext);
    }


    public String convertDate(java.sql.Date inputDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");

            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yy");

            Date date = (Date) (inputDate);

            return outputFormat.format(date);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Action Listener for 'Reset' button. Clears all filters and re-executes.
     */
    public void resetFiltersListener(ActionEvent actionEvent) {
        this.fromDate = null;
        this.DocNo = null;
        this.toDate = null;
        this.statusFilter = "ALL";
        this.shipToFilter = "ALL";
        this.quickFilter = null;


        try {
            BindingContext bctx = BindingContext.getCurrent();
            BindingContainer bindings = bctx.getCurrentBindingsEntry();
            DCDataControl dc = (DCDataControl) bindings.get("ReportsAMDataControl"); // Verify your Data Control name

            if (dc != null) {
                oracle.jbo.ApplicationModule am = (oracle.jbo.ApplicationModule) dc.getDataProvider();
                ViewObject activeVO = am.findViewObject("SaleOrdersVO1"); // Verify your VO instance name
                if (activeVO != null) {
                    activeVO.setWhereClause(null); // Reset VC
                    // activeVO.setNamedWhereClauseParam("p_fromDate", null); // Reset params
                    // activeVO.setNamedWhereClauseParam("p_toDate", null);
                    activeVO.executeQuery();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Action Listener for Report Type Selection buttons.
     */
    public void reportTypeSelectListener(ActionEvent actionEvent) {
        String reportType = (String) actionEvent.getComponent().getAttributes().get("reportType");
        if (reportType != null) {
            this.activeReportType = reportType;
            System.out.println("Switching to report: " + activeReportType);

            // Re-apply current filters to the newly selected VO or execute it plainly
            resetFiltersListener(actionEvent);
        }
    }

    /**
     * Placeholder method for handling Quick Filter buttons.
     */
    public void quickFilterListener(ActionEvent actionEvent) {
        this.quickFilter = (String) actionEvent.getComponent().getAttributes().get("quickFilter");
        System.out.println("Quick filter selected: " + quickFilter);

        // Add logic to determine From/To dates based on quickFilter selection (e.g., 'this month')
    }

    /**
     * Utility method to get the active ViewObject based on current selection.
     */
    private ViewObject getActiveViewObject() {
        String iteratorName = null;
        if ("SALE_ORDERS".equals(activeReportType))
            iteratorName = "SaleOrdersVO1Iterator";
        else if ("DISPATCH_ADVICE".equals(activeReportType))
            iteratorName = "DispatchAdviceVO1Iterator";
        else if ("INVOICE".equals(activeReportType))
            iteratorName = "InvoiceVO1Iterator";

        if (iteratorName != null) {
            DCBindingContainer bindings = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
            DCIteratorBinding iterator = bindings.findIteratorBinding(iteratorName);
            return iterator.getViewObject();
        }
        return null;
    }

    // Getters and Setters
    public String getActiveReportType() {
        return activeReportType;
    }

    public void setActiveReportType(String activeReportType) {
        this.activeReportType = activeReportType;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
    
    public String getDocNo() {
        return DocNo;
    }

    public void setDocNo(String DocNo) {
        this.DocNo = DocNo;
    }

    public String getStatusFilter() {
        return statusFilter;
    }

    public void setStatusFilter(String statusFilter) {
        this.statusFilter = statusFilter;
    }

    public String getShipToFilter() {
        return shipToFilter;
    }

    public void setShipToFilter(String shipToFilter) {
        this.shipToFilter = shipToFilter;
    }

    public String getQuickFilter() {
        return quickFilter;
    }

    public void setQuickFilter(String quickFilter) {
        this.quickFilter = quickFilter;
    }


    public void downloadSO_PDF(FacesContext facesContext, OutputStream outputStream) {

        Document document = null;
        RowSetIterator rsi = null;

        try {

            // Get ADF bindings
            BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();

            DCBindingContainer dcBindings = (DCBindingContainer) bindings;

            // Replace with your actual VO iterator name
            DCIteratorBinding iterator = dcBindings.findIteratorBinding("SaleOrdersVO1Iterator");

            if (iterator == null) {
                throw new Exception("SaleOrdersVO1Iterator not found.");
            }

            ViewObject vo = iterator.getViewObject();

            // Create PDF document
            document = new Document(PageSize.A4);

            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            // Let ADF manage the output stream
            writer.setCloseStream(false);

            document.open();

            // Fonts
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.BLACK);

            Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);

            Font dataFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);

            // Report heading
            Paragraph title = new Paragraph("Sale Order Report", titleFont);

            title.setAlignment(Element.ALIGN_CENTER);

            document.add(title);

            document.add(new Paragraph(" "));

            // PDF table
            PdfPTable pdfTable = new PdfPTable(8);

            pdfTable.setWidthPercentage(100);

            pdfTable.setWidths(new float[] { 2.5f, 4.5f, 3.5f, 3.5f, 5.5f, 2.5f, 2.5f, 2.5f });

            // Headers
            addHeader(pdfTable, "Unit", headerFont);
            addHeader(pdfTable, "Sale Order No.", headerFont);
            addHeader(pdfTable, "SO Date", headerFont);
            addHeader(pdfTable, "PO Ref No.", headerFont);
            addHeader(pdfTable, "Ship To", headerFont);
            addHeader(pdfTable, "Quantity", headerFont);
            addHeader(pdfTable, "Price", headerFont);
            addHeader(pdfTable, "Status", headerFont);

            // Read all VO records
            rsi = vo.createRowSetIterator(null);

            int count = 0;

            while (rsi.hasNext()) {

                Row row = rsi.next();

                addCell(pdfTable, getValue(row, "UnitCode"), dataFont);
                addCell(pdfTable, getValue(row, "AckNumber"), dataFont);
                addCell(pdfTable, getValue(row, "AckDate"), dataFont);
                addCell(pdfTable, getValue(row, "CustPoNumber"), dataFont);
                addCell(pdfTable, getValue(row, "ShipToName"), dataFont);
                addCell(pdfTable, getValue(row, "Quantity"), dataFont);
                addCell(pdfTable, getValue(row, "Price"), dataFont);
                addCell(pdfTable, getValue(row, "Status"), dataFont);
                count++;
            }

            if (count == 0) {

                PdfPCell emptyCell = new PdfPCell(new Paragraph("No records found.", dataFont));

                emptyCell.setColspan(4);
                emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);

                pdfTable.addCell(emptyCell);
            }

            // IMPORTANT: Add table to PDF
            document.add(pdfTable);

            // Finish PDF generation
            document.close();
            document = null;

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException("Unable to generate PDF: " + e.getMessage(), e);

        } finally {

            if (rsi != null) {
                rsi.closeRowSetIterator();
            }

            if (document != null && document.isOpen()) {
                document.close();
            }
        }
    }


    private void addHeader(PdfPTable table, String value, Font font) {

        PdfPCell cell = new PdfPCell(new Paragraph(value, font));

        cell.setBackgroundColor(new BaseColor(45, 85, 125));

        cell.setPadding(7);

        table.addCell(cell);
    }


    private void addCell(PdfPTable table, String value, Font font) {

        PdfPCell cell = new PdfPCell(new Paragraph(value, font));

        cell.setPadding(6);

        table.addCell(cell);
    }


    private String getValue(Row row, String attribute) {

        Object value = row.getAttribute(attribute);

        return value == null ? "" : value.toString();
    }


    public void downloadDA_PDF(FacesContext facesContext, OutputStream outputStream) {

        Document document = null;
        RowSetIterator rsi = null;

        try {

            // Get ADF bindings
            BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();

            DCBindingContainer dcBindings = (DCBindingContainer) bindings;

            // Replace with your actual VO iterator name
            DCIteratorBinding iterator = dcBindings.findIteratorBinding("DispatchOrdersVO1Iterator");

            if (iterator == null) {
                throw new Exception("DispatchOrdersVO1Iterator not found.");
            }

            ViewObject vo = iterator.getViewObject();

            // Create PDF document
            document = new Document(PageSize.A4);

            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            // Let ADF manage the output stream
            writer.setCloseStream(false);

            document.open();

            // Fonts
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.BLACK);

            Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);

            Font dataFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);

            // Report heading
            Paragraph title = new Paragraph("Dispatch Order Report", titleFont);

            title.setAlignment(Element.ALIGN_CENTER);

            document.add(title);

            document.add(new Paragraph(" "));

            // PDF table
            PdfPTable pdfTable = new PdfPTable(7);

            pdfTable.setWidthPercentage(100);

            pdfTable.setWidths(new float[] { 2.5f, 4.5f, 3.5f, 3.5f, 5.5f, 2.5f, 2.5f });

            // Headers
            addHeader(pdfTable, "DA No.", headerFont);
            addHeader(pdfTable, "DA Date", headerFont);
            addHeader(pdfTable, "SO Ref No.", headerFont);
            addHeader(pdfTable, "Transporter", headerFont);
            addHeader(pdfTable, "Vehicle", headerFont);
            addHeader(pdfTable, "Quantity", headerFont);
            addHeader(pdfTable, "Status", headerFont);

            // Read all VO records
            rsi = vo.createRowSetIterator(null);

            int count = 0;

            while (rsi.hasNext()) {

                Row row = rsi.next();

                addCell(pdfTable, getValue(row, "DaNo"), dataFont);
                addCell(pdfTable, getValue(row, "DaDate"), dataFont);
                addCell(pdfTable, getValue(row, "SoRef"), dataFont);
                addCell(pdfTable, getValue(row, "Transporter"), dataFont);
                addCell(pdfTable, getValue(row, "Vehicle"), dataFont);
                addCell(pdfTable, getValue(row, "Qty"), dataFont);
                addCell(pdfTable, getValue(row, "Status"), dataFont);
                count++;
            }

            if (count == 0) {

                PdfPCell emptyCell = new PdfPCell(new Paragraph("No records found.", dataFont));

                emptyCell.setColspan(4);
                emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);

                pdfTable.addCell(emptyCell);
            }

            // IMPORTANT: Add table to PDF
            document.add(pdfTable);

            // Finish PDF generation
            document.close();
            document = null;

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException("Unable to generate PDF: " + e.getMessage(), e);

        } finally {

            if (rsi != null) {
                rsi.closeRowSetIterator();
            }

            if (document != null && document.isOpen()) {
                document.close();
            }
        }
    }
    
    public void downloadINV_PDF(FacesContext facesContext, OutputStream outputStream) {

        Document document = null;
        RowSetIterator rsi = null;

        try {

            // Get ADF bindings
            BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();

            DCBindingContainer dcBindings = (DCBindingContainer) bindings;

            // Replace with your actual VO iterator name
            DCIteratorBinding iterator = dcBindings.findIteratorBinding("InvoicesVO1Iterator");

            if (iterator == null) {
                throw new Exception("InvoicesVO1Iterator not found.");
            }

            ViewObject vo = iterator.getViewObject();

            // Create PDF document
            document = new Document(PageSize.A4);

            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            // Let ADF manage the output stream
            writer.setCloseStream(false);

            document.open();

            // Fonts
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.BLACK);

            Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);

            Font dataFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);

            // Report heading
            Paragraph title = new Paragraph("Dispatch Order Report", titleFont);

            title.setAlignment(Element.ALIGN_CENTER);

            document.add(title);

            document.add(new Paragraph(" "));

            // PDF table
            PdfPTable pdfTable = new PdfPTable(8);

            pdfTable.setWidthPercentage(100);

            pdfTable.setWidths(new float[] {2.5f, 4.5f, 3.5f, 3.5f, 3.5f, 3f, 3.5f, 2.5f });

            // Headers
            addHeader(pdfTable, "Unit", headerFont);
            addHeader(pdfTable, "Invoice No.", headerFont);
            addHeader(pdfTable, "Invoice Date", headerFont);
            addHeader(pdfTable, "SO Ref No.", headerFont);
            addHeader(pdfTable, "Taxable Amount", headerFont);
            addHeader(pdfTable, "Total Amount", headerFont);
            addHeader(pdfTable, "Due Date", headerFont);
            addHeader(pdfTable, "Status", headerFont);

            // Read all VO records
            rsi = vo.createRowSetIterator(null);

            int count = 0;

            while (rsi.hasNext()) {

                Row row = rsi.next();

                addCell(pdfTable, getValue(row, "UnitCode"), dataFont);
                addCell(pdfTable, getValue(row, "InvNo"), dataFont);
                addCell(pdfTable, getValue(row, "InvDate"), dataFont);
                addCell(pdfTable, getValue(row, "SaleOrderNumber"), dataFont);
                addCell(pdfTable, getValue(row, "TaxableAmount"), dataFont);
                addCell(pdfTable, getValue(row, "TotalAmt"), dataFont);
                addCell(pdfTable, getValue(row, "DueDate"), dataFont);
                addCell(pdfTable, getValue(row, "Status"), dataFont);
                count++;
            }

            if (count == 0) {

                PdfPCell emptyCell = new PdfPCell(new Paragraph("No records found.", dataFont));

                emptyCell.setColspan(4);
                emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);

                pdfTable.addCell(emptyCell);
            }

            // IMPORTANT: Add table to PDF
            document.add(pdfTable);

            // Finish PDF generation
            document.close();
            document = null;

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException("Unable to generate PDF: " + e.getMessage(), e);

        } finally {

            if (rsi != null) {
                rsi.closeRowSetIterator();
            }

            if (document != null && document.isOpen()) {
                document.close();
            }
        }
    }

    public void onClickPurchaseOrderPrint(ActionEvent actionEvent) {
        try {
            DCIteratorBinding poIter = (DCIteratorBinding) getBindings().get("SearchPurOrderVO1Iterator");
            String poNum = poIter.getCurrentRow().getAttribute("PoNo").toString();
            String unitCode = poIter.getCurrentRow().getAttribute("UnitCd").toString();
            System.out.println("Po Number :- " + poNum + " " + unitCode);
            FacesContext ctx = FacesContext.getCurrentInstance();
            ExtendedRenderKitService erks =
                org.apache.myfaces.trinidad.util.Service.getRenderKitService(ctx, ExtendedRenderKitService.class);
            HttpServletRequest request = (HttpServletRequest) getExternalContext().getRequest();
            HttpServletResponse resp = (HttpServletResponse) ctx.getExternalContext().getResponse();
            String strWinOpen = null;
            PrintWriter out = resp.getWriter();
            String URL = request.getRequestURL().toString();
            int Counn = URL.lastIndexOf("faces");
            //        System.out.println(Counn);
            String requestUrl = URL.substring(0, Counn);
            //            System.out.println(requestUrl);
            if (!requestUrl.equals(null)) {

                strWinOpen =
                    "window.open('" + requestUrl + "/mtltrnacationuploadfile?RepId=ERP0000000039-001&UnitCd=" +
                    unitCode + "&fromName=MMPO&poNum=" + poNum +
                    "','','width=800,height=600,directories=no,titlebar=no,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no');";
            }

            erks.addScript(FacesContext.getCurrentInstance(), strWinOpen);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setJasperReportName(String jasperReportName) {
        this.jasperReportName = jasperReportName;
    }

    public String getJasperReportName() {
        System.out.println(bindReportRadio.getValue().toString());
        String path = null;
        if (bindReportRadio.getValue().toString().equals("S"))
            path = ADFBeanUtils.jasperReportFileName("ERP0000000548");
        if (bindReportRadio.getValue().toString().equals("D"))
            path = ADFBeanUtils.jasperReportFileName("ERP0000001655");
        if (bindReportRadio.getValue().toString().equals("I"))
            path = ADFBeanUtils.jasperReportFileName("ERP0000000142");
        if (bindReportRadio.getValue().toString().equals("N"))
            path = ADFBeanUtils.jasperReportFileName("ERP0000000548");
        //        logger.info("Inside Getter of  Path: " + path);
        if (path != null) {
            return path;
        }

        return jasperReportName;
    }


    public BindingContainer getBindings() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    public static ExternalContext getExternalContext() {
        return getFacesContext().getExternalContext();
    }

    public static FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    public void setBindReportRadio(RichSelectOneRadio bindReportRadio) {
        this.bindReportRadio = bindReportRadio;
    }

    public RichSelectOneRadio getBindReportRadio() {
        return bindReportRadio;
    }

    public void setBindSOPanel(RichPanelGroupLayout bindSOPanel) {
        this.bindSOPanel = bindSOPanel;
    }

    public RichPanelGroupLayout getBindSOPanel() {
        return bindSOPanel;
    }

    public void setBindDAPanel(RichPanelGroupLayout bindDAPanel) {
        this.bindDAPanel = bindDAPanel;
    }

    public RichPanelGroupLayout getBindDAPanel() {
        return bindDAPanel;
    }

    public void reportRadioVce(ValueChangeEvent vce) {
        vce.getComponent().processUpdates(FacesContext.getCurrentInstance());
        if (vce.getNewValue().toString().equals("S")) {
            bindSOPanel.setVisible(true);
            bindDAPanel.setVisible(false);
            bindINVPanel.setVisible(false);
        }
        if (vce.getNewValue().toString().equals("D")) {
            bindSOPanel.setVisible(false);
            bindDAPanel.setVisible(true);
            bindINVPanel.setVisible(false);
        }
        if (vce.getNewValue().toString().equals("I")) {
            bindSOPanel.setVisible(false);
            bindDAPanel.setVisible(false);
            bindINVPanel.setVisible(true);
        }

        AdfFacesContext.getCurrentInstance().addPartialTarget(bindMainPanel);
    }

    public void setBindMainPanel(RichPanelGroupLayout bindMainPanel) {
        this.bindMainPanel = bindMainPanel;
    }

    public RichPanelGroupLayout getBindMainPanel() {
        return bindMainPanel;
    }

    public void setBindINVPanel(RichPanelGroupLayout bindINVPanel) {
        this.bindINVPanel = bindINVPanel;
    }

    public RichPanelGroupLayout getBindINVPanel() {
        return bindINVPanel;
    }
}
