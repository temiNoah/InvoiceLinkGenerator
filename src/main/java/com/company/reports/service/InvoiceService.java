package com.company.reports.service;

import com.company.reports.vo.InvoiceDataVo;

public interface InvoiceService {
	
	public String generateInvoice(InvoiceDataVo invoiceDataVo) throws Exception;
	
	public byte[] getPdfDocument(String docUuid) throws Exception;
	
	public byte[] getDocument(String docUuid, String displayFormat)throws Exception ;
}
