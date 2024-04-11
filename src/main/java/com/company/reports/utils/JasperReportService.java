package com.company.reports.utils;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;

@Service
public class JasperReportService {
	
	private JasperReport jasperReport;
	
	@Value("${company.reports.jrxml.path}")
	private String jrxmlFilePath;
	public JasperReport getJasperReport() throws IOException {
		if (jasperReport == null) {
			try {
				FileSystemResource jrxmlResource = null;
				jrxmlResource = new FileSystemResource(jrxmlFilePath);
				jasperReport = JasperCompileManager.compileReport(jrxmlResource.getInputStream());
			} catch (JRException e) {
				throw new RuntimeException("Error loading and compiling the JasperReport", e);
			}
		}
		return jasperReport;
	}
	








}
