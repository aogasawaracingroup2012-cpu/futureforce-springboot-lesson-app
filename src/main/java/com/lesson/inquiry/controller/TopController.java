package com.lesson.inquiry.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TopController {

    @GetMapping("/")
    public String top() {
        return "Top Page";
    }
}