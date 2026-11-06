
package view.bspl.track.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewObject;


public class OrderTrackingBean implements Serializable {

    private static final long serialVersionUID = 1L;


    /* =========================================
       PAGE PROPERTIES
    ========================================= */

    private String searchNumber;

    private String sessionId;
    private String orderNo;
    private String documentNo;
    private String currentStatus;

    private String customerCode;
    private String unitCode;
    private String amount;
    private String creationDate;

    private String mapHtml;

    private List<TrackingStep> trackingSteps =
        new ArrayList<TrackingStep>();


    /* =========================================
       TRACKING STAGES
    ========================================= */

    private static final String[] STAGE_NAMES = {
        "Order Placed",
        "Approved",
        "Processing",
        "Dispatched",
        "In Transit",
        "Delivered"
    };


    /* =========================================
       TRACK ORDER
    ========================================= */

    public String trackOrder() {

        trackingSteps.clear();

        sessionId = "";
        orderNo = "";
        documentNo = "";
        customerCode = "";
        unitCode = "";
        amount = "";
        creationDate = "";
        currentStatus = "";

        if (searchNumber == null ||
            searchNumber.trim().isEmpty()) {

            currentStatus = "Enter Order Number";
            return null;
        }

        try {

            DCBindingContainer bindings =
                (DCBindingContainer) BindingContext
                    .getCurrent()
                    .getCurrentBindingsEntry();

            DCIteratorBinding iterator =
                bindings.findIteratorBinding(
                    "TrackOrdersVO1Iterator"
                );

            if (iterator == null) {

                currentStatus = "Tracking VO not found";
                return null;
            }

            ViewObject vo = iterator.getViewObject();

           
            vo.setWhereClause("DOC_NO = '"+searchNumber+"'");
            vo.executeQuery();

            RowSetIterator rsi =
                vo.createRowSetIterator(null);

            Row[] stageRows =
                new Row[STAGE_NAMES.length];

            int activeIndex = -1;

            boolean orderFound = false;

            try {

                while (rsi.hasNext()) {

                    Row row = rsi.next();

                    orderFound = true;

                    /*
                     * FIRST ROW: ORDER DETAILS
                     */

                    if (documentNo.isEmpty()) {

                        documentNo =
                            getString(row, "DocNo");

                        orderNo = documentNo;

                        sessionId =
                            getString(row, "SessionId");

                        customerCode =
                            getString(row, "CustCode");

                        unitCode =
                            getString(row, "UnitCd");

                        amount =
                            getString(row, "Amount");

                        creationDate =
                            formatDate(
                                getAttribute(row, "CreationDate")
                            );
                    }


                    /*
                     * IDENTIFY STAGE
                     */

                    String status =
                        getString(row, "Remarks");

                    if (status.isEmpty()) {

                        status =
                            getString(row, "Status");
                    }

                    int stageIndex =
                        findStageIndex(status);

                    if (stageIndex >= 0) {

                        Row oldRow = stageRows[stageIndex];

                        if (oldRow == null ||
                            isLaterDate(
                                getAttribute(row, "CreationDate"),
                                getAttribute(oldRow, "CreationDate")
                            )) {

                            stageRows[stageIndex] = row;
                        }

                        if (stageIndex > activeIndex) {

                            activeIndex = stageIndex;
                        }
                    }
                }

            } finally {

                rsi.closeRowSetIterator();
            }


            /*
             * NO RECORD FOUND
             */

            if (!orderFound) {

                currentStatus = "Order Not Found";
                return null;
            }


            /*
             * DEFAULT FIRST STAGE
             */

            if (activeIndex < 0) {
                activeIndex = 0;
            }


            /*
             * BUILD TIMELINE
             */

            buildTimeline(stageRows, activeIndex);


            /*
             * CURRENT STATUS
             */

            currentStatus = STAGE_NAMES[activeIndex];

        } catch (Exception e) {

            e.printStackTrace();

            currentStatus = "Unable to load tracking details";
        }

        return null;
    }


    /* =========================================
       BUILD TIMELINE
    ========================================= */

