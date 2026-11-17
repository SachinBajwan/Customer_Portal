package view.bspl.reports.beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXhtmlExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@WebServlet(name = "DispatchAdviceServlet", urlPatterns = { "/reports/dispatchadviceservlet" })
public class DispatchAdviceServlet extends HttpServlet {
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
            String fileName = "Invoice_" + DocNo + ".pdf";
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
                // System.out.println("in Try1 connection closed");
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (JRException e) {
            e.printStackTrace();
            try {
                // System.out.println("in catch finally connection closed");
                conn.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
        } finally 
        {
            if (conn != null) {
                try {
                    // System.out.println("Inside finally connection closed");
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

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType(CONTENT_TYPE);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet reportPathRs = null;
        try 
        {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/APPLICATIONDBDS");
            conn = ds.getConnection();
            String reportName = null;

            String path;

            ps = conn.prepareStatement("SELECT PATH FROM SERVER_PATH WHERE MODULE = 'REPSERVERPATH'");
            reportPathRs = ps.executeQuery();

            path = null;

            //path = "/home/aakash/Jasper Reports/";

            while (reportPathRs.next()) {
                path = reportPathRs.getString(1);
                // System.out.println("Main Server path is---->>" + reportPathRs.getString(1));
            }
            int last_index = path.lastIndexOf("/");
            if (last_index == -1) {
                last_index = path.lastIndexOf("\\");
            }
            String serverpath = path.substring(0, last_index + 1);
            // System.out.println("the server path is -->" + serverpath);
            
            String defaultFileName = request.getParameter("defpath").toString();
            String DocNo = request.getParameter("DocId").toString();
            String UnitCode = request.getParameter("UnitCd").toString();
            String ReportMode = request.getParameter("RepType").toString();
            String DispParam = request.getParameter("DispParam").toString();
            String LocCode = " ";
            if(ReportMode.equalsIgnoreCase("PL")){
             LocCode = request.getParameter("LocCode").toString();
            }
            // System.out.println("Dispatch Advice No--->>:" + DocNo + " Unit-->>:" + UnitCode + " Report Mode-->>:" +ReportMode + " Inv Param-->>:" + DispParam);
            // System.out.println("Default----->>:"+defaultFileName);
            
            if (!(defaultFileName == null || defaultFileName.equals(""))) 
            {
                defaultFileName = defaultFileName.substring(0, defaultFileName.lastIndexOf("."));
                // System.out.println("default file name:" + defaultFileName);
                reportName = defaultFileName;
            } 
            else 
            {
                    if (ReportMode.equalsIgnoreCase("SN")) 
                    {
                        reportName = "sale_note";
                    }
            }


                // Map<String, Object> parameters = new HashMap<String, Object>();
                Map parameters = new HashMap();
                if (ReportMode.equalsIgnoreCase("SN")) {
                    parameters.put("p_da_no", DocNo);
                    parameters.put("p_unit", UnitCode);
                    parameters.put("p_sp", DispParam);
                
                }
            if (ReportMode.equalsIgnoreCase("PL")) {
                parameters.put("p_da_no", DocNo);
                parameters.put("p_unit", UnitCode);
                //parameters.put("p_sp", DispParam);
                parameters.put("LocCode", LocCode);
            
            }
                /* If we want to pass the file as .jsaper , use below two lines*/
                File repFile = new File(serverpath + reportName + ".jasper");
                JasperReport report = (JasperReport) JRLoader.loadObject(repFile);


                /* If we want to pass the file as .jrxml , use below two lines*/

                /*             InputStream input = new FileInputStream(new File(serverpath + reportName + ".jrxml"));
                JasperDesign design = JRXmlLoader.load(input);
                JasperReport report = JasperCompileManager.compileReport(design); */

                JasperPrint jasperPrint = null;
                jasperPrint = JasperFillManager.fillReport(report, parameters, conn);
                OutputStream ouputStream = response.getOutputStream();
                String fileName = "DispatchAdvice_" + DocNo + ".pdf";
                JRExporter exporter = null;
                if (true) 
                {
                    response.setContentType("application/pdf");
                    response.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");
                    exporter = new JRPdfExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);
                }

                try 
                {
                    exporter.exportReport();
                    String str = "<script>window.onload= function () { window.print();window.close();   }  </script>";
                    ouputStream.write(str.getBytes());
                    try 
                    {
                        // System.out.println("in Try1 connection closed");
                        conn.close( );
                        reportPathRs.close();
                    } catch( SQLException e ) {
                        e.printStackTrace( );
                    }
                } catch (JRException e) 
                {
                    e.printStackTrace();
                    try 
                    {
                        // System.out.println("in catch finally connection closed");
                        conn.close( );
                        reportPathRs.close();
                    } catch( SQLException e1 ) {
                        e1.printStackTrace( );
                    }
                } 
                finally 
                {
                    try 
                    {
                        if (conn != null) 
                        {
                            try 
                            {
                                // System.out.println("Inside finally connection closed");
                                conn.close();
                                reportPathRs.close();
                            } catch (SQLException e) {
                                }
                        }
                        if (ouputStream != null) 
                        {
                            // System.out.println("hhdhdhdh");
                            ouputStream.flush();
                            ouputStream.close();
                        }
                    } catch (IOException ex) {
                        throw (ex);
                    }
                }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally 
        {
            try 
            {
                if(conn!=null)
                {
                    // System.out.println("Ever Most finally connection closed");
                    conn.close( );
                    reportPathRs.close();
                    conn = null; 
                }
            } catch( SQLException e ) {
                    e.printStackTrace( );
            }
        }
    }
}


