package com.company.reports.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import com.company.reports.exceptions.InvalidDisplayFormatException;

@Data
@Entity
@Table(name = "invoice")
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    @Id
    @Column(name = "sha_key", length = 64, nullable = false)
    private String shaKey;

    @Column(name = "doc_uuid", length = 36, nullable = false)
    private String docUuid;

    @Column(name = "json_data", nullable = false)
    private String jsonData;

    @Column(name = "html_exists")
    private boolean htmlExists;

    @Column(name = "xls_exists")
    private boolean xlsExists;

    @Column(name = "xlsx_exists")
    private boolean xlsxExists;

    @Column(name = "csv_exists")
    private boolean csvExists;

    @Enumerated(EnumType.STRING)
    @Column(name= "payment_status")
    private PAYMENT_STATUS payment_status;
    
    public boolean hasDisplayFormat(String displayFormat) throws Exception {
        switch (displayFormat.toLowerCase()) {
            case "pdf":
                return true;
            case "html":
                return htmlExists;
            case "xls":
                return xlsExists;
            case "xlsx":
                return xlsxExists;
            case "csv":
                return csvExists;
            default:
                throw new InvalidDisplayFormatException("Invalid display format!");
        }
    }
    public void setDisplayFormat(String displayFormat) throws Exception {
        switch (displayFormat.toLowerCase()) {
            case "pdf":
                // No need to set anything, always true for all invoices
                break;
            case "html":
                htmlExists = true;
                break;
            case "xls":
                xlsExists = true;
                break;
            case "xlsx":
                xlsxExists = true;
                break;
            case "csv":
                csvExists = true;
                break;
            default:
                throw new InvalidDisplayFormatException("Invalid display format!");
        }
    }

    public Invoice(String shaKey,String docUuid, String jsonData) {
    	this.shaKey= shaKey;
    	this.docUuid = docUuid;
        this.jsonData = jsonData;
       }
}
