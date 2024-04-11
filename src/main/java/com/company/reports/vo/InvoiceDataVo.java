package com.company.reports.vo;

import java.util.List;

import lombok.Data;

@Data
public class InvoiceDataVo {
    private String buyer;
    private String buyerAddress;
    private String buyerGstin;
    private List<Item> items;
    private String seller;
    private String sellerAddress;
    private String sellerGstin;
}
