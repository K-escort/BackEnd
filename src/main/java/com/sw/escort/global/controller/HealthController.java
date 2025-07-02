package com.sw.escort.global.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @Deprecated
    @GetMapping("/health")
    public String health() {
        return "ㅎㅇ";
    }

}
