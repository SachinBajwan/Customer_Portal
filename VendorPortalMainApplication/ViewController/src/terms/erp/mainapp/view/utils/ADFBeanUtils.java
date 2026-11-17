package terms.erp.mainapp.view.utils;

import VendorProfile.view.bean.ADFUtils;

import adf.utils.bean.Lookup;
import adf.utils.com.GlobalParams;
import adf.utils.log.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.sql.DataSource;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.model.binding.DCParameter;
import oracle.adf.share.ADFContext;
import oracle.adf.share.logging.ADFLogger;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputListOfValues;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.binding.AttributeBinding;
import oracle.binding.BindingContainer;
import oracle.binding.ControlBinding;
import oracle.binding.OperationBinding;

import oracle.jbo.ApplicationModule;
import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.domain.Number;
import oracle.jbo.domain.Timestamp;
import oracle.jbo.server.JboPrecisionScaleValidator;
import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import org.apache.myfaces.trinidad.model.UploadedFile;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;

public class ADFBeanUtils {
    
    private static Number ONE_HUNDRED = new Number(100);
    private static Number ZERO = new Number(0);
    private static int _PRECISION = 26;
    private static int _SCALE = 6;

    private static ADFLogger logger = ADFLogger.createADFLogger(adf.utils.bean.ADFBeanUtils.class);


    public ADFBeanUtils() {
        super();
    }

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
    
    
    /**
     * Get application module for an application module data control by name.
     * @param name application module data control name
     * @return ApplicationModule
     */

