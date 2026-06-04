package com.lesson.memo.controller;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lesson.memo.model.Memo;
import com.lesson.memo.repository.MemoRepository;

@Controller
@RequestMapping("/memo")
public class MemoController {

    @Autowired
    private MemoRepository memoRepository;

    // メモ一覧を表示する機能を記述してください
    @GetMapping
    // 関数名はlist
     public String list(Model model) {
         // メモ一覧を取得する記述を記述してください。変数名はmemosとしてください。
    	List<Memo> memos = memoRepository.findAll();
         // モデルにmemosを追加する記述を記述してください
    	model.addAttribute("memos", memos);
         // テンプレートはmemo-list.htmlを使用してください
    	return "memo-list";
     }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("memo", new Memo());
        return "memo-form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute @Valid Memo memo,
            BindingResult result) {
        if (result.hasErrors()) {
            return "memo-form";
        }

        memo.setCreatedAt(LocalDateTime.now());
        memo.setUpdatedAt(LocalDateTime.now());
        memoRepository.save(memo);
        // メモ一覧ページへリダイレクトするように記述してください。
        return "redirect:/memo";
    }

}