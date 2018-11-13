package com.hc.opc.utgard;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LogTest {

    private static Logger logger = LoggerFactory.getLogger(LogTest.class);


    @Test
    public void test(){
        logger.error("testError:{}","error1");
    }
}
