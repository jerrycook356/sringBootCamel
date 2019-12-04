package com.learncamel.Route;

import com.learncamel.domain.Item;
import com.learncamel.process.BuildSQLProcessor;
import com.learncamel.process.SuccessProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.logging.Logger;

/**
 * Created by z001qgd on 1/24/18.
 */
@Component
@Slf4j
public class SimpleCamelRoute  extends RouteBuilder{


    @Autowired
    Environment environment;

    @Qualifier("dataSource")
    @Autowired
    DataSource dataSource;

    @Autowired
    BuildSQLProcessor buildSQLProcessor;

    @Autowired
    SuccessProcessor successProcessor;

    @Override
    public void configure() throws Exception {

        log.info("Starting the Camel Route");

        DataFormat bindy = new BindyCsvDataFormat(Item.class);

        from("{{startRoute}}")
                .log("Timer Invoked and the body" + environment.getProperty("message"))
                .choice()  //determine if mock environment
                .when((header("env").isNotEqualTo("mock")))
                //if not poll input directory by timer if directory is not empty
                .pollEnrich("{{fromRoute}}")
                //if mock environment  log message
                .otherwise()
                .log("mock env flow and the body is ${body}")
                .end() //end when
                // move files to this route , and currently delete the original
                .to("{{toRoute1}}")
                //unmarshal the csv in the file to an Item
                .unmarshal(bindy)
                .split(body()) // split into separate records
                   .log("Record is ${body}")
                //buildSQLProcessor parses record and determines which sql action to perform based on
                //transaction type listed in the file
                   .process(buildSQLProcessor)
                //to the postgresql database
                .to("{{toRoute2}}")
                .end() //end split
                 //add processor to verify the data has been inserted into the database.
                .process(successProcessor)
                .to("{{toRoute3}}");

        log.info("Ending the Camel Route");


    }
}
