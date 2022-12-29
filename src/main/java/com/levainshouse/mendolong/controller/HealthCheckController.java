package com.levainshouse.mendolong.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/health-check")
@Controller
public class HealthCheckController {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String healthCheck(){
        return "ok";
    }
}
