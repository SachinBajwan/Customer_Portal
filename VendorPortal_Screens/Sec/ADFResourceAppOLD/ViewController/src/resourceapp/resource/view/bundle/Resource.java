package resourceapp.resource.view.bundle;

import java.util.ListResourceBundle;

import java.io.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.servlet.http.HttpSession;

import javax.sql.DataSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import oracle.adf.share.logging.ADFLogger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class Resource extends ListResourceBundle 
{
    private static ADFLogger logger = ADFLogger.createADFLogger(Resource.class);

    public Resource() {
        for (int i = 0; i < 50000; i++) {
            contents[i][0] = "";
            contents[i][1] = "";

        }

    }
    private static final Object[][] contents = new Object[50000][2];


    public String getFilePathFromDB() 
    {
        String localUserName="Aakash";
        PreparedStatement ps = null;
        ResultSet reportPathRs = null;
        Connection conn = null;

        String resFilepath = null;

        try {
            conn = openDataSourceConnection();
            ps =
                conn.prepareStatement("SELECT LOCAL_USER_PATH FROM LOCAL_RESOURCE_PATH WHERE USER_NAME ='" +localUserName+"'");
            reportPathRs = ps.executeQuery();
            while (reportPathRs.next()) 
            {
                resFilepath = reportPathRs.getString(1);
                System.out.println("Local Resource Server path is---->>" + reportPathRs.getString(1));
            }
        } catch (Exception e) 
        {
            if(conn!=null)
            {
            closeDataSourceConnection(conn);
            }
        } finally{
            if(conn!=null){
            closeDataSourceConnection(conn);
            }
        }
        return resFilepath;
    } 
    
    
    public Object getResourceFilePath() 
    {
        String defaultPath = "/Home/aakash/Resources/Resource_EN.xml";
        String localserverpath="";
        String resFilepath=null;
        try 
        {
            localserverpath = (String)getFilePathFromDB();
            System.out.println("Local Server Resource file path----->>"+localserverpath);
            if(localserverpath!=null)
            {
                resFilepath= localserverpath;
            }else{
                resFilepath= defaultPath;
            }
            System.out.println("Actual resource path from Local Application---->>"+resFilepath);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return resFilepath;
    }
    
    
    public Object[][] getContents() 
    {
        try 
        {

            String path = (String)getResourceFilePath();
            
            logger.info("_________Local Resource path------->>"+ path);
            
            File file = new File(path);
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
}

