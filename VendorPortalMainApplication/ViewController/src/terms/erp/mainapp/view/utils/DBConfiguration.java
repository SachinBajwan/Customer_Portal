package terms.erp.mainapp.view.utils;

import java.io.BufferedReader;

import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import oracle.adf.share.logging.ADFLogger;

import oracle.jbo.JboException;
import oracle.jbo.client.Configuration;

import oracle.jdbc.internal.OracleTypes;

import terms.erp.mainapp.model.helper.ProcedureResult;
import terms.erp.mainapp.model.services.TermsMainAppAMImpl;


public class DBConfiguration {

    private static ADFLogger logger = ADFLogger.createADFLogger(DBConfiguration.class);

    public DBConfiguration() {
        super();
    }


    /**
     *method to create am
     * @return
     */
    public static TermsMainAppAMImpl createAM() {
        TermsMainAppAMImpl am =
            (TermsMainAppAMImpl) Configuration.createRootApplicationModule("terms.erp.mainapp.model.services.TermsMainAppAMImpl",
                                                                               "TermsMainAppAMLocal");
        // System.out.println("Creating AM----->>" + am);
        return am;
    }

    /**
     *method to release am
     * @param am
     */
    public static void releaseAM(TermsMainAppAMImpl am) {

        Configuration.releaseRootApplicationModule(am, true);
        // System.out.println("Releasing AM----->>" + am);
    }

    public static Object callFunction(int sqlReturnType, String stmt, Object[] bindVars) {
        logger.log("input parameters are " + Arrays.toString(bindVars));
        TermsMainAppAMImpl am = createAM();
        CallableStatement st = null;
        try {

            st = am.getDBTransaction().createCallableStatement("begin ? := " + stmt + ";end;", 0);
            st.registerOutParameter(1, sqlReturnType);
            if (bindVars != null) {
                for (int z = 0; z < bindVars.length; z++) {
                    st.setObject(z + 2, bindVars[z]);
                }
            }
            st.executeUpdate();

            logger.log("Output is " + st.getObject(1));

            return st.getObject(1);
        } catch (SQLException e) {
            int end = e.getMessage().indexOf("\n");

            throw new JboException(e.getMessage());
        } finally {
            if (st != null) {
                try {
                    st.close();
                    releaseAM(am);
                } catch (SQLException e) {
                    throw new JboException(e.getMessage());
                }
            }
        }
    }


    public static Object[] callFunctionOutParam(int sqlReturnType, String stmt, Object[] inBindVars,
                                                Object[] outBindVar) {
        logger.log("input parameters are " + Arrays.toString(inBindVars));
        TermsMainAppAMImpl am = createAM();
        CallableStatement st = null;
        try {

            st = am.getDBTransaction().createCallableStatement("begin ? := " + stmt + ";end;", 0);
            st.registerOutParameter(1, sqlReturnType);
            int inLength = 0;
            if (inBindVars != null) {
                inLength = inBindVars.length;
                for (int z = 1; z <= inBindVars.length; z++) {
                    st.setObject(z + 1, inBindVars[z - 1]);
                }
            }

            if (outBindVar != null) {

                for (int z = 1; z <= outBindVar.length; z++) {
                    st.registerOutParameter(z + inLength + 1, (Integer) outBindVar[z - 1]);
                }
            }
            st.executeUpdate();
            Object[] retArary = new Object[outBindVar.length + 1];
            retArary[0] = st.getObject(1);
            if (outBindVar != null) {

                for (int z = 1; z <= outBindVar.length; z++) {
                    retArary[z] = st.getObject(z + inLength + 1);
                }
            }
            logger.log("output parameters are " + Arrays.toString(retArary));

            return retArary;
        } catch (SQLException e) {
            int end = e.getMessage().indexOf("\n");
            e.printStackTrace();
            return null;
        } finally {
            if (st != null) {
                try {
                    st.close();
                    releaseAM(am);
                } catch (SQLException e) {
                    throw new JboException(e.getMessage());
                }
            }
        }
    }


