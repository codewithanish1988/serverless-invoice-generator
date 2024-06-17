package org.codewithanish.service;

import org.codewithanish.util.ConfigProperties;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;

public class EmailService {
    public void sendEmail(String to, String customerName, String invoiceUrl) {

        try(SesClient sesClient = SesClient.builder()
                .region(Region.AP_SOUTH_1)
                .build()) {
            SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                    .source(ConfigProperties.getInstance().getProperty("from.email.address"))
                    .destination(Destination.builder()
                            .toAddresses(to)
                            .build())
                    .message(Message.builder()
                            .subject(Content.builder()
                                    .data("Your recent shopping invoice")
                                    .build())
                            .body(Body.builder()
                                    .html(Content.builder()
                                            .data(createBodyContent(customerName, invoiceUrl))
                                            .build())
                                    .build())
                            .build())
                    .build();
           sesClient.sendEmail(sendEmailRequest);
        }
    }

    private String createBodyContent(String customerName, String invoiceUrl)
    {
        return "<html>"
                + "<body>"
                + "<h1>Hello "+customerName+",</h1>"
                + "<p>Thanks for shopping with us. Please click below button to download the invoice of your recent shopping.</p>"
                + "<a style=\""
                + "background-color: orange;"
                + "color: white;"
                + "padding: 14px 25px;"
                + "text-align: center;"
                + "text-decoration: none;"
                + "display: inline-block;\" "
                + "href="+invoiceUrl+">Download Invoice</a>"
                + "</body>"
                + "</html>";
    }
}
