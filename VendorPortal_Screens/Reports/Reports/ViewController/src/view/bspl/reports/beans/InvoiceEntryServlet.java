package view.bspl.reports.beans;

import java.awt.image.BufferedImage;

import java.io.ByteArrayOutputStream;
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

import javax.imageio.ImageIO;

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
import adf.utils.bean.ADFBeanUtils;

import javax.el.ELContext;
import javax.el.ExpressionFactory;

import javax.el.ValueExpression;
    import oracle.jbo.ApplicationModule;
import javax.faces.application.Application;

@WebServlet(name = "InvoiceEntryServlet", urlPatterns = { "/reports/invoiceentryservlet" })
public class InvoiceEntryServlet extends HttpServlet {
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
                System.out.println("in Try1 connection closed");
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (JRException e) {
            e.printStackTrace();
            try {
                System.out.println("in catch finally connection closed");
                conn.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
        } finally 
        {
            if (conn != null) {
                try {
                    System.out.println("Inside finally connection closed");
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
        PreparedStatement psfile = null;
        ResultSet reportPathRsfile = null;
        PreparedStatement ps = null;
        ResultSet reportPathRs = null;
        String reportName = null;
        String FilePath = null;
        BufferedImage image=null;
        String EmpCode=null;
        String DocNo = request.getParameter("DocId").toString();
        String UnitCode = request.getParameter("UnitCd").toString();
        String InvoiceMode = request.getParameter("RepType").toString();
        String InvParam = request.getParameter("InvParam").toString();
        if(null==request.getParameter("EmpCode")){
             
        }else{
            EmpCode = request.getParameter("EmpCode").toString();
        }
       
        try 
        {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/APPLICATIONDBDS");
            conn = ds.getConnection();

            String path;
            FilePath = null;

//            psfile = conn.prepareStatement("SELECT FILE_PATH FROM VW_DOC_SIGN_DETAIL WHERE EMP_CODE = '"+EmpCode+"' AND UNIT_CD ='"+UnitCode+"'");
//            reportPathRsfile = psfile.executeQuery();            
//            while (reportPathRsfile.next()) 
//            {
//                FilePath = reportPathRsfile.getString(1);
//                System.out.println("FilePath---->>" + reportPathRsfile.getString(1));
//            }




            ps = conn.prepareStatement("SELECT PATH FROM SERVER_PATH WHERE MODULE = 'REPSERVERPATH'");
            reportPathRs = ps.executeQuery();

            path = null;

            //path = "/home/aakash/Jasper Reports/";

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


            System.out.println("Invoice No--->>:" + DocNo + " Unit-->>:" + UnitCode + " Invoice Mode-->>:" +
                               InvoiceMode + " Inv Param-->>:" + InvParam);

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
                if (InvoiceMode.equalsIgnoreCase("PRP")) 
                {
                    reportName = "r_pre_pack";
                } else if (InvoiceMode.equalsIgnoreCase("POP")) {
                    reportName = "r_post_pack";
                } else if (InvoiceMode.equalsIgnoreCase("PRI")) {
                    reportName = "r_pre_inv";
                } else if (InvoiceMode.equalsIgnoreCase("POI")) {
                    reportName = "r_post_inv";
                } else if (InvoiceMode.equalsIgnoreCase("TAI")) {
                    reportName = "tax_invoice";
                } else if (InvoiceMode.equalsIgnoreCase("CO")) {
                    reportName = "r_certi_origin";
                } else if (InvoiceMode.equalsIgnoreCase("DC")) {
                    reportName = "tax_invoice_crdr";
                } else if (InvoiceMode.equalsIgnoreCase("RM")) {
                    reportName = "rm_tax_inv";
                }else if (InvoiceMode.equalsIgnoreCase("CIS")) {
                    reportName = "challan_int_sto";
                }else if (InvoiceMode.equalsIgnoreCase("COM")) {
                                    reportName = "comm_inv";
                                }
                
            }

            Map parameters = new HashMap();
            if (InvoiceMode.equalsIgnoreCase("DC"))
            {
                System.out.println("When Invoice Type Debit Or Credit");
                parameters.put("p_inv_no", DocNo);
                parameters.put("p_unit", UnitCode);
                parameters.put("p_tp", InvParam);
            } else if (InvoiceMode.equalsIgnoreCase("RM")) 
            {
                System.out.println("When Invoice Type is RM--->>");
                parameters.put("p_inv_no", DocNo);
                parameters.put("p_unit", UnitCode);
            }  else if (InvoiceMode.equalsIgnoreCase("COM")) 
            {
                System.out.println("When Invoice Type is RM--->>");
                parameters.put("p_inv_no", DocNo);
                parameters.put("p_unit", UnitCode);
            } 
            else 
            {
                System.out.println("When Invoice Type is NOT Debit Or Credit");
                System.out.println("invoiceNum--->>" + DocNo);
                System.out.println("unitCode--->>" + UnitCode);
                System.out.println("INV HEAD TYPE--->>" + InvParam);
              
                if(FilePath!=null){
              image=   ImageIO.read(new File(FilePath));
                }
                parameters.put("p_inv_no", DocNo);
                parameters.put("p_unit", UnitCode);
                if (InvoiceMode.equalsIgnoreCase("TAI"))
                {
                    
                    parameters.put("p_tp", InvParam);
                    parameters.put("emp_no", EmpCode);
                    parameters.put("image", image);
                }
            }
            System.out.println("callinf jasper reportName:" + reportName);
            callJasper(reportName, serverpath, parameters, conn, DocNo,response);


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    System.out.println("Ever Most finally connection closed");
                    conn.close();
                    reportPathRsfile.close();
                    reportPathRs.close();
                
                    conn = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

