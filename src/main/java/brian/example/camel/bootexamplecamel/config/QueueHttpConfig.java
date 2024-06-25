package brian.example.camel.bootexamplecamel.config;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class QueueHttpConfig extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        // Queue the HTTP request message
        from("direct:queueMessage")
                .log("Send message to queue:${body}")
                .to("activemq:queue:inbound.queue?replyTo=outbound.queue&requestTimeout=5000")
                .log("Received response from queue: ${body}");

        // Processing queue message
        from("activemq:queue:inbound.queue")
                .log("Received from queue:${body}")
                .transform().simple("Processed: ${body}")
//                .to("activemq:queue:outbound.queue");
                .to("seda:endpoint");

        // Returns the processed message
        from("seda:endpoint")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(202))
                .setHeader("Content-Type", constant("text/plain"));
    }
}
