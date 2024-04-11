package com.company.reports.service.impl;

import static com.company.reports.utils.InvoiceConstants.DISPLAY_FORMAT_DIRECTORY_CSV;
import static com.company.reports.utils.InvoiceConstants.DISPLAY_FORMAT_DIRECTORY_HTML;
import static com.company.reports.utils.InvoiceConstants.DISPLAY_FORMAT_DIRECTORY_PDF;
import static com.company.reports.utils.InvoiceConstants.DISPLAY_FORMAT_DIRECTORY_XLSX;
import static com.company.reports.utils.JasperReportConstants.DISPLAY_FORMAT_CSV;
import static com.company.reports.utils.JasperReportConstants.DISPLAY_FORMAT_HTML;
import static com.company.reports.utils.JasperReportConstants.DISPLAY_FORMAT_PDF;
import static com.company.reports.utils.JasperReportConstants.DISPLAY_FORMAT_XLS;
import static com.company.reports.utils.JasperReportConstants.DISPLAY_FORMAT_XLSX;

import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.company.reports.exceptions.InvalidDisplayFormatException;
import com.company.reports.model.Invoice;
import com.company.reports.repo.InvoiceRepo;
import com.company.reports.service.InvoiceService;
import com.company.reports.service.JasperReportExporterService;
import com.company.reports.utils.CompanyIOUtils;
import com.company.reports.utils.HashCodeGenerator;
import com.company.reports.vo.InvoiceDataVo;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class InvoiceServiceImpl implements InvoiceService {
	@Autowired
	private InvoiceRepo invoiceRepo;

	@Autowired
	JasperReportExporterService reportExporter;
	
	@Autowired 
	CompanyIOUtils companyIOUtils;

	@Value("${company.reports.location}")
	private String invoicesLocation;

	@Override
	public String generateInvoice(InvoiceDataVo invoiceDataVo) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String invoiceData = mapper.writeValueAsString(invoiceDataVo);
		String hashKey = HashCodeGenerator.generateSHA256HashCode(invoiceData);
		Optional<Invoice> optional = invoiceRepo.findById(hashKey);
		Invoice invoice = null;
		if (optional.isPresent()) {
			invoice = optional.get();
			return invoice.getDocUuid();
		}
		String docUuid = String.valueOf(UUID.randomUUID());
		byte[] pdfBytes = reportExporter.generatePdfFromJson(invoiceData);
		companyIOUtils.createFileFromByteArray(pdfBytes, invoicesLocation, DISPLAY_FORMAT_DIRECTORY_PDF, docUuid,
				".pdf");
		invoice = new Invoice(hashKey, docUuid, invoiceData);
		invoiceRepo.save(invoice);
		return invoice.getDocUuid();
	}

	@Override
	public byte[] getPdfDocument(String docUuid) throws Exception {
		byte[] pdfBytes = companyIOUtils.readFileAsBytes(invoicesLocation, DISPLAY_FORMAT_DIRECTORY_PDF, docUuid,
				".pdf");
		return pdfBytes;
	}

	@Override
	public byte[] getDocument(String docUuid, String displayFormat) throws Exception {

		if (!isValidDisplayFormat(displayFormat)) {
			throw new InvalidDisplayFormatException("Unsupported display format: " + displayFormat);
		}
		Optional<Invoice> invoiceOptional = invoiceRepo.findByDocUuid(docUuid);
		if (invoiceOptional.isEmpty()) {
			throw new FileNotFoundException("Document not found");
		}
		Invoice invoice = invoiceOptional.get();
		if (!invoice.hasDisplayFormat(displayFormat)) {
			String jsonData = invoice.getJsonData();
			generateAndSaveReport(docUuid, displayFormat, jsonData);
			invoice.setDisplayFormat(displayFormat);
			invoiceRepo.save(invoice);
		}

		return getDocumentByFormat(docUuid, displayFormat);
	}

	private byte[] getDocumentByFormat(String docUuid, String format) throws Exception {
		switch (format.toLowerCase()) {
		case DISPLAY_FORMAT_PDF:
			return companyIOUtils.readFileAsBytes(invoicesLocation, DISPLAY_FORMAT_DIRECTORY_PDF, docUuid, ".pdf");
		case DISPLAY_FORMAT_HTML:
			return companyIOUtils.readFileAsBytes(invoicesLocation, DISPLAY_FORMAT_DIRECTORY_HTML, docUuid, ".html");
		case DISPLAY_FORMAT_XLSX:
			return companyIOUtils.readFileAsBytes(invoicesLocation, DISPLAY_FORMAT_DIRECTORY_XLSX, docUuid, ".xlsx");
		case DISPLAY_FORMAT_CSV:
			return companyIOUtils.readFileAsBytes(invoicesLocation, DISPLAY_FORMAT_DIRECTORY_CSV, docUuid, ".csv");
		default:
			throw new InvalidDisplayFormatException("Unsupported display format: " + format);
		}
	}

	private void generateAndSaveReport(String docUuid, String displayFormat, String jsonData) throws Exception {
		byte[] fileBytes = null;
		if (DISPLAY_FORMAT_PDF.equals(displayFormat)) {
			fileBytes = reportExporter.generatePdfFromJson(jsonData);
			companyIOUtils.createFileFromByteArray(fileBytes, invoicesLocation, DISPLAY_FORMAT_DIRECTORY_PDF, docUuid,
					".pdf");
		} else if (DISPLAY_FORMAT_HTML.equals(displayFormat)) {
			fileBytes = reportExporter.generateHtmlFromJson(jsonData);
			companyIOUtils.createFileFromByteArray(fileBytes, invoicesLocation, DISPLAY_FORMAT_DIRECTORY_HTML, docUuid,
					".html");
		} else if (DISPLAY_FORMAT_XLSX.equals(displayFormat)) {
			fileBytes = reportExporter.generateXlsxFromJson(jsonData);
			companyIOUtils.createFileFromByteArray(fileBytes, invoicesLocation, DISPLAY_FORMAT_DIRECTORY_XLSX, docUuid,
					".xlsx");
		} else if (DISPLAY_FORMAT_CSV.equals(displayFormat)) {
			fileBytes = reportExporter.generateCsvFromJson(jsonData);
			companyIOUtils.createFileFromByteArray(fileBytes, invoicesLocation, DISPLAY_FORMAT_DIRECTORY_CSV, docUuid,
					".csv");
		} else {
			throw new InvalidDisplayFormatException("Unsupported display format: " + displayFormat);
		}
	}

	private boolean isValidDisplayFormat(String displayFormat) {
		return DISPLAY_FORMAT_PDF.equals(displayFormat) || DISPLAY_FORMAT_HTML.equals(displayFormat)
				|| DISPLAY_FORMAT_XLS.equals(displayFormat) || DISPLAY_FORMAT_XLSX.equals(displayFormat)
				|| DISPLAY_FORMAT_CSV.equals(displayFormat);
	}

}
