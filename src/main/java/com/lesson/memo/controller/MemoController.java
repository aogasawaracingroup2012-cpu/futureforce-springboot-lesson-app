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

	// 【/memo/detail/{id}】にアクセスしたら、メモ詳細画面を表示する機能を記述してください
	@GetMapping("/detail/{id}")
	// 関数名はshowDetail
	public String showDetail(@PathVariable Long id, Model model, HttpServletResponse response) {
		// メモを取得する記述を記述してください。変数名はmemoとしてください。
		Optional<Memo> memo = memoRepository.findById(id);
		// メモがない場合はnot-found画面にHTTPステータスを404にして、リダイレクトする記述を記述してください。
		if (memo.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return "not-found";
		}
		// メモをモデルに追加する記述を記述してください。変数名はmemoとしてください。
		model.addAttribute("memo", memo.get());
		// テンプレートはmemo-detail.htmlを使用してください。
		return "memo-detail";

		//上記をチェーンメソッドで.map()を使って短く書くと下記記述になる
//		return memoRepository.findById(id)
//			.map(memo -> {
//				model.addAttribute("memo", memo);
//				return "memo-detail";
//			})
//			.orElseGet(() -> {
//				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//				return "not-found";
//			});
	}

	// 【/memo/edit/{id}】にアクセスしたら、メモ編集画面を表示する機能を記述してください
	@GetMapping("/edit/{id}")
    // 関数名はshowEditForm
     public String showEditForm(@PathVariable Long id, Model model, HttpServletResponse response) { 
         // update処理でリダイレクトされた時に、直前の入力内容が保持される取得する記述をしてください。
		if (model.containsAttribute("memo")) {
		    return "memo-form";
		  }
    	 // メモを取得する記述を記述してください。変数名はmemoとしてください。
    	 Optional<Memo> memo = memoRepository.findById(id);
         // メモがない場合はnot-found画面にHTTPステータスを404にして、リダイレクトする記述を記述してください。
    	 if(memo.isEmpty()) {
        	 response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        	 return "not-found";
         }
    	 // メモをモデルに追加する記述を記述してください。変数名はmemoとしてください。
    	 model.addAttribute("memo", memo.get());
    	 // テンプレートはmemo-form.htmlを使用してください。
         return "memo-form";
     }

	// 【/memo/update/{id}】にアクセスしたら、メモを更新する機能を記述してください
	@PostMapping("/update/{id}")
    // 関数名はupdate
     public String update(@PathVariable Long id, @ModelAttribute @Valid Memo memo,
             BindingResult result, HttpServletResponse response, RedirectAttributes redirectAttributes) { 
         // メモがない場合はnot-found画面にHTTPステータスを404にして、リダイレクトする記述を記述してください。
		 Optional<Memo> opt = memoRepository.findById(id);
		 if(opt.isEmpty()) {
        	 response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        	 return "not-found";
		 }
		 // バリデーションエラーが発生した場合、入力内容を保持した状態で、編集画面にリダイレクトするように記述してください。
         if(result.hasErrors()) {
        	 redirectAttributes.addFlashAttribute(
        	 "org.springframework.validation.BindingResult.memo", result);
        	  redirectAttributes.addFlashAttribute("memo", memo);
        	  return "redirect:/memo/edit/" + id;
        	}
         // メモを更新する記述を記述してください。
         Memo memoToUpdate = opt.get();
         memoToUpdate.setTitle(memo.getTitle());
         memoToUpdate.setContent(memo.getContent());
         memoToUpdate.setUpdatedAt(LocalDateTime.now());

         memoRepository.save(memoToUpdate);
         // メモ詳細画面にリダイレクトする記述を記述してください。
         return "redirect:/memo/detail/" + id;
     }
}