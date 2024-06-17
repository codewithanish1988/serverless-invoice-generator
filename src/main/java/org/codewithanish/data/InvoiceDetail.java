package org.codewithanish.data;


import java.util.List;

public record InvoiceDetail(InvoiceMetaData invoiceMetaData, CustomerDetail customerDetail,
                           CompanyDetail companyDetail, List<Item> itemList, String totalAmount,
                            String totalDiscount) {

}
