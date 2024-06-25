package brian.example.camel.bootexamplecamel.config;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class FileCopyConfig extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("file:file-src?noop=true")     // 'noop=true' : Copying and leave source alone
                .to("file:file-dest");
    }
}