    private void buildTimeline(
        Row[] stageRows,
        int activeIndex) {

        trackingSteps.clear();

        for (int i = 0; i < STAGE_NAMES.length; i++) {

            TrackingStep step = new TrackingStep();

            step.setName(STAGE_NAMES[i]);


            /*
             * ICON STATUS
             */

            if (i < activeIndex) {

                step.setStyleClass("completed");

                step.setIcon("\u2713");

            } else if (i == activeIndex) {

                step.setStyleClass("active");

                step.setIcon("\u25CF");

            } else {

                step.setStyleClass("pending");

                step.setIcon("\u25CB");
            }


            /*
             * CONNECTING LINE STATUS
             */

            if (i == STAGE_NAMES.length - 1) {

                step.setConnectorClass("connector-last");

            } else if (i < activeIndex) {

                step.setConnectorClass(
                    "connector-completed"
                );

            } else {

                step.setConnectorClass(
                    "connector-pending"
                );
            }


            /*
             * STAGE DATE
             */

            Row stageRow = stageRows[i];

            if (stageRow != null) {

                step.setCreationDate(
                    formatDate(
                        getAttribute(
                            stageRow,
                            "CreationDate"
                        )
                    )
                );

            } else {

                step.setCreationDate("");
            }

            trackingSteps.add(step);
        }
    }


    /* =========================================
       IDENTIFY TRACKING STAGE
    ========================================= */

    private int findStageIndex(String remarks) {

        if (remarks == null ||
            remarks.trim().isEmpty()) {

            return -1;
        }

        String status = remarks.toLowerCase()
                               .trim()
                               .replaceAll("[^a-z]", "");


        if (status.contains("orderplaced") ||
            status.contains("created") ||
            status.contains("booking")) {

            return 0;
        }

        if (status.contains("approved") ||
            status.contains("approval")) {

            return 1;
        }

        if (status.contains("processing") ||
            status.contains("production")) {

            return 2;
        }

        if (status.contains("dispatched") ||
            status.contains("dispatch")) {

            return 3;
        }

        if (status.contains("intransit") ||
            status.contains("transit")) {

            return 4;
        }

        if (status.contains("delivered") ||
            status.contains("delivery")) {

            return 5;
        }

        return -1;
    }


    /* =========================================
       GET ATTRIBUTE SAFELY
    ========================================= */

    private Object getAttribute(Row row, String name) {

        try {

            return row.getAttribute(name);

        } catch (Exception e) {

            return null;
        }
    }


    private String getString(Row row, String name) {

        Object value = getAttribute(row, name);

        return value == null ? "" : value.toString();
    }


    /* =========================================
       DATE FORMAT
    ========================================= */

    private String formatDate(Object value) {

        if (value == null) {
            return "";
        }

        if (value instanceof Date) {

            SimpleDateFormat sdf =
                new SimpleDateFormat(
                    "dd-MMM-yyyy hh:mm a"
                );

            return sdf.format((Date) value);
        }

        return value.toString();
    }


    /* =========================================
       LATEST DATE
    ========================================= */

    private boolean isLaterDate(
        Object newDate,
        Object oldDate) {

        if (newDate == null) {
            return false;
        }

        if (oldDate == null) {
            return true;
        }

        if (newDate instanceof Date &&
            oldDate instanceof Date) {

            return ((Date) newDate).after(
                (Date) oldDate
            );
        }

        return false;
    }


    /* =========================================
       SHARE STATUS
    ========================================= */

    public String shareEta() {
        return null;
    }


    /* =========================================
       GETTERS AND SETTERS
    ========================================= */

    public String getSearchNumber() {
        return searchNumber;
    }

    public void setSearchNumber(String searchNumber) {
        this.searchNumber = searchNumber;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getDocumentNo() {
        return documentNo;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public String getAmount() {
        return amount;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getMapHtml() {
        return mapHtml;
    }

    public void setMapHtml(String mapHtml) {
        this.mapHtml = mapHtml;
    }

    public List<TrackingStep> getTrackingSteps() {
        return trackingSteps;
    }


    /* =========================================
       TRACKING STEP MODEL
    ========================================= */

    public static class TrackingStep
        implements Serializable {

        private static final long serialVersionUID = 1L;

        private String name;
        private String creationDate;
        private String styleClass;
        private String connectorClass;
        private String icon;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCreationDate() {
            return creationDate;
        }

        public void setCreationDate(String creationDate) {
            this.creationDate = creationDate;
        }

        public String getStyleClass() {
            return styleClass;
        }

        public void setStyleClass(String styleClass) {
            this.styleClass = styleClass;
        }

        public String getConnectorClass() {
            return connectorClass;
        }

        public void setConnectorClass(String connectorClass) {
            this.connectorClass = connectorClass;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
