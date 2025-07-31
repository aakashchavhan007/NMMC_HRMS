package com.nmmc.hrms.Utils;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nmmc.hrms.Entity.Report;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.fill.JRGzipVirtualizer;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@SuppressWarnings("deprecation")
@Component
public class JRReport {
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private Util util;

	
	private static HashMap<String, JasperReport> reportCache = new HashMap<String, JasperReport>();	
	
	//new implementation for LOI
	@SuppressWarnings({ "rawtypes" })
	public byte[] generateReportFromServer(Report report) {
		JasperPrint jasperPrint = null;
		try {
			String jrxmlPath = report.getReportPath();
			String jrxmlName = report.getReportName();
			
			JasperReport jasperReport = null;//reportCache.get(jrxmlPath + jrxmlName);
//			if (jasperReport == null) {
				InputStream inputStream = new FileInputStream(new File(jrxmlPath));
				JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
				jasperReport = JasperCompileManager.compileReport(jasperDesign);
				reportCache.put(jrxmlPath, jasperReport);
//			}
			
			JRGzipVirtualizer virtualizer = new JRGzipVirtualizer(10240);
			report.getInputDataMap().put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
			jasperPrint = generateReport(jasperReport, report.getInputDataMap());
			virtualizer.setReadOnly(true);
			
			if(report.getReportExtension().equals(".pdf")) {
				ByteArrayOutputStream pdfReport = new ByteArrayOutputStream();
				
				JRAbstractExporter pdfExpoter = null;
				pdfExpoter = new net.sf.jasperreports.engine.export.JRPdfExporter();
				
				pdfExpoter.setParameter(JRPdfExporterParameter.JASPER_PRINT,jasperPrint);
				pdfExpoter.setParameter(JRPdfExporterParameter.CHARACTER_ENCODING, "UTF-8");
				pdfExpoter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM,pdfReport);
				pdfExpoter.exportReport();
				pdfReport.flush();
				report.setByteArray(pdfReport.toByteArray());
				pdfReport.close();
			}
			else if(report.getReportExtension().equals(".xls")) {
				ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
				JRXlsExporter xlsExporter = new JRXlsExporter();
				xlsExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				xlsExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, xlsExporter);
				xlsExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, xlsReport);
				
				xlsExporter.setParameter( JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, Boolean.FALSE);
				xlsExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
				xlsExporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS,Boolean.TRUE);
				
				xlsExporter.setParameter( JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
				xlsExporter.setParameter( JRXlsExporterParameter.MAXIMUM_ROWS_PER_SHEET,30000);
				xlsExporter.setParameter( JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.FALSE);
				xlsExporter.exportReport();
				
				xlsReport.flush();
				report.setByteArray(xlsReport.toByteArray());
				xlsReport.close();
			}else if (report.getReportExtension().equals(".xlsx")) {
			    ByteArrayOutputStream xlsxReport = new ByteArrayOutputStream();
			    JRXlsxExporter xlsxExporter = new JRXlsxExporter();
			    xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			    xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsxReport));

			    SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
			    configuration.setOnePagePerSheet(false);
			    configuration.setWhitePageBackground(false);
			    configuration.setDetectCellType(true);
			    configuration.setRemoveEmptySpaceBetweenRows(true);

			    xlsxExporter.setConfiguration(configuration);

			    try {
			        xlsxExporter.exportReport();
			        xlsxReport.flush();
			        report.setByteArray(xlsxReport.toByteArray());
			    } finally {
			        xlsxReport.close();
			    }
			}

			virtualizer.cleanup();
			
		} catch (JRException jrExec) {
			jrExec.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return report.getByteArray();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private JasperPrint generateReport(JasperReport jasperReport, HashMap inputDataMap) throws JRException, SQLException {
		JasperPrint jasperPrint = null;
		Connection con = null;
		try {
			con = dataSource.getConnection();
			System.out.println("Con"+con);
			jasperPrint = JasperFillManager.fillReport(jasperReport, inputDataMap,	con);
			System.out.println("After fill report");
		}
		finally {
			if(con != null)
				con.close();
		}
		return jasperPrint;
	}

	public void exportReportFromServer(Report report, String filePath, String fileName) throws Exception {
		util.convertByteArrayToFile(generateReportFromServer(report), filePath, fileName);	
	}
}
