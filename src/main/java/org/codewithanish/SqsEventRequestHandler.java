package org.codewithanish;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.codewithanish.data.InvoiceDetail;
import org.codewithanish.service.EmailService;
import org.codewithanish.service.PdfService;
import org.codewithanish.service.S3Service;

public class SqsEventRequestHandler implements RequestHandler<SQSEvent, Void> {

    private Context context;

    @Override
    public Void handleRequest(SQSEvent sqsEvent, Context context) {
        this.context = context;
        for (SQSMessage msg : sqsEvent.getRecords()) {
            processMessage(msg);
        }
        return null;
    }

    private void processMessage(SQSMessage msg) {
        try {
            context.getLogger().log("Processing Invoice Detail message " + msg.getBody());
            InvoiceDetail invoiceDetail = new ObjectMapper().readValue(msg.getBody(), InvoiceDetail.class);
            sendEmail(invoiceDetail, getInvoiceUrl(invoiceDetail));
        } catch (Exception e) {
            context.getLogger().log("An error occurred :: "+e.getMessage());
        }
    }

    private String getInvoiceUrl(InvoiceDetail invoiceDetail) throws Exception {
        try(S3Service s3Service = new S3Service()) {
            String fileName = invoiceDetail.invoiceMetaData().invoiceNumber() + ".pdf";
            String url = invoiceDetail.invoiceMetaData().isInvoiceGenerated() ?
                    s3Service.getUrl(fileName) :
                    s3Service.uploadContent(fileName,
                            new PdfService().generatePdf(invoiceDetail, fileName));
            context.getLogger().log("Invoice Url = " + url);
            return url;
        }
    }

    private void sendEmail(InvoiceDetail invoiceDetail, String invoiceUrl)
    {
        EmailService emailService = new EmailService();
        emailService.sendEmail(invoiceDetail.customerDetail().email(), invoiceDetail.customerDetail().name(), invoiceUrl);
        context.getLogger().log("Email send successfully!!!");
    }

}
