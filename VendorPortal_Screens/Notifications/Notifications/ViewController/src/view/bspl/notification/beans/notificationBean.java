package view.bspl.notification.beans;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.event.ActionEvent;

import oracle.adf.view.rich.component.rich.data.RichTable;

import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.component.rich.nav.RichLink;

import oracle.adf.view.rich.component.rich.output.RichOutputText;

import oracle.jbo.Row;
import oracle.jbo.ViewObject;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;


import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.binding.OperationBinding;

import oracle.jbo.RowSetIterator;

public class notificationBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Notification> notifications;

    // =========================
    // PAGINATION VARIABLES
    // =========================

    private int currentPage = 0;

    private static final int PAGE_SIZE = 5;
    private RichTable bindNotificationTable;
    private RichPopup bindAlertPop;
    private RichOutputText bindNotificationId;
    private RichPanelGroupLayout bindMsgStatusPanel;
    private RichPanelGroupLayout notificationList;
    public static String popTitle =null;
    public static String popMsg =null;

    public notificationBean() {
        loadNotifications(null);
    }

    public static void setPopTitle(String popTitle) {
        notificationBean.popTitle = popTitle;
    }

    public String getPopTitle() {
        return popTitle;
    }


    /**
     * Fetch notifications from NotificationVO.
     */
    public void loadNotifications(String type) {

        notifications = new ArrayList<Notification>();

        // Reset to the first page when notifications are reloaded
        currentPage = 0;

        try {

            DCBindingContainer bindings = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();

            DCIteratorBinding iterator = bindings.findIteratorBinding("MsgNotificationSynVO1Iterator");

            if (iterator == null) {
                return;
            }

            ViewObject vo = iterator.getViewObject();

            if (type != null) {
                vo.setWhereClause("MsgNotificationSynEO.DOC_TYPE = '" + type + "'");
            }


            vo.executeQuery();

            vo.reset();

            vo.setWhereClause(null);

            RowSetIterator iter = vo.createRowSetIterator(null);

            while (iter.hasNext()) {

                Row row = iter.next();

                Notification notification = new Notification();

                notification.setNotificationId(row.getAttribute("LineId"));

                notification.setType(getString(row.getAttribute("DocType")));

                notification.setTitle(getString(row.getAttribute("Subject")));

                notification.setMessage(getString(row.getAttribute("Body")));

                notification.setRead("R".equalsIgnoreCase(getString(row.getAttribute("Status"))));
                
                notification.setStatus(getString(row.getAttribute("Status")));

                notifications.add(notification);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    private String getString(Object value) {

        return value == null ? "" : value.toString();
    }


    // =========================
    // EXISTING NOTIFICATION LIST
    // =========================

    public List<Notification> getNotifications() {

        if (notifications == null) {
            loadNotifications(null);
        }

        return notifications;
    }


    public int getNotificationCount() {

        return notifications == null ? 0 : notifications.size();
    }


    public int getUnreadCount() {

        int count = 0;

        if (notifications != null) {

            for (Notification n : notifications) {

                if (!n.isRead()) {
                    count++;
                }
            }
        }

        return count;
    }


    // =========================
    // PAGINATION METHODS
    // =========================

    /**
     * Returns only 8 notifications for the current page.
     */
    public List<Notification> getPageNotifications() {

        if (notifications == null || notifications.isEmpty()) {
            return Collections.emptyList();
        }

        int totalPages = getTotalPages();

        // Prevent an invalid page index
        if (currentPage >= totalPages) {
            currentPage = totalPages - 1;
        }

        if (currentPage < 0) {
            currentPage = 0;
        }

        int startIndex = currentPage * PAGE_SIZE;

        int endIndex = Math.min(startIndex + PAGE_SIZE, notifications.size());

        return notifications.subList(startIndex, endIndex);
    }


    /**
     * Returns the total number of pages.
     */
    public int getTotalPages() {

        int count = getNotificationCount();

        if (count == 0) {
            return 1;
        }

        return (count + PAGE_SIZE - 1) / PAGE_SIZE;
    }


    /**
     * Current page number for display.
     * The displayed page number starts from 1.
     */
    public int getCurrentPageNumber() {

        return currentPage + 1;
    }


    /**
     * Determines whether Previous is enabled.
     */
    public boolean isHasPreviousPage() {

        return currentPage > 0;
    }


    /**
     * Determines whether Next is enabled.
     */
    public boolean isHasNextPage() {

        return currentPage < getTotalPages() - 1;
    }


    /**
     * Navigate to the previous page.
     */
    public String previousPage() {

        if (currentPage > 0) {
            currentPage--;
        }

        return null;
    }


    /**
     * Navigate to the next page.
     */
    public String nextPage() {

        if (currentPage < getTotalPages() - 1) {
            currentPage++;
        }

        return null;
    }


    /**
     * Reset pagination to the first page.
     */
    public void resetPagination() {

        currentPage = 0;
    }


    // =========================
    // MARK ALL AS READ
    // =========================

    public void markAllAsRead() {

        try {

            if (notifications != null) {

                for (Notification n : notifications) {
                    n.setRead(true);
                }
            }

            // Update the database here if required.

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    // =========================
    // NOTIFICATION TYPE CSS
    // =========================

    public String getTypeClass(String type) {

        if (type == null) {
            return "notification-info";
        }

        if ("PAYMENT".equalsIgnoreCase(type)) {
            return "notification-payment";
        }

        if ("ORDER".equalsIgnoreCase(type)) {
            return "notification-order";
        }

        if ("DOCUMENT".equalsIgnoreCase(type)) {
            return "notification-document";
        }

        if ("APPROVAL".equalsIgnoreCase(type)) {
            return "notification-approval";
        }

        if ("SECURITY".equalsIgnoreCase(type)) {
            return "notification-security";
        }

        return "notification-info";
    }

    public void getRecords(ActionEvent actionEvent) {
        String buttonId = actionEvent.getComponent().getId();

        System.out.println("Button ID: " + buttonId);
        if ("allBtn".equals(buttonId)) {
            System.out.println("Orders button clicked");
            loadNotifications(null);
        }
        if ("orderBtn".equals(buttonId)) {
            System.out.println("Orders button clicked");
            loadNotifications("S");
        }
        if ("dispatchBtn".equals(buttonId)) {
            System.out.println("Orders button clicked");
            loadNotifications("D");
        }

        if ("invoiceBtn".equals(buttonId)) {
            System.out.println("Orders button clicked");
            loadNotifications("I");
        }
    }

    public void readAlertMessage(ActionEvent actionEvent) {

        markAsRead(bindNotificationId.getValue());
        
        loadNotifications(null);
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        getBindAlertPop().show(hints);
      
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindMsgStatusPanel);
        AdfFacesContext.getCurrentInstance().addPartialTarget(bindNotificationTable);
    }

    public void markAsRead(Object subject) {

        BindingContext bc = BindingContext.getCurrent();

        DCBindingContainer bindings =
            (DCBindingContainer) bc.getCurrentBindingsEntry();

        DCIteratorBinding iterator =
            bindings.findIteratorBinding("MsgNotificationSynVO1Iterator1");

        ViewObject vo = iterator.getViewObject();

        RowSetIterator rsi = vo.createRowSetIterator(null);

        try {

            while (rsi.hasNext()) {

                Row row = rsi.next();

                Object rowSubject = row.getAttribute("Subject");
                Object rowBody = row.getAttribute("Body");
                
                if (subject != null && subject.equals(rowSubject)) {

                    row.setAttribute("Status", "R");
                    popTitle = (String) rowSubject;
                    popMsg = (String) rowBody;
                    
                    System.out.println(
                        "Updated subject: " + rowSubject
                    );

                    break;
                }
            }

        } finally {
            rsi.closeRowSetIterator();
        }

        vo.getApplicationModule()
          .getTransaction()
          .commit();
    }
    
    

    public void setNotificationList(RichPanelGroupLayout notificationList) {
        this.notificationList = notificationList;
    }

    public RichPanelGroupLayout getNotificationList() {
        return notificationList;
    }


    public void setBindNotificationTable(RichTable bindNotificationTable) {
        this.bindNotificationTable = bindNotificationTable;
    }

    public RichTable getBindNotificationTable() {
        return bindNotificationTable;
    }

    public void setBindAlertPop(RichPopup bindAlertPop) {
        this.bindAlertPop = bindAlertPop;
    }

    public RichPopup getBindAlertPop() {
        return bindAlertPop;
    }

    public void setBindNotificationId(RichOutputText bindNotificationId) {
        this.bindNotificationId = bindNotificationId;
    }

    public RichOutputText getBindNotificationId() {
        return bindNotificationId;
    }

    public void setBindMsgStatusPanel(RichPanelGroupLayout bindMsgStatusPanel) {
        this.bindMsgStatusPanel = bindMsgStatusPanel;
    }

    public RichPanelGroupLayout getBindMsgStatusPanel() {
        return bindMsgStatusPanel;
    }

    public static void setPopMsg(String popMsg) {
        notificationBean.popMsg = popMsg;
    }

    public String getPopMsg() {
        return popMsg;
    }


    // =========================
    // INNER NOTIFICATION CLASS<!-- UNREAD DOT -->
    // =========================

    public static class Notification implements Serializable {

        private static final long serialVersionUID = 1L;

        private Object notificationId;
        private String type;
        private String title;
        private String message;
        private String referenceNo;
        private Object createdDate;
        private boolean read;
        private String status;

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }


        public Object getNotificationId() {
            return notificationId;
        }

        public void setNotificationId(Object notificationId) {
            this.notificationId = notificationId;
        }


        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }


        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }


        public String getReferenceNo() {
            return referenceNo;
        }

        public void setReferenceNo(String referenceNo) {
            this.referenceNo = referenceNo;
        }


        public Object getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(Object createdDate) {
            this.createdDate = createdDate;
        }


        public boolean isRead() {
            return read;
        }

        public void setRead(boolean read) {
            this.read = read;
        }


        public String getTypeClass() {

            if (type == null) {
                return "notification-info";
            }

            if ("I".equalsIgnoreCase(type)) {
                return "notification-payment";
            }

            if ("S".equalsIgnoreCase(type)) {
                return "notification-order";
            }

            if ("D".equalsIgnoreCase(type)) {
                return "notification-document";
            }

            if ("APPROVAL".equalsIgnoreCase(type)) {
                return "notification-approval";
            }

            if ("SECURITY".equalsIgnoreCase(type)) {
                return "notification-security";
            }

            return "notification-info";
        }

        public String getReadUnreadClass() {

            if (status.equalsIgnoreCase("U")) {
                return "notification-unread-dot";
            }
            if (status.equalsIgnoreCase("R")) {
                return "notification-read-dot";
            }


            return "notification-info";
        }
    }
}
