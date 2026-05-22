package com.lesson.memo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    // 【/hello】にアクセスしたら、Hello, Spring Boot! を返すように記述しましょう
     @GetMapping("hello")
     public String hello() {
         return "Hello, Spring Boot!";
     }
}