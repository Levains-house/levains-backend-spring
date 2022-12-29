package com.levainshouse.mendolong.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/health-check")
@RestController
public class HealthCheckController {

    @GetMapping
    public String healthCheck(){
        return "ok";
    }
}
