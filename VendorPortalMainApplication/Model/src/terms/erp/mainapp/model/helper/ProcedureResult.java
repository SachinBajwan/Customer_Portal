package terms.erp.mainapp.model.helper;

import java.util.List;
import java.util.Map;

public abstract class ProcedureResult {
    public ProcedureResult() {
        super();
    }


    public abstract void setValues(Object... obj);

    public <K> void addToCollection(K collection) {
        if (collection instanceof List)
            ((List) collection).add(this);
        else if (collection instanceof Map)
            ((Map) collection).put(this, this);
    }

}