    public static Object[] callProcedure(String stmt, Object[] inBindVars, Integer[] outBindVars) {
        logger.log("input parameters are " + Arrays.toString(inBindVars));
        TermsMainAppAMImpl am = createAM();

        CallableStatement st = null;
        try {

            st = am.getDBTransaction().createCallableStatement("begin ? " + stmt + ";end;", 0);

            int inLength = 0;
            if (inBindVars != null) {
                inLength = inBindVars.length;
                for (int z = 1; z <= inBindVars.length; z++) {
                    st.setObject(z, inBindVars[z - 1]);
                }
            }
            if (outBindVars != null) {
                for (int z = 1; z <= outBindVars.length; z++) {

                    st.registerOutParameter(inLength + z, outBindVars[inLength + z]);

                }
            }
            st.execute();
            ArrayList list = new ArrayList();
            if (outBindVars != null) {
                for (int z = 1; z <= outBindVars.length; z++) {
                    list.add(st.getObject(inLength + z));
                }
            }

            logger.log("output parameters are " + Arrays.toString(list.toArray()));
            return list.toArray();
        } catch (SQLException e) {
            int end = e.getMessage().indexOf("\n");

            throw new JboException(e.getMessage());
        } finally {
            if (st != null) {
                try {
                    st.close();
                    releaseAM(am);
                } catch (SQLException e) {
                    throw new JboException(e.getMessage());
                }
            }
        }
    }


    public static Object[] callProcedureForRs(String stmt, Object[] inBindVars, Object[] outBindVars) {
        logger.log("input parameters are " + Arrays.toString(inBindVars));
        return callProcedureForRsrc(stmt, inBindVars, outBindVars);
    }


    public static <K> Object[] callFunctionForRs(int retValue, String stmt, Object[] inBindVars, Object[] outBindVars,
                                                 K collection) {
        logger.log("input parameters are " + Arrays.toString(inBindVars));
        TermsMainAppAMImpl am = createAM();
        CallableStatement st = null;
        try {

            st = am.getDBTransaction().createCallableStatement("begin ? :=" + stmt + ";end;", 0);
            st.registerOutParameter(1, retValue);
            int inLength = 0;
            if (inBindVars != null) {
                inLength = inBindVars.length;
                for (int z = 1; z <= inBindVars.length; z++) {
                    st.setObject(z + 1, inBindVars[z - 1]);
                }
            }
            ArrayList<Integer> rss = new ArrayList<Integer>();
            if (outBindVars != null) {
                for (int z = 1; z <= outBindVars.length; z++) {
                    Object obj = outBindVars[z - 1];

                    if ((obj instanceof Class) && (ProcedureResult.class.isAssignableFrom((Class) obj))) {
                        rss.add((Integer) inLength + z + 1);
                        st.registerOutParameter(inLength + z + 1, OracleTypes.CURSOR);
                    } else {
                        st.registerOutParameter(inLength + z + 1, (Integer) obj);
                    }
                }
            }
            st.execute();
            //System.out.println("List is "+Arrays.toString(rss.toArray()));
            ArrayList list = new ArrayList();
            list.add(st.getObject(1));
            if (outBindVars != null) {
                for (int z = 1; z <= outBindVars.length; z++) {
                    Object obj = st.getObject(inLength + z + 1);
                    if (rss.contains(inLength + z + 1)) {
                        ResultSet rs = (ResultSet) obj;
                        // System.out.println("For intdex"+z);

                        if (rs == null)
                            continue;

                        while (rs.next()) {
                            // System.out.println("Has Next");
                            Object[] ProcResultableArray = new Object[(rs.getMetaData()).getColumnCount()];
                            for (int i = 1; i <= (rs.getMetaData()).getColumnCount(); i++) {
                                Object colVal = rs.getObject(i);
                                if (rs.getMetaData().getColumnType(i) == Types.CLOB) {
                                    Clob clob = (Clob) rs.getObject(i);
                                    colVal = converClobToString(clob);

                                }

                                ProcResultableArray[i - 1] = colVal;
                            }
                            ProcedureResult procObj = null;
                            try {
                                procObj = (ProcedureResult) ((Class) (outBindVars[z - 1])).newInstance();
                                procObj.setValues(ProcResultableArray);
                            } catch (IllegalAccessException | InstantiationException e) {
                                e.printStackTrace();
                            }

                            procObj.addToCollection(collection);
                        }
                        obj = collection;
                        rs.close();
                    }

                    list.add(obj);

                }
            }
            logger.log("output parameters are " + Arrays.toString(list.toArray()));
            return list.toArray();
        } catch (SQLException e) {
            int end = e.getMessage().indexOf("\n");
            e.printStackTrace();
            throw new JboException(e.getMessage());
        } finally {
            if (st != null) {
                try {
                    st.close();
                    releaseAM(am);
                } catch (SQLException e) {
                    throw new JboException(e.getMessage());
                }
            }
        }
    }

