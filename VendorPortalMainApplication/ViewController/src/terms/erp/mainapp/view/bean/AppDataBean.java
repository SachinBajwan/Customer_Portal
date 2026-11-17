package terms.erp.mainapp.view.bean;

import adf.utils.bean.ADFBeanUtils;

import java.io.Serializable;

import java.sql.Types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;

import oracle.binding.OperationBinding;

import terms.erp.mainapp.model.helper.FinYear;
import terms.erp.mainapp.model.helper.TermsParam;
import terms.erp.mainapp.model.helper.Unit;
import terms.erp.mainapp.view.utils.DBConfiguration;
import terms.erp.mainapp.view.utils.TermsADFUtils;
import java.util.stream.Collectors;
public class AppDataBean implements Serializable{
    
    public AppDataBean() {
    }
    
    private String unitCode;
    private String finYear;


    public String getUnitCode() 
    {
        System.out.println("getunitCode:"+unitCode);
        if(unitCode == null)
        {
            String currentUnit = (String) TermsADFUtils.getValueFromPageFlowScope("UnitCode");
            setUnitCode(currentUnit);
        }
        return unitCode;
    }

    public void setFinYear(String finYear) {
        this.finYear = finYear;
        TermsADFUtils.setValueToPageFlowScope("FinYear", finYear);
        setGlobalMutableMap();
    }

    public String getFinYear() {
        if(finYear == null){
            String currentFinYear = (String) TermsADFUtils.getValueFromPageFlowScope("FinYear");
            setFinYear(currentFinYear);
        }
        return finYear;
    }

    public List<SelectItem> getUnitList() {
        Collection list = this.getUnitMap().values();
        List<SelectItem> unitList = new ArrayList<>();
        SelectItem newItem;
        for (Object item : list) {
            Unit unit = (Unit) item;
            newItem = new SelectItem();
            newItem.setValue(unit.getUnitCode());
            newItem.setLabel(unit.getUnitCode());

            unitList.add(newItem);
        }
        Collections.sort(unitList, new Comparator<SelectItem>() {
            @Override
            public int compare(SelectItem item1, SelectItem item2) {
                if (item1 != null && item2 != null) {
                    if (item1.getValue() != null && item2.getValue() != null) {
                        return item1.getValue().toString().compareTo(item2.getValue().toString());
                    }
                }
                return 0;
            }
        });
       return unitList;
    }
    
    public List<SelectItem> getFinYearList() {
        Collection list = this.getFinYearMap().values();
        List<SelectItem> finYearList = new ArrayList<>();
        SelectItem newItem;
        for (Object item : list) {
            FinYear finYear = (FinYear) item;
            newItem = new SelectItem();
            newItem.setValue(finYear.getKey());
            newItem.setLabel(finYear.getValue());

            finYearList.add(newItem);
        }
        System.out.println("finYearList:"+finYearList);

        return finYearList;
    }
    
    public Map<String, Unit> getUnitMap() {
        System.out.println("TermsADFUtils.getValueFromPageFlowScope(\"UnitList\"):"+TermsADFUtils.getValueFromPageFlowScope("UnitList"));
        return (Map<String, Unit>) TermsADFUtils.getValueFromPageFlowScope("UnitList");

    }
    
    public Map<String, FinYear> getFinYearMap() {
        System.out.println("TermsADFUtils.getValueFromPageFlowScope(\"FinYearList\"):"+TermsADFUtils.getValueFromPageFlowScope("FinYearList"));
        return (Map<String, FinYear>) TermsADFUtils.getValueFromPageFlowScope("FinYearList");

    }

    public void setUnitCode(String unitCode) {
        System.out.println("setunitCode:"+unitCode);
        this.unitCode = unitCode;
        TermsADFUtils.setValueToPageFlowScope("UnitCode", unitCode);
        String userId = (String) TermsADFUtils.getValueFromPageFlowScope("UserId");
        authUserModule(userId, unitCode);
        setGlobalMutableMap();
    }
    
    private String authUserModule(String userId, String unitCd)
    {
        Object[] obj = null;
        try 
        {
            obj = DBConfiguration.callFunctionForMultiResult(Types.VARCHAR, "AUTH_USER_MOD(?,?,?,?)", new Object[] 
                                                         {
                                                     userId,unitCd }, new Object[] {
                                                     Types.VARCHAR,TermsParam.class}, new HashMap());
                  
            System.out.println( Arrays.toString(obj)) ;
            String result = (String) obj[0];
             System.out.println("result-----::" + obj[0].toString());
            if (result == null)
            {
                return "N";
            }

            if (result.toUpperCase().equals("Y")) 
            {
                this.assignauthUserModuleDataToPageFlowScope(obj);
                return "Y";
            }else{
                return "N";
            }
        } catch (Exception e) 
        {
            e.printStackTrace();
            return "Something went wrong when authUserModule!!";
        }
    }
    
    
    public void assignauthUserModuleDataToPageFlowScope(Object[] obj) 
    {
        System.out.println("object in assignauthUserModuleDataToPageFlowScope:"+Arrays.toString(obj));
        TermsADFUtils.setValueToPageFlowScope("UnitName", obj[1]);
        this.setMapToPageFlowScope((HashMap) obj[2]);
        System.out.println((HashMap) obj[2] + "...............modauth OBJ2");
    }
    
    public void setMapToPageFlowScope(HashMap map) 
    {
        for (Object key : map.keySet()) 
        {
            TermsADFUtils.setValueToPageFlowScope((String) key, map.get(key));
        }
    }
    
    public void setGlobalMutableMap(){
        HashMap<String, Object> mutableMap = new HashMap<String, Object>();
        mutableMap.put("UnitCode", TermsADFUtils.getValueFromPageFlowScope("UnitCode"));
        mutableMap.put("UnitName", TermsADFUtils.getValueFromPageFlowScope("UnitName"));
        mutableMap.put("FinYear", TermsADFUtils.getValueFromPageFlowScope("FinYear"));
        
        mutableMap.put("unitCode", TermsADFUtils.getValueFromPageFlowScope("UnitCode"));
        mutableMap.put("finYear", TermsADFUtils.getValueFromPageFlowScope("FinYear"));
        
        TermsADFUtils.setValueToPageFlowScope("GblMutableMap", mutableMap);
    }
    

}
