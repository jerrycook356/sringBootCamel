package com.learncamel.route;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;

import static junit.framework.TestCase.assertTrue;

@ActiveProfiles("dev") //gets profile from application.yml
@RunWith(CamelSpringBootRunner.class)
//clears the context cache after each test run
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)

@SpringBootTest
public class SimpleCamelRouteTest  {

    @Autowired
    ProducerTemplate producerTemplate;

    @Autowired
    Environment environment;

    //runs before the class and cleans up the directories before the test is run.
    @BeforeClass
    public static void startCleanUp() throws IOException {
        FileUtils.cleanDirectory(new File("data/input"));
        FileUtils.deleteDirectory(new File("data/output"));
    }

    @Test
    public void testMoveFile() throws InterruptedException{
        String message = "type, sku#, itemDesciption, price\n"
                +"ADD, 100, Samsung TV, 500\n"
                +"ADD, 101 LG TV, 500";
        String fileName = "testFile.txt";
        //creates file testFile.txt with the content of message.
        //then runs the camel route
        producerTemplate.sendBodyAndHeader(environment.getProperty("fromRoute"),
                message, Exchange.FILE_NAME,fileName);
        Thread.sleep(3000);
        //creates the file outfile
        File outFile = new File("data/output/"+fileName);
        //checks that the file outfile exists, means it was transferred from the data/input directory
        assertTrue(outFile.exists());
    }
}
