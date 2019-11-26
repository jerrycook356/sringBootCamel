package com.learncamel.Route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class SimpleCamelRoute extends RouteBuilder {


    @Override
    public void configure() throws Exception {
        //camel route using timer. will poll the data/input directory every 10 seconds
        //will copy the contents of the directory to the data/output directory if new files are present
        //will not poll directory if there is no new files,will poll again when there is new files

        from("timer:hello?period=10")
                .log("Timer invoked and the body is ${body}")
                .pollEnrich("file:data/input?delete=true&readLock=none")
                .to("file:data/output");
    }
}
