package view.bspl.reports.beans;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;

import javax.naming.InitialContext;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

@WebServlet(name = "SaleOrderServlet", urlPatterns = { "/reports/saleorderservlet" })
public class SaleOrderServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
    
    public void callJasper(String reportName, String serverpath, Map parameters, Connection conn, String DocNo,
                           HttpServletResponse response) throws IOException {

        OutputStream ouputStream = null;

        try 
        {
            /* If we want to pass the file as .jsaper , use below two lines*/
            File repFile = new File(serverpath + reportName + ".jasper");
            JasperReport report = (JasperReport) JRLoader.loadObject(repFile);


            /*  If we want to pass the file as .jrxml , use below two lines*/

            /*  InputStream input = new FileInputStream(new File(serverpath + reportName + ".jrxml"));
                JasperDesign design = JRXmlLoader.load(input);
                JasperReport report = JasperCompileManager.compileReport(design); 
            */

            JasperPrint jasperPrint = null;
            jasperPrint = JasperFillManager.fillReport(report, parameters, conn);
            ouputStream = response.getOutputStream();
            String fileName = "SaleOrder_" + DocNo + ".pdf";
            JRExporter exporter = null;
            if (true) {
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");
                exporter = new JRPdfExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);
            }

            exporter.exportReport();
            String str = "<script>window.onload= function () { window.print();window.close();   }  </script>";
            ouputStream.write(str.getBytes());
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (JRException e) {
            e.printStackTrace();
            try {
                conn.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
        } finally 
        {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
            if (ouputStream != null) {
                ouputStream.flush();
                ouputStream.close();
            }
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet reportPathRs = null;
        String reportName = null;
        try{
            
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/APPLICATIONDBDS");
            conn = ds.getConnection();

            String path = null;

            ps = conn.prepareStatement("SELECT PATH FROM SERVER_PATH WHERE MODULE = 'REPSERVERPATH'");
            reportPathRs = ps.executeQuery();
            
            while (reportPathRs.next()) 
            {
                path = reportPathRs.getString(1);
                System.out.println("Main Server path is---->>" + reportPathRs.getString(1));
            }
            int last_index = path.lastIndexOf("/");
            if (last_index == -1) {
                last_index = path.lastIndexOf("\\");
            }
            String serverpath = path.substring(0, last_index + 1);
            System.out.println("the server path is -->" + serverpath);
            
            String DocNo = request.getParameter("DocId").toString();
            String UnitCode = request.getParameter("UnitCd").toString();
            String JasperFileMode = request.getParameter("RepType").toString();
            
            System.out.println("Sale Order No--->>:" + DocNo + " Unit-->>:" + UnitCode + " JasperFileMode-->>:" + JasperFileMode);
            
            String defaultFileName = request.getParameter("defpath").toString();
            System.out.println("Default----->>:" + defaultFileName);
            
            if (!(defaultFileName == null || defaultFileName.equals(""))) 
            {
                defaultFileName = defaultFileName.substring(0, defaultFileName.lastIndexOf("."));
                System.out.println("default file name:" + defaultFileName);
                reportName = defaultFileName;
            } 
            else 
            {
                if (JasperFileMode.equalsIgnoreCase("PO")) 
                {
                    reportName = "r_po_so";
                } else if (JasperFileMode.equalsIgnoreCase("CK")) {
                    reportName = "r_ck_so";
                } else if (JasperFileMode.equalsIgnoreCase("PI")) {
                    reportName = "give pi jasper filename";
                }
            }
            
            Map parameters = new HashMap();
            parameters.put("p_no", DocNo);
            parameters.put("p_unit", UnitCode);
            
            System.out.println("callinf jasper reportName:" + reportName);
            callJasper(reportName, serverpath, parameters, conn, DocNo,response);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                    reportPathRs.close();
                    conn = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
