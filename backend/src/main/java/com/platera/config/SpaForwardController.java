package com.platera.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaForwardController {

    @GetMapping("/{path:[^\\.]*}")
    public String forwardSingleLevel() {
        return "forward:/index.html";
    }

    @GetMapping("/**/{path:[^\\.]*}")
    public String forwardMultiLevel() {
        return "forward:/index.html";
    }
}