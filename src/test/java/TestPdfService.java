import com.fasterxml.jackson.databind.ObjectMapper;
import org.codewithanish.data.InvoiceDetail;
import org.codewithanish.service.PdfService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestPdfService {
    public void testGeneratePdf() throws IOException {
        String json = "{\n" +
                "  \"invoiceMetaData\": {\n" +
                "    \"isInvoiceGenerated\": false,\n" +
                "    \"invoiceNumber\": \"<Invoice Number>\",\n" +
                "    \"invoiceDate\": \"<Invoice Date>\"\n" +
                "  },\n" +
                "  \"customerDetail\": {\n" +
                "    \"name\": \"<Name>\",\n" +
                "    \"address\": \"<Address Line 1> \\n <Address Line 2> \\n <Address Line 3> \\n <Address Line 4>\",\n" +
                "    \"email\": \"test@gmail.com\"\n" +
                "  },\n" +
                "  \"companyDetail\": {\n" +
                "    \"name\": \"<Company Name>\",\n" +
                "    \"address\": \" <Address Line 1> \\n <Address Line 2> \\n <Address Line 3> \\n <Address Line 4>\"\n" +
                "  },\n" +
                "  \"itemList\": [\n" +
                "    {\n" +
                "      \"slNo\": 1,\n" +
                "      \"itemName\": \"<Item 1>\",\n" +
                "      \"amount\": \"<Amount 1>\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"slNo\": 2,\n" +
                "      \"itemName\": \"<Item 2>\",\n" +
                "      \"amount\": \"<Amount 2>\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"slNo\": 3,\n" +
                "      \"itemName\": \"<Item 3>\",\n" +
                "      \"amount\": \"<Amount3>\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"totalAmount\": \"<Total Amount>\",\n" +
                "  \"totalDiscount\": \"<Total Discount>\"\n" +
                "}";

        InvoiceDetail invoiceDetail = new ObjectMapper().readValue(json, InvoiceDetail.class);
        String filename = "test.pdf";
        File file = new File(filename);
        try (FileOutputStream fos = new FileOutputStream(file)){
            fos.write(new PdfService().generatePdf(invoiceDetail,filename));
        }
    }

    public static void main(String...s)
    {
        try {
            new TestPdfService().testGeneratePdf();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
