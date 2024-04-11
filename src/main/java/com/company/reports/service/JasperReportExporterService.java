package com.company.reports.service;

import java.io.IOException;

import net.sf.jasperreports.engine.JRException;

public interface JasperReportExporterService {

    byte[] generatePdfFromJson(String jsonData) throws JRException, IOException;

    byte[] generateHtmlFromJson(String jsonData) throws JRException, IOException;

    byte[] generateXlsFromJson(String jsonData) throws JRException, IOException;

    byte[] generateXlsxFromJson(String jsonData) throws JRException, IOException;

    byte[] generateCsvFromJson(String jsonData) throws JRException, IOException;

}