    public static ApplicationModule getApplicationModuleForDataControl(String name) {
        return (ApplicationModule) resolveExpression("#{data." + name + 
                                                             ".dataProvider}");
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
     * Method to validate precision of a Number field.
     * @param precision ex: 26
     * @param scale ex: 6
     * @param total value that need to be checked
     * @return true when valid false when precision is invalid
     */
    public static Boolean isPrecisionValid(Integer precision, Integer scale, Number total) {
        JboPrecisionScaleValidator vc = new JboPrecisionScaleValidator();
        vc.setPrecision(precision);
        vc.setScale(scale);
        return vc.validateValue(total);
    }

    /**
     * Method to validate default precision of a Number field.
     * @param total value that need to be checked
     * @return true when valid false when precision is invalid
     */
    public static Boolean isPrecisionValid(Number total) {
        JboPrecisionScaleValidator vc = new JboPrecisionScaleValidator();
        vc.setPrecision(_PRECISION);
        vc.setScale(_SCALE);
        return vc.validateValue(total);
    }

    /**
     * Method to get rounded value for any Number passed.
     * @param object Number to be rounded
     * @return Number after rounding it, to GLBL_AMT_DIGIT
     */
    public static Number roundOff(Number object) {
        return (Number) object.round(GlobalParams.AmountDigit());
    }

    /**
     * Method to get rounded value for any Number passed.
     * @param digit
     * @param object Number to be rounded
     * @return Number after rounding it, to passed digit
     */
    public static Number roundOff(int digit, Number object) {
        return (Number) object.round(digit);
    }


    /**
     * Method to check if a Number is negative or not.
     * @param object
     * @return Boolean true if Number is negative and false if Number is not negative
     */
    public static Boolean isNumberNegative(Number object) {
        Boolean b = false;
        if (object != null) {
            int i = object.compareTo(ZERO);
            if (i == -1) {
                b = true;
            }
        }
        return b;
    }

    /**
     * Method to get Operation Binding Directly.
     * @param methodBinding name of the method that need to be called.
     * @return OperationBinding
     */
    public static OperationBinding getOperationBinding(String methodBinding) 
    {
        OperationBinding obj = null;
        try {
            obj = BindingContext.getCurrent().getCurrentBindingsEntry().getOperationBinding(methodBinding);
        } catch (Exception e) 
        {
            showGlobelFatal("There have been some error while fetching Operation Binding !",
                                                    methodBinding + " not found." + e.getMessage(),
                                                    FacesMessage.SEVERITY_FATAL);
            e.printStackTrace();
        }
        return obj;
    }


    /**
     * Method to validate Precision and Negative value of a Number.
     * @param object Number to be validated
     * @return int 0 - valid ; -1 : Invalid Precision ; -2 : Number is Negative
     */
    public static int isAmountValid(Number object) {
        int i = 0;
        if (isNumberNegative(object)) {
            i = -1;
        } else if (!isPrecisionValid(_PRECISION, _SCALE, object)) {
            i = -2;
        }
        return i;
    }

    /**
     * Method to validate percentage value of a Number.
     * @param object Number to be validated
     * @return int 0 - valid ; -1 : Invalid Percentage ;
     */
    public static int isPercentValueValid(Number object) {
        int i = -1;
        if (ONE_HUNDRED.compareTo(object) >= 0) {
            i = 0;
        }
        return i;
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
    public static List<SelectItem> selectItemsForIterator(DCIteratorBinding iter, String valueAttrName,
                                                          String displayAttrName, String descriptionAttrName) {
        List<SelectItem> selectItems = new ArrayList<SelectItem>();
        for (Row r : iter.getAllRowsInRange()) {
            selectItems.add(new SelectItem(r.getAttribute(valueAttrName), (String) r.getAttribute(displayAttrName),
                                           (String) r.getAttribute(descriptionAttrName)));
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
    public static List<SelectItem> selectItemsForIterator(DCIteratorBinding iter, String valueAttrName,
                                                          String displayAttrName) {
        List<SelectItem> selectItems = new ArrayList<SelectItem>();
        for (Row r : iter.getAllRowsInRange()) {
            selectItems.add(new SelectItem(r.getAttribute(valueAttrName), (String) r.getAttribute(displayAttrName)));
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
    public static List<SelectItem> selectItemsByKeyForIterator(String iteratorName, String displayAttrName) {
        return selectItemsByKeyForIterator(findIterator(iteratorName), displayAttrName);
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
    public static List<SelectItem> selectItemsByKeyForIterator(String iteratorName, String displayAttrName,
                                                               String descriptionAttrName) {
        return selectItemsByKeyForIterator(findIterator(iteratorName), displayAttrName, descriptionAttrName);
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
    public static List<SelectItem> selectItemsByKeyForIterator(DCIteratorBinding iter, String displayAttrName,
                                                               String descriptionAttrName) {
        List<SelectItem> selectItems = new ArrayList<SelectItem>();
        for (Row r : iter.getAllRowsInRange()) {
            selectItems.add(new SelectItem(r.getKey(), (String) r.getAttribute(displayAttrName),
                                           (String) r.getAttribute(descriptionAttrName)));
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
    public static List<SelectItem> selectItemsByKeyForIterator(DCIteratorBinding iter, String displayAttrName) {
        List<SelectItem> selectItems = new ArrayList<SelectItem>();
        for (Row r : iter.getAllRowsInRange()) {
            selectItems.add(new SelectItem(r.getKey(), (String) r.getAttribute(displayAttrName)));
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
        BindingContainer foundContainer = bctx.findBindingContainer(pageDefName);
        return foundContainer;
    }


    /**
     * Function to open popup, this function takes value of popup name and generates a java script to open popup.
     * @param pop
     * @param visible
     */
    public static void openPopup(RichPopup pop, boolean visible) {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            if (context != null && pop != null) {
                String popupId = pop.getClientId(context);
                if (popupId != null) {
                    StringBuilder script = new StringBuilder();
                    script.append("var popup = AdfPage.PAGE.findComponent('").append(popupId).append("'); ");
                    if (visible) {
                        script.append("if (!popup.isPopupVisible()) { ").append("popup.show();}");
                    } else {
                        script.append("if (popup.isPopupVisible()) { ").append("popup.hide();}");
                    }
                    ExtendedRenderKitService erks =
                        Service.getService(context.getRenderKit(), ExtendedRenderKitService.class);
                    erks.addScript(context, script.toString());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Methods to call method defined in bindings.
     * @param bindingName to call method of this name from model bindings.
     * *
     * @param paramVal
     * @param paramNm
     * @return
     */

    public static Object callBindingsMethod(String bindingName, Object[] paramVal, Object[] paramNm) {
        OperationBinding op = findOperation(bindingName);
        if (paramVal != null && paramNm != null) {
            for (int z = 0; z < paramVal.length; z++) {
                op.getParamsMap().put(paramNm[z], paramVal[z]);
            }
        }
        op.execute();
        return op.getResult();
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
     * @param el EL of the method to invoke
     * @param paramTypes Array of Class defining the types of the parameters
     * @param params Array of Object defining the values of the parametrs
     * @return Object that the method returns
     */
    public static Object invokeEL(String el, Class[] paramTypes, Object[] params) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
        MethodExpression exp = expressionFactory.createMethodExpression(elContext, el, Object.class, paramTypes);
        return exp.invoke(elContext, params);
    }

    public static Object elExpressionResolve(String expression) 
    {
        Object o = null;
        if (expression != null) 
        {
            ADFContext adfCtx = ADFContext.getCurrent();
            ELContext elContext = adfCtx.getELContext();
            ValueExpression valueExp =
                adfCtx.getExpressionFactory().createValueExpression(elContext, expression, Object.class);
            o = valueExp.getValue(elContext);
        } else {
            logger.severe("Warning : Method | resolvEl(StringBuilder expression) | called with parameter as null.");
        }
        if (LogUtil.enableLogger) {
            logger.fine("Method | resolvEl(StringBuilder expression) | called with following parameters ");
            logger.fine("=======> resolvEl(" + expression + ") | Returned : " + o);
        }
        return o;
    }
    
    /**
     * Method that returns the value of the passed expression.
     * @param data
     * @return
     */
    public Object resolvEl(String data) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Application app = fc.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = fc.getELContext();
        ValueExpression valueExp =
            elFactory.createValueExpression(elContext, "#{data." + data + ".dataProvider}", Object.class);
        return valueExp.getValue(elContext);
    }

    /**
     * Method to reset the Values of all the input items.
     * @param adfFacesContext - Current Instance of AdfFacesContext
     * @param component - UIComponent for whome we want to reset fields.
     */
    @SuppressWarnings("unused")
    private static void resetValueOfInputItems(AdfFacesContext adfFacesContext, UIComponent component) {
        List<UIComponent> items = component.getChildren();
        for (UIComponent item : items) {
            resetValueOfInputItems(adfFacesContext, item);
            if (item instanceof RichInputText) {
                RichInputText input = (RichInputText) item;
                if (!input.isDisabled()) {
                    input.resetValue();
                    input.setValue("");
                    adfFacesContext.addPartialTarget(input);
                }
            } else if (item instanceof RichInputDate) {
                RichInputDate input = (RichInputDate) item;
                if (!input.isDisabled()) {
                    input.resetValue();
                    input.setValue("");
                    adfFacesContext.addPartialTarget(input);
                }
            } else if (item instanceof RichSelectOneChoice) {
                RichSelectOneChoice input = (RichSelectOneChoice) item;
                if (!input.isDisabled()) {
                    input.resetValue();
                    input.setValue("");
                    adfFacesContext.addPartialTarget(input);
                }
            } else if (item instanceof RichInputListOfValues) {
                RichInputListOfValues input = (RichInputListOfValues) item;
                if (!input.isDisabled()) {
                    input.resetValue();
                    input.setValue("");
                    adfFacesContext.addPartialTarget(input);
                }
            }
        }
    }

    /**
     * Method to fetch Component client id.
     * @param comp
     * @param id
     * @return
     */
    public static String getComponentClientIdFromId(UIComponent comp, String id) {
        if (comp != null) {
            StringBuilder clientId = new StringBuilder(comp.getClientId());
            clientId = new StringBuilder(clientId.substring(0, clientId.lastIndexOf(":") + 1));
            return clientId.append(id).toString();
        } else {
            return null;
        }
    }

    /**
     * Programmatic fetching a bean instance in another bean.
     * @param beanNameWithScope : like #{pageFlowScope.HelloJava}
     * @param beanClassNm : HelloJava.class
     * @return Class : 
     */
    public static Object getBean(String beanNameWithScope, Class beanClassNm) 
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
        ValueExpression exp = expressionFactory.createValueExpression(elContext, beanNameWithScope, beanClassNm);
        return exp.getValue(elContext);
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
    
    /**
         * Get the page flow scope
         * @return
         */
        public static Map getPageFlowScope() {
            return AdfFacesContext.getCurrentInstance().getPageFlowScope();
        }
    
    public static void showPopup(RichPopup popup) 
    {
          FacesContext ctx = FacesContext.getCurrentInstance();
          String popupId = popup.getClientId(ctx);
          ExtendedRenderKitService erkService =
              Service.getService(ctx.getRenderKit(),ExtendedRenderKitService.class);
         erkService.addScript(ctx,"var hints = {autodismissNever:true}; " + "AdfPage.PAGE.findComponent('" + popupId +  "').show(hints);");
    }
    
    /**
     * Created On:10-09-2021
     * Created By:Aakash Kumar
     * Remaorks:
     */
    public static String uploadFilePath() 
    {
        PreparedStatement ps = null;
        ResultSet filePathResultset = null;
        Connection conn = null;

        String filepath = null;

        try {
            conn = openDataSourceConnection();
            ps =
                conn.prepareStatement("SELECT PATH FROM SERVER_PATH WHERE MODULE ='UPLOAD_DIR'");
            filePathResultset = ps.executeQuery();
            while (filePathResultset.next()) 
            {
                filepath = filePathResultset.getString(1);
                System.out.println("Main Server path is---->>" + filePathResultset.getString(1));
            }
        } catch (Exception e) {
            if(conn!=null){
            closeDataSourceConnection(conn);
            }
        } finally{
            if(conn!=null){
            closeDataSourceConnection(conn);
            }
        }
        return filepath;
    } 
    
    
//    public static String uploadFile(UploadedFile file)
//    {
//       UploadedFile myfile = file;
//       String path = null;
//       if (myfile == null) 
//       {
//       } 
//       else 
//       {
//           String uploadfilepath=(String)uploadFilePath();
//           System.out.println("Upload File Path return from db----->>"+uploadfilepath);
//            // All uploaded files will be stored in below path
//            path = uploadfilepath.toString() + myfile.getFilename();
//            InputStream inputStream = null;
//            try
//            {
//                FileOutputStream out = new FileOutputStream(path);
//                inputStream = myfile.getInputStream();
//                byte[] buffer = new byte[8192];
//                int bytesRead = 0;
//                while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
//                out.write(buffer, 0, bytesRead);
//                }
//                out.flush();
//                out.close();
//            } catch (Exception ex) {
//                // handle exception
//                ex.printStackTrace();
//            } 
//            finally 
//            {
//            try {
//            inputStream.close();
//            } catch (Exception e) {
//            }
//            }
//        }
//            return path;
//    }
    
    
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
    
    public static void setLastUpdatedByNew(String itr,String colname)
    {
        System.out.println("in adfutil iterator:"+itr);
        System.out.println("in adfutil colname:"+colname);
        String empcd=(String)resolveExpression("#{pageFlowScope.EmpCode}");
        empcd=empcd!=null?empcd:"Admin";
        Timestamp t=new Timestamp(System.currentTimeMillis());
        System.out.println("empcode:"+empcd+" timestamp:"+t);
        DCIteratorBinding dci = findIterator(itr);
        if(colname.equalsIgnoreCase("LastUpdatedBy"))
        {
            dci.getCurrentRow().setAttribute("LastUpdatedBy",empcd);
            dci.getCurrentRow().setAttribute("LastUpdateDate",t);
        }else if(colname.equalsIgnoreCase("ModifiedBy"))
        {
            dci.getCurrentRow().setAttribute("ModifiedBy",empcd);
            dci.getCurrentRow().setAttribute("ModifiedDate",t);
        }
    }
    
    public static void setLastUpdatedBy(String itr)
    {
        String empcd=(String)resolveExpression("#{pageFlowScope.empCode}") ; 
        System.out.println("in adfutil iterator:"+itr);
        DCIteratorBinding dci = findIterator(itr);
        RowSetIterator rsi=dci.getViewObject().createRowSetIterator(null);
        while(rsi.hasNext())
        {
            Row r = rsi.next();
            System.out.println("empcd"+empcd);
            r.setAttribute("LastUpdatedBy", empcd);
        }
        rsi.closeRowSetIterator();
    }
    
    /**
     * Method to show simple facesMessage
     * @param detail Detail of the Messsage
     * @param severity Severity of FacesMessage ex : FacesMessage.SEVERITY_WARN
     */
    public static void showGlobelFatal(String header,String detail,javax.faces.application.FacesMessage.Severity severity) 
    {
        StringBuilder saveMsg = new StringBuilder("<html><body>");
        saveMsg.append("<b><p style='color:red'>");
        saveMsg.append(header);
        saveMsg.append("</p></b>");
        saveMsg.append("<b>");
        saveMsg.append(detail);
        saveMsg.append("</b></body></html>");
        FacesMessage msg = new FacesMessage(saveMsg.toString());
        msg.setSeverity(severity);
        FacesContext.getCurrentInstance().addMessage(null, msg);
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
    
    public static void showMessageWithClientId(String header, String detail,
                                        javax.faces.application.FacesMessage.Severity severity, String clientId) {
        FacesMessage msg = new FacesMessage(header, detail);
        msg.setSeverity(severity);
        FacesContext.getCurrentInstance().addMessage(clientId, msg);
    }


    public static void showFacesMessageInfo(String header, String detail) 
    {
        StringBuilder saveMsg = new StringBuilder("<html><body><b><p style='color:blue'>");
        saveMsg.append(header);
        saveMsg.append("</p></b>");
        saveMsg.append("<b>");
        saveMsg.append(detail);
        saveMsg.append("</b></body></html>");
        FacesMessage msg = new FacesMessage(saveMsg.toString());
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public static void showFacesMessageWarn(String header, String detail) 
    {
        StringBuilder saveMsg = new StringBuilder("<html><body><b><p style='color:#FABE01'>");
        saveMsg.append(header);
        saveMsg.append("</p></b>");
        saveMsg.append("<b>");
        saveMsg.append(detail);
        saveMsg.append("</b></body></html>");
        FacesMessage msg = new FacesMessage(saveMsg.toString());
        msg.setSeverity(FacesMessage.SEVERITY_WARN);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public static void showFacesMessageError(String header, String detail) 
    {
        StringBuilder saveMsg = new StringBuilder("<html><body><b><p style='color:red'>");
        saveMsg.append(header);
        saveMsg.append("</p></b>");
        saveMsg.append("<b>");
        saveMsg.append(detail);
        saveMsg.append("</b></body></html>");
        FacesMessage msg = new FacesMessage(saveMsg.toString());
        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public static Connection openDataSourceConnection()throws Exception
    {
        Connection conn=null;
        Context ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/APPLICATIONDBDS");
        conn = ds.getConnection();
        
        return conn;
    }
    
    public static void closeDataSourceConnection(Connection conn) 
    {
        try
        {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally
        {
            if(conn!=null)
            {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static String resourceExpression(String res)
    {
        Object el = null;
        if (res != null) {
            StringBuilder s = new StringBuilder("#{bundle['");
            s.append(res);
            s.append("']}");
            el = elExpressionResolve(s.toString());
        }
        if (LogUtil.enableLogger) {
            LogUtil.showInfoLog(logger, "resolvRsrc", new Object[] { "res" }, new Object[] { res }, el);
        }
        return (el == null ? "<Resource does not Exist>" : el.toString());
    }
    
    public static String jasperReportFileName(String fileId) 
    {
        PreparedStatement ps = null;
        ResultSet reportPathRs = null;
        Connection conn = null;

        String reportFileName = null;

        try {
            conn = openDataSourceConnection();
            ps =
                conn.prepareStatement("SELECT PRINT_FILE_NAME FROM FILE_MASTER_ERP WHERE FILE_ID ='" +fileId+"'");
            reportPathRs = ps.executeQuery();
            while (reportPathRs.next()) 
            {
                reportFileName = reportPathRs.getString(1);
                System.out.println("Main Server path is---->>" + reportPathRs.getString(1));
            }
        } catch (Exception e) {
            if(conn!=null){
            closeDataSourceConnection(conn);
            }
        } finally{
            if(conn!=null){
            closeDataSourceConnection(conn);
            }
        }
        return reportFileName;
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
    
    public static void downloadAttachment(String path, OutputStream outputStream) throws IOException {
        File filed = new File(path);
        FileInputStream fis;
        byte[] b;
        try {
            fis = new FileInputStream(filed);

            int n;
            while ((n = fis.available()) > 0) {
                b = new byte[n];
                int result = fis.read(b);
                outputStream.write(b, 0, b.length);
                if (result == -1)
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        outputStream.flush();
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
    
    public static String fileRename(String fileName) {
    File file = new File(fileName);
    int index = file.getName().lastIndexOf(".");
    String orgFileName= file.getName().substring(0,index);
    String ext = file.getName().substring(index);
    java.sql.Timestamp timeStamp= new java.sql.Timestamp(System.currentTimeMillis());
    String renameFile = orgFileName+timeStamp.toString().replaceAll("[-+.^: ,]","").substring(0, 14) + ext;
    File fileRenameObj = new File(renameFile);
   // boolean flag = file.renameTo(fileRenameObj);
 
        return fileRenameObj.getName().toString(); 
   
    }
    
    public static Object resolvElDC(String data) {
               FacesContext fc = FacesContext.getCurrentInstance();
               Application app = fc.getApplication();
               ExpressionFactory elFactory = app.getExpressionFactory();
               ELContext elContext = fc.getELContext();
               ValueExpression valueExp = elFactory.createValueExpression(elContext, "#{data." + data + ".dataProvider}", Object.class);
               return valueExp.getValue(elContext);
           }
    
    
}
    