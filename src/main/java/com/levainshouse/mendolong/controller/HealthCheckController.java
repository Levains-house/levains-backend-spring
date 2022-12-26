package com.levainshouse.mendolong.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/health-check")
@RestController
public class HealthCheckController {

    @GetMapping
    public ResponseEntity<Void> healthCheck(){
        return ResponseEntity.ok().build();
    }
}
