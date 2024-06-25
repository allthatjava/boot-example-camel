### Apache Camel with Spring Boot example

#### File integration
```java
    public void configure() throws Exception {
        from("file:file-src?noop=true")     // 'noop=true' : Copying and leave source alone
                .to("file:file-dest");
    }
```

#### Http to File integration
- HTTP End Point
```java
    @GetMapping("/saveData")
    public ResponseEntity<String> saveDate(@RequestParam("savingData") String savingData){
        producerTemplate.sendBody("direct:saveToFile", savingData);
        return ResponseEntity.ok("Data saved successfully");
    }
```

- For example purpose, processor class has been redefined
```java
    @Override
    public void configure() throws Exception {
        from("direct:saveToFile")
                .log("Received HTTP request with body: ${body}")
                .process(new HttpToFileProcess())
                .to("file:data/output?fileName=request_"+new Date().getTime()+".txt");
    }

class HttpToFileProcess implements Processor{

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = (String)exchange.getIn().getBody();
        body = body + new Date().getTime();

        exchange.getIn().setBody(body);
    }
}    
```

#### HTTP to Queue example
- HTTP End Point
```java
    @GetMapping("/queue")
    public ResponseEntity<String> queueData(@RequestParam("data") String data){
        String result = producerTemplate.requestBody("direct:queueMessage", data, String.class);
        return ResponseEntity.ok(result);
    }
```

- Configuration
```java
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
```