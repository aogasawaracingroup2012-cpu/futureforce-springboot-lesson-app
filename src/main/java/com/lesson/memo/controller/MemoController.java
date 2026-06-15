package com.lesson.memo.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lesson.memo.model.Memo;
import com.lesson.memo.repository.MemoRepository;

@Controller
@RequestMapping("/memo")
public class MemoController {

    @Autowired
    private MemoRepository memoRepository;

    @GetMapping
    public String list(Model model) {
        List<Memo> memos = memoRepository.findAll();
        model.addAttribute("memos", memos);
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
        return "redirect:/memo";
    }

    @GetMapping("/detail/{id}")
    public String showDetail(@PathVariable Long id, Model model,
            HttpServletResponse response) {
        Optional<Memo> memo = memoRepository.findById(id);
        if (memo.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "not-found"; // エラー画面にリダイレクト
        }

        model.addAttribute("memo", memo.get());
        return "memo-detail";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, HttpServletResponse response) {
        if (model.containsAttribute("memo")) {
            return "memo-form";
        }

        return memoRepository.findById(id)
                .map(memo -> {
                    model.addAttribute("memo", memo);
                    return "memo-form";
                })
                .orElseGet(() -> {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    return "not-found";
                });
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
            @ModelAttribute @Valid Memo memo,
            BindingResult result,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes) {

        Optional<Memo> opt = memoRepository.findById(id);
        if (opt.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "not-found"; // エラー画面表示
        }

        Memo memoToUpdate = opt.get();

        if (result.hasErrors()) {
            // 入力内容とエラー情報をFlashスコープに保存
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.memo", result);
            redirectAttributes.addFlashAttribute("memo", memo);
            return "redirect:/memo/edit/" + id; // editにリダイレクト
        }

        // 入力内容を既存エンティティに反映
        memoToUpdate.setTitle(memo.getTitle());
        memoToUpdate.setContent(memo.getContent());
        memoToUpdate.setUpdatedAt(LocalDateTime.now());

        memoRepository.save(memoToUpdate);

        return "redirect:/memo/detail/" + id;
    }

    // 【/memo/delete/{id}】にアクセスしたら、メモを削除する機能を記述してください
     @GetMapping("/delete/{id}")
    // 関数名はdelete
     public String delete(@PathVariable Long id, HttpServletResponse response) {
         // メモを削除する記述を記述してください。
    	 if(memoRepository.existsById(id)) { // findByID(id)はあればOptional[Memo]を返しなければOptional.emptyを返すが、existsById(id)はあるかどうかのみを真偽で返す。
    		memoRepository.deleteById(id);
    	 } else {
    		 response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    		 return "not-found";
    	 }
         // メモ一覧画面にリダイレクトする記述を記述してください。
         return "redirect:/memo";
     }
}