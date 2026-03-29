package com.elastic_app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
public class ElasticController {

    @GetMapping
    public String landingPage() {
        return "ElasticSearch app ";
    }

    @GetMapping("/welcome")
    public String welcomePage() {
        return "welcome you all";
    }
}
