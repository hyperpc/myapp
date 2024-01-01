package org.myapp.demo.controller;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HelloControllerTest {
    @Test
    void HelloTest(){
        var resp = new HelloController().Hello();
        assertEquals(resp, "Hello Spring Boot v2.6.6!");
    }
}