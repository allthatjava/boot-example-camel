package brian.example.camel.bootexamplecamel.controller;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HttpCamelController {

    @Autowired
    private ProducerTemplate producerTemplate;

    @GetMapping("/saveData")
    public ResponseEntity<String> saveDate(@RequestParam("savingData") String savingData){
        producerTemplate.sendBody("direct:saveToFile", savingData);
        return ResponseEntity.ok("Data saved successfully");
    }

    @GetMapping("/queue")
    public ResponseEntity<String> queueData(@RequestParam("data") String data){
        String result = producerTemplate.requestBody("direct:queueMessage", data, String.class);
        return ResponseEntity.ok(result);
    }
}
