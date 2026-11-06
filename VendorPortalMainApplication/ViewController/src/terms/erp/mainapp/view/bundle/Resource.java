package terms.erp.mainapp.view.bundle;

import adf.utils.model.ADFModelUtils;

import java.util.ListResourceBundle;

import java.io.File;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import oracle.adf.share.logging.ADFLogger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import oracle.adf.share.logging.ADFLogger;

import terms.erp.mainapp.model.services.TermsMainAppAMImpl;
import terms.erp.mainapp.view.helper.ResourceFilter;
import terms.erp.mainapp.view.utils.AMConfigUtils;
import terms.erp.mainapp.view.utils.TermsADFUtils;


public class Resource extends ListResourceBundle {

    private static ADFLogger logger = ADFLogger.createADFLogger(Resource.class);

    public Resource() {
        for (int i = 0; i < 50000; i++) {
            contents[i][0] = "";
            contents[i][1] = "";

        }

    }
    private static final Object[][] contents = new Object[50000][2];

    public Object[][] getContents() 
    {
        try 
        {
            String filepath ="";
            
            System.out.println((String) getResourceFilePath() + "<<--------Resource Path---->>");
            filepath = getResourceFilePath().toString();
            
            logger.info("_________Local Resource path------->>"+filepath);
            
            File file = new File(filepath);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("label");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) 
                {
                    Element eElement = (Element) nNode;

                    contents[temp][0] = getTagValue("key", eElement);
                    contents[temp][1] = getTagValue("value", eElement);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contents;
    }


    private static String getTagValue(String sTag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        return nValue.getNodeValue();
    }

    public Object resolvEl(String data) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Application app = fc.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = fc.getELContext();
        ValueExpression valueExp = elFactory.createValueExpression(elContext, data, Object.class);
        Object Message = valueExp.getValue(elContext);
        return Message;
    }
    
    public String getLangFilePathFromDB()
    {
        System.out.println("<<---------getLangFilePathFromDB------------>>");
        String path="";
        PreparedStatement ps=null;
        ResultSet rs=null;
        TermsMainAppAMImpl am = AMConfigUtils.getAM();
        
        try 
        {
            ps = am.getDBTransaction().createPreparedStatement("SELECT PATH FROM SERVER_PATH WHERE MODULE="+"'RESOURCE_PATH'", 0);
            rs = ps.executeQuery();
            while (rs.next())
            {
                //System.out.println(rs.getString(1)+"RETURNED FROM DB___");
                path = rs.getString(1);
                //                        rs.close();
                //                        ps.close();
                System.out.println("Picking Value of Resource Path From DB .."+path);
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if(rs!=null){
                rs.close();
                }
            }
            catch (Exception e)
            {
               e.printStackTrace();
            }
            try
            {
                if(ps!=null){
                ps.close(); 
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            if(am!=null)
            {
                AMConfigUtils.releaseAM();
            }
        }
        return path;  
       }
    
    
    public Object getResourceFilePath() 
    {

        String resFilepath=null;
        try 
        { 
            HttpSession session = ResourceFilter.getSession();
            if (session == null) {
                session = TermsADFUtils.getSession();
            }
                resFilepath= getLangFilePathFromDB();
            System.out.println("Actual resource path from main app---->>"+resFilepath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resFilepath;
    }
}
