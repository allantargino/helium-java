package com.microsoft.azure.helium;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sample")
public class TestController {

    private static final Logger logger = LogManager.getLogger(TestController.class);

    @GetMapping("/hello")
    public String hello() {

        //track a custom event  
        logger.info("Sending a custom event...");

        logger.debug("this is a debug");

        logger.trace("this is a trace!");

        return "hello";
    }
}