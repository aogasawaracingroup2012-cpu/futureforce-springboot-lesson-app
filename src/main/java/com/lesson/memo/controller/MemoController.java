package com.lesson.memo.controller;

import java.time.LocalDateTime;

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

	// 【/memo/new】にアクセスしたら、メモ作成画面を表示する機能を記述してください
	@GetMapping("/new")
	//     関数名はshowForm
	public String showForm(Model model) {
		model.addAttribute("memo", new Memo());
		// テンプレートはmemo-form.htmlを使用してください
		return "memo-form";
	}

	// 【/memo/create】にアクセスしたら、メモを作成する機能を記述してください
	@PostMapping("/create")
	// 関数名はcreate
	public String create(@ModelAttribute @Valid Memo memo,
			BindingResult result) {
		// バリデーションエラーをチェックする機能の記述
		if (result.hasErrors()) {
			return "memo-form";
		}

		// メモを保存する機能の記述
		memo.setCreatedAt(LocalDateTime.now());
		memo.setUpdatedAt(LocalDateTime.now());
		memoRepository.save(memo);

		// 【/memo/new】にリダイレクトする記述
		return "redirect:/memo/new";
	}
	

}