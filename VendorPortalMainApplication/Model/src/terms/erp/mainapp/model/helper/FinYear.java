package terms.erp.mainapp.model.helper;

import java.util.List;
import java.util.Map;

public class FinYear extends ProcedureResult{
    
    String key;
    String value;

    public FinYear() {
        super();
    }

    @Override
    public void setValues(Object[] obj) {
        key = (String) obj[0];
        value = (String) obj[1];
    }


    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public <K> void addToCollection(K collection) {
        if (collection instanceof List)
            ((List) collection).add(this);
        else if (collection instanceof Map)
            ((Map) collection).put(this.key, this);
    }
}
