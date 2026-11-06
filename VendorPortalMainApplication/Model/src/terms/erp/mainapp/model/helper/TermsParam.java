package terms.erp.mainapp.model.helper;

import java.util.List;
import java.util.Map;

public class TermsParam extends ProcedureResult {

    String key;
    Object value;

    public TermsParam() {
        super();
    }

    @Override
    public void setValues(Object[] obj) {
        key = obj[0].toString();
        value = obj[1];
    }


    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public <K> void addToCollection(K collection) {
        if (collection instanceof List)
            ((List) collection).add(this);
        else if (collection instanceof Map)
            ((Map) collection).put(this.key, this.value);
    }
}