    public static String converClobToString(Clob clob) {
        if (clob == null)
            return null;

        StringBuffer strOut = new StringBuffer();
        String aux;
        try {
            BufferedReader br = new BufferedReader(clob.getCharacterStream());
            while ((aux = br.readLine()) != null) {
                strOut.append(aux);
                //  strOut.append(System.getProperty("line.separator"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String clobStr = strOut.toString();


        return clobStr;
    }


    public static Object[] callFunctionForMultiResult(int retValue, String stmt, Object[] inBindVars, Object[] outBindVars,
                                                  Collection... collections) {
        logger.log("input parameters are " + Arrays.toString(inBindVars));
        TermsMainAppAMImpl am = createAM();
        CallableStatement st = null;
        try {

            st = am.getDBTransaction().createCallableStatement("begin ? :=" + stmt + ";end;", 0);
            st.registerOutParameter(1, retValue);
            int inLength = 0;
            if (inBindVars != null) {
                inLength = inBindVars.length;
                for (int z = 1; z <= inBindVars.length; z++) {
                    st.setObject(z + 1, inBindVars[z - 1]);
                }
            }
            ArrayList<Integer> rss = new ArrayList<Integer>();
            if (outBindVars != null) {
                for (int z = 1; z <= outBindVars.length; z++) {
                    Object obj = outBindVars[z - 1];

                    if ((obj instanceof Class) && (ProcedureResult.class.isAssignableFrom((Class) obj))) {
                        rss.add((Integer) inLength + z + 1);
                        st.registerOutParameter(inLength + z + 1, OracleTypes.CURSOR);
                    } else {
                        st.registerOutParameter(inLength + z + 1, (Integer) obj);
                    }
                }
            }
            st.execute();
            ArrayList list = new ArrayList();
            int collectionPos = 0;
            list.add(st.getObject(1));
            if (outBindVars != null) {
                for (int z = 1; z <= outBindVars.length; z++) {
                    Object obj = st.getObject(inLength + z + 1);
                    if (rss.contains(inLength + z + 1)) {
                        ResultSet rs = (ResultSet) obj;
                        if (rs == null)
                            continue;
                        while (rs.next()) {
                            // System.out.println("Has Next");
                            Object[] ProcResultableArray = new Object[(rs.getMetaData()).getColumnCount()];
                            for (int i = 1; i <= (rs.getMetaData()).getColumnCount(); i++) {
                                Object colVal = rs.getObject(i);
                                if (rs.getMetaData().getColumnType(i) == Types.CLOB) {
                                    colVal = converClobToString((Clob) rs.getObject(i));

                                }

                                ProcResultableArray[i - 1] = colVal;
                            }
                            ProcedureResult procObj = null;
                            try {
                                procObj = (ProcedureResult) ((Class) (outBindVars[z - 1])).newInstance();
                                procObj.setValues(ProcResultableArray);
                            } catch (IllegalAccessException | InstantiationException e) {
                                e.printStackTrace();
                            }

                            procObj.addToCollection(collections[collectionPos]);

                        }
                        obj = collections[collectionPos];
                        collectionPos++;
                        rs.close();
                    }

                    list.add(obj);

                }
            }
            logger.log("output parameters are " + Arrays.toString(list.toArray()));
            return list.toArray();
        } catch (SQLException e) {
            int end = e.getMessage().indexOf("\n");
            e.printStackTrace();
            throw new JboException(e.getMessage());
        } finally {
            if (st != null) {
                try {
                    st.close();
                    releaseAM(am);
                } catch (SQLException e) {
                    throw new JboException(e.getMessage());
                }
            }
        }
    }

    public static Object[] callFunctionForMultiResult(int retValue, String stmt, Object[] inBindVars, Object[] outBindVars,
                                                  Map... map) {
        System.out.println("input parameters are " + Arrays.toString(inBindVars)+" retValue:"+retValue+" stmt:"+stmt+" outBindVars:"+Arrays.toString(outBindVars));
        TermsMainAppAMImpl am = createAM();
        CallableStatement st = null;
        try {

            st = am.getDBTransaction().createCallableStatement("begin ? :=" + stmt + ";end;", 0);
            st.registerOutParameter(1, retValue);
            int inLength = 0;
            if (inBindVars != null) {
                inLength = inBindVars.length;
                System.out.println(" inBindVars.length:"+inBindVars.length);
                for (int z = 1; z <= inBindVars.length; z++) {
                    System.out.println("st.setObject("+(z+1)+")"+" inBindVars position:"+(z - 1)+" value:"+inBindVars[z - 1]);
                    st.setObject(z + 1, inBindVars[z - 1]);
                }
            }
            
            ArrayList<Integer> rss = new ArrayList<Integer>();
            if (outBindVars != null) {
                System.out.println(" outBindVars.length:"+outBindVars.length);
                for (int z = 1; z <= outBindVars.length; z++) {
                    Object obj = outBindVars[z - 1];
                    System.out.println("outBindVars position:"+(z - 1)+" value:"+outBindVars[z - 1]+" (obj instanceof Class):"+(obj instanceof Class));
                    if ((obj instanceof Class) && (ProcedureResult.class.isAssignableFrom((Class) obj))) {
//                        System.out.println(" ProcedureResult.class.isAssignableFrom((Class) obj)):"+ProcedureResult.class.isAssignableFrom((Class) obj));
                        System.out.println("Array rss adding:"+(inLength + z + 1));
                        rss.add((Integer) inLength + z + 1);
                        System.out.println("Array Printing:"+rss);
                        System.out.println("st.registerOutParameter("+(inLength+z+1)+")"+" value:"+OracleTypes.CURSOR);
                        st.registerOutParameter(inLength + z + 1, OracleTypes.CURSOR);
                    } else {
                        System.out.println("st.registerOutParameter("+(inLength+z+1)+")"+" value:"+Arrays.asList(obj));
                        st.registerOutParameter(inLength + z + 1, (Integer) obj);
                    }
                }
            }
            st.execute();
            System.out.println("List is "+Arrays.toString(rss.toArray()));
            ArrayList list = new ArrayList();
            int collectionPos = 0;
            list.add(st.getObject(1));
            if (outBindVars != null) {
                for (int z = 1; z <= outBindVars.length; z++) {
                    Object obj = st.getObject(inLength + z + 1);
                    if (rss.contains(inLength + z + 1)) {
                        ResultSet rs = (ResultSet) obj;
                        if (rs == null)
                            continue;
                        // System.out.println("For intdex"+z);
                        while (rs.next()) {
                            // System.out.println("Has Next");
                            Object[] ProcResultableArray = new Object[(rs.getMetaData()).getColumnCount()];
                            for (int i = 1; i <= (rs.getMetaData()).getColumnCount(); i++) {
                                Object colVal = rs.getObject(i);
                                System.out.println(" colVal:"+colVal);
                                if (rs.getMetaData().getColumnType(i) == Types.CLOB) {
                                    colVal = converClobToString((Clob) rs.getObject(i));
                                    System.out.println(" clob["+i+"]:"+colVal);
                                }

                                ProcResultableArray[i - 1] = colVal;
                            }
                            ProcedureResult procObj = null;
                            try {
                                procObj = (ProcedureResult) ((Class) (outBindVars[z - 1])).newInstance();
                                procObj.setValues(ProcResultableArray);
                            } catch (IllegalAccessException | InstantiationException e) {
                                e.printStackTrace();
                            }

                            procObj.addToCollection(map[collectionPos]);

                        }
                        obj = map[collectionPos];
                        collectionPos++;
                        rs.close();
                    }

                    list.add(obj);

                }
            }
            System.out.println("output parameters are " + Arrays.toString(list.toArray()));
            return list.toArray();
        } catch (SQLException e) {
            int end = e.getMessage().indexOf("\n");
            e.printStackTrace();
            throw new JboException(e.getMessage());
        } finally {
            if (st != null) {
                try {
                    st.close();
                    releaseAM(am);
                } catch (SQLException e) {
                    throw new JboException(e.getMessage());
                }
            }
        }
    }


    public static Object[] callProcedureForRsrc(String stmt, Object[] inBindVars, Object[] outBindVars) {
        logger.log("input parameters are " + Arrays.toString(inBindVars));
        TermsMainAppAMImpl am = createAM();
        CallableStatement st = null;
        try {

            st = am.getDBTransaction().createCallableStatement("begin  " + stmt + ";end;", 0);

            int inLength = 0;
            if (inBindVars != null) {
                inLength = inBindVars.length;
                for (int z = 1; z <= inBindVars.length; z++) {
                    st.setObject(z, inBindVars[z - 1]);
                }
            }

            ArrayList<Integer> rss = new ArrayList<Integer>();
            if (outBindVars != null) {
                for (int z = 1; z <= outBindVars.length; z++) {
                    Object obj = outBindVars[inLength + z - 1];

                    if ((obj instanceof Class) && (ProcedureResult.class.isAssignableFrom((Class) obj))) {
                        rss.add((Integer) inLength + z);
                        st.registerOutParameter(inLength + z, OracleTypes.CURSOR);

                    } else {
                        st.registerOutParameter(inLength + z, (Integer) obj);
                    }
                }
            }
            st.execute();

            ArrayList list = new ArrayList();
            if (outBindVars != null) {
                for (int z = 1; z <= outBindVars.length; z++) {
                    Object obj = st.getObject(inLength + z);
                    if (rss.contains((Integer) z)) {
                        ResultSet rs = (ResultSet) obj;
                        if (rs == null)
                            continue;
                        ArrayList<ProcedureResult> rsList = new ArrayList<ProcedureResult>();
                        //  System.out.println("For intdex"+z);

                        while (rs.next()) {
                            // System.out.println("Has Next");
                            Object[] ProcResultableArray = new Object[(rs.getMetaData()).getColumnCount()];
                            for (int i = 1; i <= (rs.getMetaData()).getColumnCount(); i++) {
                                ProcResultableArray[i - 1] = rs.getObject(i);
                            }
                            ProcedureResult procObj = null;
                            try {
                                procObj = (ProcedureResult) ((Class) (outBindVars[z - 1])).newInstance();
                                procObj.setValues(ProcResultableArray);
                            } catch (IllegalAccessException | InstantiationException e) {
                                e.printStackTrace();
                            }

                            procObj.addToCollection(rsList);
                        }
                        obj = rsList;
                        rs.close();
                    }

                    list.add(obj);

                }
            }
            logger.log("output parameters are " + Arrays.toString(list.toArray()));
            return list.toArray();
        } catch (SQLException e) {
            int end = e.getMessage().indexOf("\n");
            e.printStackTrace();
            throw new JboException(e.getMessage());
        } finally {
            if (st != null) {
                try {
                    st.close();
                    releaseAM(am);
                } catch (SQLException e) {
                    throw new JboException(e.getMessage());
                }
            }
        }
    }


}
