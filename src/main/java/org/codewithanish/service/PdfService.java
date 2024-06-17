package org.codewithanish.service;

import com.sughelp.pdf.generator.Column;
import com.sughelp.pdf.generator.PdfGenerator;
import com.sughelp.pdf.generator.Row;
import com.sughelp.pdf.generator.Table;
import com.sughelp.pdf.generator.Template;
import com.sughelp.pdf.generator.constants.PdfConstants;
import org.codewithanish.data.CompanyDetail;
import org.codewithanish.data.CustomerDetail;
import org.codewithanish.data.InvoiceDetail;
import org.codewithanish.data.InvoiceMetaData;
import org.codewithanish.data.Item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdfService {

    public byte[] generatePdf(InvoiceDetail invoiceDetail, String fileName) throws IOException {
        try(PdfGenerator pdfGenerator = new PdfGenerator()) {
           return pdfGenerator.createPdfByteArray(generateTemplate(invoiceDetail), fileName);
        }
    }

    private Template generateTemplate(InvoiceDetail invoiceDetail)
    {
        Template template = new Template();
        template.setTables(List.of(
                generateCompanyDetailTable(invoiceDetail.companyDetail()),
                generateCustomerDetailTable(invoiceDetail.customerDetail(), invoiceDetail.invoiceMetaData()),
                generateItemTable(invoiceDetail.itemList()),
                generateSingleValueTable("Discount : "+ invoiceDetail.totalDiscount()),
                generateSingleValueTable("Total Amount : " + invoiceDetail.totalAmount())
        ));
        return template;
    }

    private Table generateCompanyDetailTable(CompanyDetail companyDetail)
    {
        Table table = new Table();
        table.setTotalColumnCount(1);
        table.setRows(List.of(generateCompanyNameRow(companyDetail.name()), generateCompanyAddressRow(companyDetail.address())));
        return  table;
    }

    private Row generateCompanyNameRow(String companyName)
    {
        Row row = new Row();
        row.setIsHeader(true);
        row.setDrawBottomLine(true);
        row.setLineColorComponents(new float[]{255f, 165f, 0f});
        row.setColumns(List.of(generateCompanyNameColumn(companyName)));
        return row;
    }

    private Row generateCompanyAddressRow(String companyAddress)
    {
        Row row = new Row();
        row.setDrawBottomLine(true);
        row.setLineColorComponents(new float[]{255f, 165f, 0f});
        row.setColumns(List.of(generateCompanyAddressColumn(companyAddress)));
        return row;
    }

    private Column generateCompanyNameColumn(String companyName)
    {
        Column column = new Column();
        column.setText(companyName);
        column.setFontSize(15f);
        column.setHorizontalGravity(PdfConstants.COLUMN_GRAVITY_CENTER.getValue());
        return column;
    }

    private Column generateCompanyAddressColumn(String companyAddress)
    {
        Column column = new Column();
        column.setIsItalic(true);
        column.setText(companyAddress);
        column.setFontSize(10f);
        column.setHorizontalGravity(PdfConstants.COLUMN_GRAVITY_CENTER.getValue());
        return column;
    }

    private Table generateCustomerDetailTable(CustomerDetail customerDetail, InvoiceMetaData invoiceMetaData)
    {
        Table table = new Table();
        table.setTopMargin(10f);
        table.setTotalColumnCount(2);
        table.setRows(List.of(
                generateCustomerInfoRow("Billed To :", "Invoice Number : "+invoiceMetaData.invoiceNumber()),
                generateCustomerInfoRow(customerDetail.name(), "Invoice Date : "+invoiceMetaData.invoiceDate()),
                generateCustomerInfoRow(customerDetail.address(), "")));
        return  table;
    }

    private Row generateCustomerInfoRow(String col1Value, String col2Value)
    {
        Row row = new Row();
        Column column1 = new Column();
        column1.setText(col1Value);
        column1.setFontSize(10f);
        Column column2 = new Column();
        column2.setText(col2Value);
        column2.setFontSize(10f);
        column2.setHorizontalGravity(PdfConstants.COLUMN_GRAVITY_RIGHT.getValue());
        row.setColumns(List.of(column1, column2));
        return row;
    }

    private Table generateItemTable(List<Item> invoiceDetails)
    {
        Table table = new Table();
        table.setTopMargin(10f);
        table.setDrawBoundary(true);
        table.setTotalColumnCount(3);
        List<Row> rows = new ArrayList<>();
        rows.add(createItemHeader());
        rows.addAll(invoiceDetails.stream().map(this::createItemRow).toList());
        table.setRows(rows);
        return table;
    }

    private Row createItemHeader()
    {
        Row row = new Row();
        row.setDrawBottomLine(true);
        row.setIsHeader(true);
        row.setColumns(List.of(
                createItemNormalColumn("Sl No.", true),
                createItemNormalColumn("Item Name", true),
                createItemAmountColumn("Amount", true)));
        return row;
    }

    private Row createItemRow(Item item)
    {
        Row row = new Row();
        row.setDrawBottomLine(true);
        row.setColumns(List.of(
                createItemNormalColumn(String.valueOf(item.slNo()), false),
                createItemNormalColumn(item.itemName(), false),
                createItemAmountColumn(item.amount(), false)));
        return row;
    }

    private Column createItemNormalColumn(String text, boolean isHeader)
    {
        Column column = new Column();
        column.setText(text);
        column.setFontSize(10f);
        if(isHeader) {
            column.setTextColorComponents(new float[]{240f, 128f, 0f});
            column.setFontSize(12f);
        }
        return column;
    }

    private Column createItemAmountColumn(String text, boolean isHeader)
    {
        Column column = createItemNormalColumn(text,isHeader);
        column.setHorizontalGravity(PdfConstants.COLUMN_GRAVITY_RIGHT.getValue());
        return column;
    }

    private Table generateSingleValueTable(String value)
    {
        Table table = new Table();
        table.setTotalColumnCount(1);
        Row row = new Row();
        Column column = new Column();
        column.setText(value);
        column.setIsBold(true);
        column.setTextColorComponents(new float[]{240f, 128f, 0f});
        column.setFontSize(12f);
        column.setHorizontalGravity(PdfConstants.COLUMN_GRAVITY_RIGHT.getValue());
        row.setColumns(List.of(column));
        table.setRows(List.of(row));
        return  table;
    }
}
