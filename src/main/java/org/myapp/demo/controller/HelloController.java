package org.myapp.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//@Controller
@RestController
public class HelloController {
    //@ResponseBody
    @GetMapping("/hello")
    public String Hello() {
        //return "Hello world";
        return "Hello Spring Boot v2.6.6!";
    }
}
