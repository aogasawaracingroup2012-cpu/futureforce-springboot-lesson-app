package com.lesson.memo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LessonController {

	// 【/greeting】にアクセスしたら、greeting.htmlを返すように記述しましょう
    @GetMapping("/greeting")
    public String greeting(Model model) {
        // モデルにname属性を追加しましょう
        model.addAttribute("name", "太郎");
        return "greeting";
    }

    // 【/users】にアクセスしたら、user-list.htmlを返すように記述しましょう
    @GetMapping("/users")
    public String userList(Model model) {
        // モデルにusers属性を追加しましょう
        model.addAttribute("users", List.of("田中", "佐藤", "鈴木"));
        return "user-list";
    }
}