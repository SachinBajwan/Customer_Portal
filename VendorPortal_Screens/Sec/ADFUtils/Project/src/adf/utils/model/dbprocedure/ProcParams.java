package adf.utils.model.dbprocedure;


public class ProcParams
{
    public static final int IN = 1;
    public static final int OUT = 0;
    /**
     * srNo - holds the position of the Variable.
     * paramType - defines what type of Parameter i.e. IN or OUT.
     * dataType - defines the parameter datatype i.e. NUMERIC or VARCHAR2.
     * value - defines the value to the parameter.
     */
    private Integer srNo;
    private Integer paramType;
    private Integer dataType;
    private Object value;

    /**
     * @param srNo
     * @param dataType
     * @param paramType
     * @param value
     */
    public ProcParams(Integer srNo,Integer dataType,Integer paramType,Object value) {
        super();
        this.srNo = srNo;
        this.dataType = dataType;
        this.paramType = paramType;
        this.value = value;
    }

    /**
     * @param value
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * @return
     */
    public Object getValue() {
        return value;
    }

    /**
     * @param srNo
     */
    public void setSrNo(Integer srNo) {
        this.srNo = srNo;
    }

    /**
     * @return
     */
    public Integer getSrNo() {
        return srNo;
    }

    /**
     * @param paramType
     */
    public void setParamType(Integer paramType) {
        this.paramType = paramType;
    }

    /**
     * @return
     */
    public Integer getParamType() {
        return paramType;
    }

    /**
     * @param dataType
     */
    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    /**
     * @return
     */
    public Integer getDataType() {
        return dataType;
    }
}
