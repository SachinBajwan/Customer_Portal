package terms.erp.mainapp.model.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TermsUnitDetail extends ProcedureResult {

    private String unitCode;
    private String unitName;
    //
//    private List<TermsProjectDetail> visibleProjects = new ArrayList<>();
    private List visibleModlue = new ArrayList<>();


    public String getUnitCode() {
        return unitCode;
    }

    public String getUnitName() {
        return unitName;
    }


    public void setVisibleModlue(List visibleModlue) {
        this.visibleModlue = visibleModlue;
    }

    public List getVisibleModlue() {
        return visibleModlue;
    }

//    public void setVisibleProjects(List<TermsProjectDetail> visibleProjects) {
//        this.visibleProjects = visibleProjects;
//    }
//
//    public List<TermsProjectDetail> getVisibleProjects() {
//        return visibleProjects;
//    }


    public TermsUnitDetail() {
        super();
    }

    public TermsUnitDetail(String unitCode) {
        super();
        this.unitCode = unitCode;
    }

    public TermsUnitDetail(String unitCode, String unitName) {
        super();
    }


    @Override
    public void setValues(Object[] obj) {
        // TODO Implement this method
        System.out.println(" unitCode:"+obj[0]+" unitName:"+obj[1]);
        this.unitCode = (String) obj[0];
        this.unitName = (String) obj[1];

        Object list = obj[2];

//        if (list == null || list.toString().trim().length() == 0) {
//            return;
//        } else {
//            String[] projects = ((String) list).split(",");
//
//
//            for (String row : projects) {
//
//                String cols[] = row.split(":");
//                if (cols.length > 1)
//                    visibleProjects.add(new TermsProjectDetail(cols[0], cols[1]));
//            }
//
//
//        }
//        list = obj[3];

        if (list == null || list.toString().trim().length() == 0) {
            return;
        } else {
            String[] modules = ((String) list).split(",");
            this.visibleModlue = Arrays.asList(modules);
            System.out.println("modules :"+this.visibleModlue);
        }

    }

    public <K> void addToCollection(K collection) {
        if (collection instanceof List)
            ((List) collection).add(this);
        else if (collection instanceof Map)
            ((Map) collection).put(this.getUnitCode(), this);
    }


}

