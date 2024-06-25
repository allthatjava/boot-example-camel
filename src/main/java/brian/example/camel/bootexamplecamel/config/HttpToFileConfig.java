package brian.example.camel.bootexamplecamel.config;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class HttpToFileConfig extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:saveToFile")
                .log("Received HTTP request with body: ${body}")
                .process(new HttpToFileProcess())
                .to("file:data/output?fileName=request_"+new Date().getTime()+".txt");
    }
}

class HttpToFileProcess implements Processor{

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = (String)exchange.getIn().getBody();
        body = body + new Date().getTime();

        exchange.getIn().setBody(body);
    }
}
