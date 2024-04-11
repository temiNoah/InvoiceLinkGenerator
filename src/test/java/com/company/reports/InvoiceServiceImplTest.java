package com.company.reports;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.company.reports.exceptions.InvalidDisplayFormatException;
import com.company.reports.model.Invoice;
import com.company.reports.repo.InvoiceRepo;
import com.company.reports.service.impl.InvoiceServiceImpl;
import com.company.reports.service.impl.JasperReportExporterServiceImpl;
import com.company.reports.utils.CompanyIOUtils;
import com.company.reports.utils.HashCodeGenerator;
import com.company.reports.vo.InvoiceDataVo;
import com.company.reports.vo.Item;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)

public class InvoiceServiceImplTest {

    @Mock
    private InvoiceRepo invoiceRepo;

    @Mock
    private JasperReportExporterServiceImpl reportExporter;
    
    @Mock
    CompanyIOUtils companyIOUtils;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    private InvoiceDataVo invoiceDataVo;
    
    @Before
    public void setUp() {
    	InvoiceDataVo invoice = new InvoiceDataVo();

    	invoice.setBuyer("John Doe");
    	invoice.setBuyerAddress("123 Main St, Anytown USA");
    	invoice.setBuyerGstin("GSTIN1234");
    	
    	invoice.setSeller("Jane Smith");
    	invoice.setSellerAddress("456 Broad St, Anytown USA");
    	invoice.setSellerGstin("GSTIN5678");
    	
    	List<Item> itemList = new ArrayList<>();
    	Item item1 = new Item();
    	item1.setName("Item 1");
    	item1.setAmount(100.1);
    	item1.setQuantity("2");
    	item1.setRate(1234);
    	itemList.add(item1);

    	Item item2 = new Item();
    	item1.setName("Item 1");
    	item1.setAmount(100.1);
    	item1.setQuantity("2");
    	item1.setRate(1234);
    	itemList.add(item2);
    	invoice.setItems(itemList);


    }

    @Test
    public void generateInvoice_shouldReturnValidDocUuid_whenHashCodeKeyExistsForJsonInput() throws Exception {
        Invoice invoice = new Invoice("hashKey", "docUuid", "invoiceData");
        ObjectMapper mapper = new ObjectMapper();
		String invoiceData = mapper.writeValueAsString(invoiceDataVo);
        String hashKey = HashCodeGenerator.generateSHA256HashCode(invoiceData);
        when(invoiceRepo.findById(hashKey)).thenReturn(Optional.of(invoice));
        String result = invoiceService.generateInvoice(invoiceDataVo);
        assertEquals("docUuid", result);
    }

    @Test
    public void generateInvoice_shouldReturnValidDocUuid_whenHashCodeKeyDoesnotExistsForJsonInput() throws Exception {
        when(invoiceRepo.findById(anyString())).thenReturn(Optional.empty());
        when(reportExporter.generatePdfFromJson(anyString())).thenReturn(new byte[] {});
        
        String result = invoiceService.generateInvoice(invoiceDataVo);
        assertNotNull(result);
    }
    
    @Test
	public void getDocument_shouldReturnExpectedBytes_whenGetsBytesFromCommonIOUtils() throws Exception {
		String docUuid = "123";
		String displayFormat = "pdf";
		byte[] expectedBytes = new byte[] {1, 2, 3};
		Invoice invoice = new Invoice("hashKey", "docUuid", "invoiceData");
		when(invoiceRepo.findByDocUuid(docUuid)).thenReturn(Optional.of(invoice));
		
		when(companyIOUtils.readFileAsBytes(Mockito.isNull(), anyString(), anyString(),anyString())).thenReturn(expectedBytes);
		
		byte[] actualBytes = invoiceService.getDocument(docUuid, displayFormat);
		
		assertArrayEquals(expectedBytes, actualBytes);
	}
    @Test(expected = InvalidDisplayFormatException.class)
    public void getDocument_shouldThrowInvalidDisplayFormatException_whenInvalidDisplayFormatIsPassed() throws Exception {
        String docUuid = "1234";
        String invalidFormat = "invalid-format";    
        invoiceService.getDocument(docUuid, invalidFormat);
    }
    
    @Test(expected = Exception.class)
    public void getDocument_shouldThrowException_whenInvalidDocumentIdIsPassed() throws Exception {
        String invalidDocUuid = "1234";
        String invalidFormat = "pdf";
        when(invoiceRepo.findByDocUuid(invalidDocUuid)).thenReturn(Optional.empty());
        invoiceService.getDocument(invalidDocUuid, invalidFormat);
    }
}
