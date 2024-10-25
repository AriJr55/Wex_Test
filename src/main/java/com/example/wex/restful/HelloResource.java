package com.example.wex.restful;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloResource {

    @GetMapping("/")
    public @ResponseBody
    String greeting() {
        return "Hello, World";
    }

}
