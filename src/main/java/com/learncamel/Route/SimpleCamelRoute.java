package com.learncamel.Route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class SimpleCamelRoute extends RouteBuilder {

    @Autowired
    Environment environment;

    @Override
    public void configure() throws Exception {
        //camel route using timer. will poll the data/input directory every 10 seconds
        //will copy the contents of the directory to the data/output directory if new files are present
        //will not poll directory if there is no new files,will poll again when there is new files
        from("{{startRoute}}")
                .log("Timer invoked and the body "+ environment.getProperty("message"))
                //choice for whether is mock or not
                .choice()
                .when((header("env").isNotEqualTo("mock")))
                .pollEnrich("{{fromRoute}}")
                .otherwise()
                .log("mock env flow and body is  ${body}")
                .to("{{toRoute1}}");
    }
}
