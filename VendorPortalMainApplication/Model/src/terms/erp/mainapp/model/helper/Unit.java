package terms.erp.mainapp.model.helper;

import java.util.List;
import java.util.Map;

public class Unit extends ProcedureResult{
    
    String unitCode;
    String unitName;
    
    public Unit() {
        super();
    }
    
    public Unit(String unitCode) {
        super();
        this.unitCode = unitCode;
    }

    public Unit(String unitCode, String unitName) {
        super();
    }

    @Override
    public void setValues(Object[] obj) {
        unitCode = (String) obj[0];
        unitName = (String) obj[1];
    }


    public String getUnitCode() {
        return unitCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public <K> void addToCollection(K collection) {
        if (collection instanceof List)
            ((List) collection).add(this);
        else if (collection instanceof Map)
            ((Map) collection).put(this.unitCode, this);
    }
}
