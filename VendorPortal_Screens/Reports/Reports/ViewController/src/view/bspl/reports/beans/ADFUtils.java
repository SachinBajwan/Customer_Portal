package view.bspl.reports.beans;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.InetAddress;
import java.net.UnknownHostException;

import java.sql.Types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


    import javax.el.ELContext;
    import javax.el.ExpressionFactory;
    import javax.el.MethodExpression;
    import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.model.SelectItem;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.adf.model.BindingContext;
import oracle.adf.model.DataControlFrame;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCDataControl;
import oracle.adf.model.binding.DCIteratorBinding;
    import oracle.adf.model.binding.DCParameter;
    import oracle.adf.share.logging.ADFLogger;


import oracle.adf.view.rich.component.rich.RichPopup;

import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.binding.AttributeBinding;
    import oracle.binding.BindingContainer;
    import oracle.binding.ControlBinding;
    import oracle.binding.OperationBinding;
    import oracle.jbo.ApplicationModule;
    import oracle.jbo.Key;
    import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.domain.Timestamp;
import oracle.jbo.uicli.binding.JUCtrlValueBinding;
import org.apache.myfaces.trinidad.model.UploadedFile;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service; 


public class ADFUtils {
        public static final ADFLogger LOGGER = ADFLogger.createADFLogger(ADFUtils.class);
        
        
        public static Object resolveExpression(String expression) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            Application app = facesContext.getApplication();
            ExpressionFactory elFactory = app.getExpressionFactory();
            ELContext elContext = facesContext.getELContext();
            ValueExpression valueExp =
                elFactory.createValueExpression(elContext, expression,
                                                Object.class);
            return valueExp.getValue(elContext);
        }
        
        public static Object resolvElDC(String data) {
                   FacesContext fc = FacesContext.getCurrentInstance();
                   Application app = fc.getApplication();
                   ExpressionFactory elFactory = app.getExpressionFactory();
                   ELContext elContext = fc.getELContext();
                   ValueExpression valueExp = elFactory.createValueExpression(elContext, "#{data." + data + ".dataProvider}", Object.class);
                   return valueExp.getValue(elContext);
               }
        
        public static oracle.jbo.domain.Date getJboToday() {
          return new oracle.jbo.domain.Date(
            oracle.jbo.domain.Date.getCurrentDate());
        }
        
        /**
         * Get application module for an application module data control by name.
         * @param name application module data control name
         * @return ApplicationModule
         */

        public static ApplicationModule getApplicationModuleForDataControl(String name) {
            return (ApplicationModule) resolveExpression("#{data." + name + 
                                                                 ".dataProvider}");
        }
                public static oracle.jbo.domain.Date
                  convertStringToJboDate(String datestring) {
                  java.text.SimpleDateFormat sdf =
                    new java.text.SimpleDateFormat("dd.MM.yyyy");
                  java.text.SimpleDateFormat sdfsql =
                    new java.text.SimpleDateFormat("yyyy-MM-dd");
                  java.util.Date date = null;
                  try {
                    date = sdf.parse(datestring);
                  } catch (Exception exc) {
                    System.out.println("Date error");
                  }
                  return new oracle.jbo.domain.Date(
                    sdfsql.format(date));
                }
        

        /**
         * A convenience method for getting the value of a bound attribute in the
         * current page context programatically.
         * @param attributeName of the bound value in the pageDef
         * @return value of the attribute
         */

        public static Object getBoundAttributeValue(String attributeName) {
            return findControlBinding(attributeName).getInputValue();
        }


       /**
         * A convenience method for setting the value of a bound attribute in the
         * * context of the current page.
         * @param attributeName of the bound value in the pageDef
         * @param value to set
         */

        public static void setBoundAttributeValue(String attributeName, 
                                                  Object value) {
            findControlBinding(attributeName).setInputValue(value);
        }
       
        public static void showPopup(RichPopup popup) {
              FacesContext ctx = FacesContext.getCurrentInstance();
              String popupId = popup.getClientId(ctx);
              ExtendedRenderKitService erkService =
                  Service.getService(ctx.getRenderKit(),ExtendedRenderKitService.class);
             erkService.addScript(ctx,"var hints = {autodismissNever:true}; " + "AdfPage.PAGE.findComponent('" + popupId +  "').show(hints);");
            }

        /**
         * Returns the evaluated value of a pageDef parameter.
         * @param pageDefName reference to the page definition file of the page with the parameter
         * @param parameterName name of the pagedef parameter
         * @return evaluated value of the parameter as a String
         */

        public static Object getPageDefParameterValue(String pageDefName, 
                                                      String parameterName) {
            BindingContainer bindings = findBindingContainer(pageDefName);
            DCParameter param = 
                ((DCBindingContainer)bindings).findParameter(parameterName);
            return param.getValue();
        }

        /**
         * Convenience method to find a DCControlBinding as an AttributeBinding
         * to get able to then call getInputValue() or setInputValue() on it.
         * @param bindingContainer binding container
         * @param attributeName name of the attribute binding.
         * @return the control value binding with the name passed in.
         *
         */

        public static AttributeBinding findControlBinding(BindingContainer bindingContainer, 
                                                          String attributeName) {
            if (attributeName != null) {
                if (bindingContainer != null) {
                    ControlBinding ctrlBinding = 
                        bindingContainer.getControlBinding(attributeName);
                    if (ctrlBinding instanceof AttributeBinding) {
                        return (AttributeBinding)ctrlBinding;
                    }
                }
            }
            return null;
        }



        /**
         * Convenience method to find a DCControlBinding as a JUCtrlValueBinding
         * to get able to then call getInputValue() or setInputValue() on it.
         * @param attributeName name of the attribute binding.
         * @return the control value binding with the name passed in.
         *
         */

        public static AttributeBinding findControlBinding(String attributeName) {
            return findControlBinding(getBindingContainer(), attributeName);
        }



        /**
         * Return the current page's binding container.
         * @return the current page's binding container
         */

        public static BindingContainer getBindingContainer() {
            return (BindingContainer) resolveExpression("#{bindings}");
        }

        /**
         * Return the Binding Container as a DCBindingContainer.
         * @return current binding container as a DCBindingContainer
         */
        public static DCBindingContainer getDCBindingContainer() {
            return (DCBindingContainer)getBindingContainer();
        }

        /**
         * Get List of ADF Faces SelectItem for an iterator binding.
         * 
         * Uses the value of the 'valueAttrName' attribute as the key for
         * the SelectItem key.
         * 
         * @param iteratorName ADF iterator binding name
         * @param valueAttrName name of the value attribute to use
         * @param displayAttrName name of the attribute from iterator rows to display
         * @return ADF Faces SelectItem for an iterator binding
         */
        public static List<SelectItem> selectItemsForIterator(String iteratorName, 
                                                              String valueAttrName, 
                                                              String displayAttrName) {
            return selectItemsForIterator(findIterator(iteratorName), 
                                          valueAttrName, displayAttrName);
        }

        /**
         * Get List of ADF Faces SelectItem for an iterator binding with description.
         * 
         * Uses the value of the 'valueAttrName' attribute as the key for
         * the SelectItem key.
         * 
         * @param iteratorName ADF iterator binding name
         * @param valueAttrName name of the value attribute to use
         * @param displayAttrName name of the attribute from iterator rows to display
         * @param descriptionAttrName name of the attribute to use for description
         * @return ADF Faces SelectItem for an iterator binding with description
         */
        public static List<SelectItem> selectItemsForIterator(String iteratorName, 
                                                              String valueAttrName, 
                                                              String displayAttrName, 
                                                              String descriptionAttrName) {
            return selectItemsForIterator(findIterator(iteratorName), 
                                          valueAttrName, displayAttrName, 
                                          descriptionAttrName);
        }

        /**
         * Get List of attribute values for an iterator.
        * @param iteratorName ADF iterator binding name
         * @param valueAttrName value attribute to use
         * @return List of attribute values for an iterator
         */
        public static List attributeListForIterator(String iteratorName, 
                                                    String valueAttrName) {
            return attributeListForIterator(findIterator(iteratorName), 
                                            valueAttrName);
        }

       /**
         * Get List of Key objects for rows in an iterator.
         * @param iteratorName iterabot binding name
         * @return List of Key objects for rows
         */
        public static List<Key> keyListForIterator(String iteratorName) {
            return keyListForIterator(findIterator(iteratorName));
        }

        /**
         * Get List of Key objects for rows in an iterator.
         * @param iter iterator binding
         * @return List of Key objects for rows
         */
        public static List<Key> keyListForIterator(DCIteratorBinding iter) {
            List<Key> attributeList = new ArrayList<Key>();
            for (Row r : iter.getAllRowsInRange()) {
                attributeList.add(r.getKey());
            }
            return attributeList;
        }


        /**
         * Get List of Key objects for rows in an iterator using key attribute.
         * @param iteratorName iterator binding name
         * @param keyAttrName name of key attribute to use
         * @return List of Key objects for rows
         */
        public static List<Key> keyAttrListForIterator(String iteratorName, 
                                                       String keyAttrName) {
            return keyAttrListForIterator(findIterator(iteratorName), keyAttrName);
        }

        /**
         * Get List of Key objects for rows in an iterator using key attribute.
         * 
         * @param iter iterator binding
         * @param keyAttrName name of key attribute to use
         * @return List of Key objects for rows
         */
        public static List<Key> keyAttrListForIterator(DCIteratorBinding iter, 
                                                       String keyAttrName) {
            List<Key> attributeList = new ArrayList<Key>();
            for (Row r : iter.getAllRowsInRange()) {
                attributeList.add(new Key(new Object[] { r.getAttribute(keyAttrName) }));
            }
            return attributeList;
        }


        /**
         * Get a List of attribute values for an iterator.
         * 
         * @param iter iterator binding
         * @param valueAttrName name of value attribute to use
         * @return List of attribute values
         */

        public static List attributeListForIterator(DCIteratorBinding iter, 
                                                    String valueAttrName) {
            List attributeList = new ArrayList();
            for (Row r : iter.getAllRowsInRange()) {
                attributeList.add(r.getAttribute(valueAttrName));
            }
            return attributeList;
        }


        /**
         * Find an iterator binding in the current binding container by name.
         * 
         * @param name iterator binding name
         * @return iterator binding
         */
        public static DCIteratorBinding findIterator(String name) {
            DCIteratorBinding iter = 
                getDCBindingContainer().findIteratorBinding(name);
            if (iter == null) {
                throw new RuntimeException("Iterator '" + name + "' not found");
            }
            return iter;
        }
        

        public static DCIteratorBinding findIterator(String bindingContainer, String iterator) {
            DCBindingContainer bindings = 
                (DCBindingContainer) resolveExpression("#{" + bindingContainer + "}");
            if (bindings == null) {
                throw new RuntimeException("Binding container '" + 
                                           bindingContainer + "' not found");
            }
            DCIteratorBinding iter = bindings.findIteratorBinding(iterator);
            if (iter == null) {
                throw new RuntimeException("Iterator '" + iterator + "' not found");
            }
            return iter;
        }


        public static JUCtrlValueBinding findCtrlBinding(String name){
            JUCtrlValueBinding rowBinding = 
                (JUCtrlValueBinding)getDCBindingContainer().findCtrlBinding(name);    
            if (rowBinding == null) {
                throw new RuntimeException("CtrlBinding " + name + "' not found");
            }
            return rowBinding;
        }


        /**
         * Find an operation binding in the current binding container by name.
         * 
         * @param name operation binding name
         * @return operation binding
         */
        public static OperationBinding findOperation(String name) {
            OperationBinding op = 
                getDCBindingContainer().getOperationBinding(name);
            if (op == null) {
                throw new RuntimeException("Operation '" + name + "' not found");
            }
            return op;
        }


        /**
         * Find an operation binding in the current binding container by name.
         * 
         * @param bindingContianer binding container name
         * @param opName operation binding name
         * @return operation binding
         */
        public static OperationBinding findOperation(String bindingContianer, 
                                                     String opName) {
            DCBindingContainer bindings = 
                (DCBindingContainer) resolveExpression("#{" + bindingContianer + "}");
            if (bindings == null) {
                throw new RuntimeException("Binding container '" + 
                                           bindingContianer + "' not found");
            }
            OperationBinding op = 
                bindings.getOperationBinding(opName);
            if (op == null) {
                throw new RuntimeException("Operation '" + opName + "' not found");
            }
            return op;
        }


        /**
         * Get List of ADF Faces SelectItem for an iterator binding with description.
         * 
         * Uses the value of the 'valueAttrName' attribute as the key for
         * the SelectItem key.
        * 
         * @param iter ADF iterator binding
         * @param valueAttrName name of value attribute to use for key
         * @param displayAttrName name of the attribute from iterator rows to display
         * @param descriptionAttrName name of the attribute for description
         * @return ADF Faces SelectItem for an iterator binding with description
         */
        public static List<SelectItem> selectItemsForIterator(DCIteratorBinding iter, 
                                                              String valueAttrName, 
                                                              String displayAttrName, 
                                                              String descriptionAttrName) {
            List<SelectItem> selectItems = new ArrayList<SelectItem>();
            for (Row r : iter.getAllRowsInRange()) {
                selectItems.add(new SelectItem(r.getAttribute(valueAttrName), 
                                               (String)r.getAttribute(displayAttrName), 
                                               (String)r.getAttribute(descriptionAttrName)));
            }
            return selectItems;
        }



        /**
         * Get List of ADF Faces SelectItem for an iterator binding.
         * 
         * Uses the value of the 'valueAttrName' attribute as the key for
         * the SelectItem key.
         * 
         * @param iter ADF iterator binding
         * @param valueAttrName name of value attribute to use for key
         * @param displayAttrName name of the attribute from iterator rows to display
         * @return ADF Faces SelectItem for an iterator binding
         */
        public static List<SelectItem> selectItemsForIterator(DCIteratorBinding iter, 
                                                              String valueAttrName, 
                                                              String displayAttrName) {
            List<SelectItem> selectItems = new ArrayList<SelectItem>();
            for (Row r : iter.getAllRowsInRange()) {
                selectItems.add(new SelectItem(r.getAttribute(valueAttrName), 
                                               (String)r.getAttribute(displayAttrName)));
            }
            return selectItems;
        }



        /**
         * Get List of ADF Faces SelectItem for an iterator binding.
         * 
         * Uses the rowKey of each row as the SelectItem key.
         * 
         * @param iteratorName ADF iterator binding name
         * @param displayAttrName name of the attribute from iterator rows to display
         * @return ADF Faces SelectItem for an iterator binding
         */
        public static List<SelectItem> selectItemsByKeyForIterator(String iteratorName, 
                                                                   String displayAttrName) {
            return selectItemsByKeyForIterator(findIterator(iteratorName), 
                                               displayAttrName);
        }



        /**
         * Get List of ADF Faces SelectItem for an iterator binding with discription.
         * 
         * Uses the rowKey of each row as the SelectItem key.
         * 
         * @param iteratorName ADF iterator binding name
         * @param displayAttrName name of the attribute from iterator rows to display
         * @param descriptionAttrName name of the attribute for description
         * @return ADF Faces SelectItem for an iterator binding with discription
         */

        public static List<SelectItem> selectItemsByKeyForIterator(String iteratorName, 
                                                                   String displayAttrName, 
                                                                   String descriptionAttrName) {

            return selectItemsByKeyForIterator(findIterator(iteratorName), 
                                               displayAttrName, 
                                               descriptionAttrName);
        }



        /**
         * Get List of ADF Faces SelectItem for an iterator binding with discription.
         * 
         * Uses the rowKey of each row as the SelectItem key.
         * 
         * @param iter ADF iterator binding
         * @param displayAttrName name of the attribute from iterator rows to display
         * @param descriptionAttrName name of the attribute for description
         * @return ADF Faces SelectItem for an iterator binding with discription
         */
        public static List<SelectItem> selectItemsByKeyForIterator(DCIteratorBinding iter, 
                                                                   String displayAttrName, 
                                                                   String descriptionAttrName) {
            List<SelectItem> selectItems = new ArrayList<SelectItem>();
            for (Row r : iter.getAllRowsInRange()) {
                selectItems.add(new SelectItem(r.getKey(), 
                                               (String)r.getAttribute(displayAttrName), 
                                               (String)r.getAttribute(descriptionAttrName)));
            }
            return selectItems;
        }



        /**
         * Get List of ADF Faces SelectItem for an iterator binding.
         * 
         * Uses the rowKey of each row as the SelectItem key.
         * 
         * @param iter ADF iterator binding
         * @param displayAttrName name of the attribute from iterator rows to display
         * @return List of ADF Faces SelectItem for an iterator binding
         */

        public static List<SelectItem> selectItemsByKeyForIterator(DCIteratorBinding iter, 
                                                                   String displayAttrName) {
            List<SelectItem> selectItems = new ArrayList<SelectItem>();
            for (Row r : iter.getAllRowsInRange()) {
                selectItems.add(new SelectItem(r.getKey(), 
                                               (String)r.getAttribute(displayAttrName)));
            }
            return selectItems;
        }



        /**
         * Find the BindingContainer for a page definition by name.
         *
         * Typically used to refer eagerly to page definition parameters. It is
         * not best practice to reference or set bindings in binding containers
         * that are not the one for the current page.
         *
         * @param pageDefName name of the page defintion XML file to use
         * @return BindingContainer ref for the named definition
         */

        private static BindingContainer findBindingContainer(String pageDefName) {
            BindingContext bctx = getDCBindingContainer().getBindingContext();
            BindingContainer foundContainer = 
                bctx.findBindingContainer(pageDefName);
            return foundContainer;
        }



        public static void printOperationBindingExceptions(List opList){
            if(opList != null && !opList.isEmpty()){
                for(Object error:opList){
                    LOGGER.severe( error.toString() );
                }
            }
        }
        
        /**
        * Programmatic evaluation of EL.
        *
        * @param el EL to evaluate
        * @return Result of the evaluation
        */
        public static Object evaluateEL(String el) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory =
        facesContext.getApplication().getExpressionFactory();
        ValueExpression exp =
        expressionFactory.createValueExpression(elContext, el,
        Object.class);

        return exp.getValue(elContext);
        }

        /**
        * Programmatic invocation of a method that an EL evaluates to.
        * The method must not take any parameters.
        *
        * @param el EL of the method to invoke
        * @return Object that the method returns
        */
        public static Object invokeEL(String el) {
        return invokeEL(el, new Class[0], new Object[0]);
        }

        /**
        * Programmatic invocation of a method that an EL evaluates to.
        *
        * @param el EL of the method to invoke
        * @param paramTypes Array of Class defining the types of the parameters
        * @param params Array of Object defining the values of the parametrs
        * @return Object that the method returns
        */
        public static Object invokeEL(String el, Class[] paramTypes,
        Object[] params) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory =
        facesContext.getApplication().getExpressionFactory();
        MethodExpression exp =
        expressionFactory.createMethodExpression(elContext, el,
        Object.class, paramTypes);

        return exp.invoke(elContext, params);
        }

        /**
        * Sets a value into an EL object. Provides similar functionality to
        * the <af:setActionListener> tag, except the from is
        * not an EL. You can get similar behavior by using the following...

        * setEL(to, evaluateEL(from))
        *
        * @param el EL object to assign a value
        * @param val Value to assign
        */
        public static void setEL(String el, Object val) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory =
        facesContext.getApplication().getExpressionFactory();
        ValueExpression exp =
        expressionFactory.createValueExpression(elContext, el,
        Object.class);

        exp.setValue(elContext, val);
        }
        
        public static void showMessage(String msg, int severity) {
              FacesMessage fm = new FacesMessage(msg);
              /**
               * set the type of the message.
               * Valid types: error, fatal,info,warning
               */
              if(severity == 0)
                  fm.setSeverity(FacesMessage.SEVERITY_ERROR);
              if(severity == 1)
                  fm.setSeverity(FacesMessage.SEVERITY_WARN);
              if(severity == 2)
                  fm.setSeverity(FacesMessage.SEVERITY_INFO);
             
              FacesContext context = FacesContext.getCurrentInstance();
              context.addMessage(null, fm);
          }
        
        public static DCIteratorBinding getBindingIterator(String iteratorName) {
              FacesContext facesContext = FacesContext.getCurrentInstance();
              ExpressionFactory expFactory = facesContext.getApplication().getExpressionFactory();
                   //Create a handle to the Binding Container to access the iterators.
              DCBindingContainer bc;
              bc = (DCBindingContainer)expFactory.createValueExpression(facesContext.getELContext(),"#{bindings}",DCBindingContainer.class).getValue(facesContext.getELContext());
                   //Creating the instance to the corresponding Iterators
              DCIteratorBinding productIter = bc.findIteratorBinding(iteratorName);
              return productIter;
            }
        
        
        public static String uploadFile(UploadedFile file)
        {
                           UploadedFile myfile = file;
                           String path = null;
                           if (myfile == null) {
                           } 
                  else 
                   {
                                 OperationBinding binding=   ADFUtils.findOperation("uploadFilePath");          
                               Object ob=binding.execute();
                               // All uploaded files will be stored in below path
                               path = binding.getResult().toString() + myfile.getFilename();
                               InputStream inputStream = null;
                               try {
                                   FileOutputStream out = new FileOutputStream(path);
                                   inputStream = myfile.getInputStream();
                                   byte[] buffer = new byte[12288];
                                   int bytesRead = 0;
                                   while ((bytesRead = inputStream.read(buffer, 0, 12288)) != -1) {
                                       out.write(buffer, 0, bytesRead);
                                   }
                                   out.flush();
                                   out.close();
                               } catch (Exception ex) {
                                   // handle exception
                                   ex.printStackTrace();
                               } finally {
                                   try {
                                       inputStream.close();
                                   } catch (Exception e) {
                                   }
                               }

                           }
                                                                                                  //Returns the path where file is stored
                           return path;
            } 
        
        public static void setColumnValue(String itr,String colname,String val){
        //            DCIteratorBinding dci = ADFUtils.findIterator("ContractOrderDetailVO1Iterator");
                    System.out.println("in adfutil iterator:"+itr);
                    System.out.println("in adfutil colname:"+colname);
                    System.out.println("in adfutil val:"+val);
                    DCIteratorBinding dci = findIterator(itr);
                    RowSetIterator rsi=dci.getViewObject().createRowSetIterator(null);
                        while(rsi.hasNext()) {
                            Row r = rsi.next();
                            r.setAttribute(colname, val);
                        }
                        rsi.closeRowSetIterator();
                }

        public static void setLastUpdatedBy(String itr,String colname){
                            System.out.println("in adfutil iterator:"+itr);
                            System.out.println("in adfutil colname:"+colname);
                            String empcd=(String)ADFUtils.resolveExpression("#{pageFlowScope.empCode}");
                            empcd=empcd!=null?empcd:"Admin";
                            Timestamp t=new Timestamp(System.currentTimeMillis());
                            System.out.println("empcode:"+empcd+" timestamp:"+t);
                            DCIteratorBinding dci = findIterator(itr);
                            if(colname.equalsIgnoreCase("LastUpdatedBy")){
                            dci.getCurrentRow().setAttribute("LastUpdatedBy",empcd);
                            dci.getCurrentRow().setAttribute("LastUpdateDate",t);
                            }else if(colname.equalsIgnoreCase("ModifiedBy")){
                                dci.getCurrentRow().setAttribute("ModifiedBy",empcd);
                                dci.getCurrentRow().setAttribute("ModifiedDate",t);
                            }
                        }
        public static String getValueFrmExpression(String data) {
                    FacesContext fc = FacesContext.getCurrentInstance();
                    Application app = fc.getApplication();
                    ExpressionFactory elFactory = app.getExpressionFactory();
                    ELContext elContext = fc.getELContext();
                    ValueExpression valueExp = elFactory.createValueExpression(elContext, data, Object.class);
                    String Message = null;
                    Object obj = valueExp.getValue(elContext);
                    if (obj != null) {
                        Message = obj.toString();
                    }
                    return Message;
                }
        
    
        
        public static String getNepaliMonthString(int month) {
            switch (month) {
            case 1:
                return "Baishakh";
            case 2:
                return "Jestha";
            case 3:
                return "Asar";
            case 4:
                return "Shrawan";
            case 5:
                return "Bhadau";
            case 6:
                return "Aswin";
            case 7:
                return "Kartik";
            case 8:
                return "Mansir";
            case 9:
                return "Poush";
            case 10:
                return "Magh";
            case 11:
                return "Falgun";
            case 12:
                return "Chaitra";
            }
            return null;
        }
        
        public static oracle.jbo.domain.Date getTodayDate() {
            return new oracle.jbo.domain.Date(oracle.jbo.domain.Date.getCurrentDate());
        }
        
        public static Object evaluteEL(String el) {

             FacesContext fctx = FacesContext.getCurrentInstance();
             ELContext elContext = fctx.getELContext();
             Application app = fctx.getApplication();
             ExpressionFactory expFactory = app.getExpressionFactory();
             ValueExpression valExp =
                 expFactory.createValueExpression(elContext, el, Object.class);
             return valExp.getValue(elContext);
         }
        
        
        public static FacesMessage showErrorMessage(String header, String text) {
            return new FacesMessage(FacesMessage.SEVERITY_ERROR, header, text);
        }

        public static FacesMessage showInfoMessage(String header, String text) {
            return new FacesMessage(FacesMessage.SEVERITY_INFO, header, text);
        }


        public static FacesContext getFacesContext() {
            return FacesContext.getCurrentInstance();
        }

        public static ExternalContext getExternalContext() {
            return getFacesContext().getExternalContext();
        }

        public static HttpServletRequest getRequest() {
            return (HttpServletRequest) getExternalContext().getRequest();
        }

        public static HttpServletResponse getResponse() {
            return (HttpServletResponse) getExternalContext().getResponse();
        }

        public static HttpSession getSession() {
            return getRequest().getSession();
        }

        public static void setValueToSession(String key, Object value) {
            getRequest().getSession().setAttribute(key, value);
        }

        public static Object getValueFromSession(String key) {
            Object obj = getRequest().getSession().getAttribute(key);
            if (obj == null || obj.toString().trim().length() == 0) {
                return null;
            } else {
                return obj;
            }
        }

        public static void sendForward(HttpServletRequest request, HttpServletResponse response, String forwardUrl) {

            FacesContext ctx = FacesContext.getCurrentInstance();

            RequestDispatcher dispatcher = request.getRequestDispatcher(forwardUrl);
            try {
                dispatcher.forward(request, response);
            } catch (ServletException se) {
                se.printStackTrace();
                showErrorMessage("Servlet Exception", se.getMessage());
            } catch (IOException ie) {
                ie.printStackTrace();
                showErrorMessage("IO Exception", ie.getMessage());
            }
            ctx.responseComplete();
        }

        public static void sendForward(String forwardUrl) {
            sendForward(getRequest(), getResponse(), forwardUrl);
        }

        public static void refreshCompoenent(UIComponent compoenent) {
            AdfFacesContext.getCurrentInstance().addPartialTarget(compoenent);
        }

        public static MethodBinding getMethodBinding(String exp, Class[] params) {
            FacesContext fc = FacesContext.getCurrentInstance();
            Application app = fc.getApplication();
            MethodBinding mb = app.createMethodBinding(exp, params);
            return mb;
        }

        public static Object getBean(String bean) {
            //System.out.println("Get bean Called for : "+bean);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            Application application = facesContext.getApplication();
            return facesContext.getApplication().getVariableResolver().resolveVariable(facesContext, bean);
        }


        public static String reloadThePage() {
            rollbackPartialTransactions();
            FacesContext fContext = FacesContext.getCurrentInstance();
            String viewId = fContext.getViewRoot().getViewId();
            String actionUrl = fContext.getApplication().getViewHandler().getActionURL(fContext, viewId);
            try {
                ExternalContext eContext = fContext.getExternalContext();
                String resourceUrl = actionUrl; //eContext.encodeResourceURL(actionUrl);
                // Use the action URL directly since the encoding a resource URL will NPE in isEmailablePage()
                eContext.redirect(resourceUrl);

            } catch (IOException ioe) {

                ioe.printStackTrace();
            }
            return null;
        }


        public static String rollbackPartialTransactions() {
            BindingContext bc = BindingContext.getCurrent();
            DataControlFrame dcf = bc.findDataControlFrame(bc.getCurrentDataControlFrame());
            Collection<DCDataControl> dcCol = dcf.datacontrols();
            //System.out.println("dcCol.size() = " + dcCol.size());
            for (DCDataControl dc : dcCol) {
                // System.out.println("dc = " + dc);
                if (dc.isTransactionDirty()) {
                    //  System.out.println("dc.isTransactionDirty() = " + dc.isTransactionDirty());
                    try {
                        dc.rollbackTransaction();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
        /**
         * Method to create expression for taskflow calling on the basis of taskflow name
         * @param tfName
         * @return
         */
        public static String getTaskFlowId(String tfName) 
        {
            return "/WEB-INF/" + tfName + ".xml#" + tfName;
        }


        /**
         *This funtion will get Defalut getUserId
         * @param am
         * @return
         */
        public static String findUserId(String userName) 
        {
            Object obj = null;
            String usrId = null;
        //        try
        //        {
        //            obj = DBConfiguration.callFunction(Types.VARCHAR, "UX_FIND_USER_ID(?)", new Object[] { userName });
        //
        //            System.out.println(" result of UX_FIND_USER:" + obj);
        //            if (obj != null) {
        //                System.out.println("User Id is----->>" + obj.toString());
        //                usrId = obj.toString();
        //                return usrId;
        //            }
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }
            return usrId;
        }

            
      
      
        
        /**
           * Locate an UIComponent in view root with its component id. Use a recursive way to achieve this.
           * Taken from http://www.jroller.com/page/mert?entry=how_to_find_a_uicomponent
           * @param id UIComponent id
           * @return UIComponent object
           */

        public static UIComponent findComponentInRoot(String id)
        {
          UIComponent component = null;
          FacesContext facesContext = FacesContext.getCurrentInstance();
          if (facesContext != null) 
          {
              UIComponent root = facesContext.getViewRoot();
              component = findComponent(root, id);
          }
          return component;
        }
        
        /**
            * Locate an UIComponent from its root component.
            * Taken from http://www.jroller.com/page/mert?entry=how_to_find_a_uicomponent
            * @param base root Component (parent)
            * @param id UIComponent id
            * @return UIComponent object
            */

        public static UIComponent findComponent(UIComponent base, String id) 
        {
           if (id.equals(base.getId()))
               return base;
           UIComponent children = null;
           UIComponent result = null;
           Iterator<UIComponent> childrens = base.getFacetsAndChildren();

           while (childrens.hasNext() && (result == null)) {
               children = (UIComponent)childrens.next();
               if (id.equals(children.getId())) {
                   result = children;
                   break;
               }
               result = findComponent(children, id);

               if (result != null) {
                   break;
               }
           }
           return result;
        }

        
        public static void setValueToPageFlowScope(String key, Object value){
            AdfFacesContext.getCurrentInstance().getPageFlowScope().put(key, value);    
        }
        
        public static Object getValueFromPageFlowScope(String key) {
            Object obj = AdfFacesContext.getCurrentInstance().getPageFlowScope().get(key);
            if (obj == null || obj.toString().trim().length() == 0) {
                return null;
            } else {
                return obj;
            }
        }
        
        public static String gblLocalServer() 
        {
            Object elExp = evaluateEL("#{pageFlowScope.G_LOCAL_SERVER}");
            if (elExp != null) 
            {
                System.out.println("------G_LOCAL_SERVER()---->>Returned-->>:"+elExp);
//                if (LogUtil.enableLogger) 
//                {
////                    _logger.fine("------G_LOCAL_SERVER()---->>Returned-->>:"+elExp);
//                }
                return elExp.toString();
            } 
            else 
            {
//                _logger.severe("Warning : Method | gblLocalServer() | gblLocalServer not defined.");
//                if (LogUtil.enableLogger) {
////                    _logger.info("Info------G_LOCAL_SERVER()---->>Returned Null-->>:"+elExp);
//                }
                return null;
            }
        }
        
        public static String getClientIpAddress() {
            try {
                String clientIpAddress =getRequest().getRemoteAddr();
                return clientIpAddress;
            } catch (Exception e) {
                // TODO: Add catch code
                e.printStackTrace();
            }
            return null;
        }

        public static String getClientCompName() {
            try {
                
                InetAddress i = InetAddress.getByName(getRequest().getRemoteAddr());
                //InetAddress i = InetAddress.getLocalHost();
                String computerName = i.getHostName();                
                return computerName;
            } catch (UnknownHostException uhe) {
                // TODO: Add catch code
                uhe.printStackTrace();
            }
            return null;
        }
        
        public static String getSessionId() {
            try {
                String sessionId =getRequest().getSession(false).getId();
                return sessionId;
            } catch (Exception e) {
                // TODO: Add catch code
                e.printStackTrace();
            }
            return null;
        }

    }