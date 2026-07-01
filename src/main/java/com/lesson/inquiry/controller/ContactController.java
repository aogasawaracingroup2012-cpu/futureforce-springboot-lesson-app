package com.lesson.inquiry.controller;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.beans.BeanUtils;
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

import com.lesson.inquiry.model.Contact;
import com.lesson.inquiry.repository.ContactRepository;

@Controller
@RequestMapping("/contact")
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    @GetMapping
    public String list(Model model) {
        List<Contact> contacts = contactRepository.findAll();
        model.addAttribute("contacts", contacts);
        return "contact-list";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("contact", new Contact());
        return "contact-form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute @Valid Contact contact,
            BindingResult result) {
        if (result.hasErrors()) {
            return "contact-form";
        }

        contact.setCreatedAt(LocalDateTime.now());
        contact.setUpdatedAt(LocalDateTime.now());
        contactRepository.save(contact);
        return "redirect:/contact";
    }

    @GetMapping("/detail/{id}")
    public String showDetail(@PathVariable Long id, Model model,
            HttpServletResponse response) {
        Optional<Contact> contact = contactRepository.findById(id);
        if (contact.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "not-found"; // エラー画面にリダイレクト
        }

        model.addAttribute("contact", contact.get());
        return "contact-detail";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, HttpServletResponse response) {
        if (model.containsAttribute("contact")) {
            return "contact-form";
        }

        return contactRepository.findById(id)
                .map(contact -> {
                    model.addAttribute("contact", contact);
                    return "contact-form";
                })
                .orElseGet(() -> {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    return "not-found";
                });
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
            @ModelAttribute @Valid Contact contact,
            BindingResult result,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes) {

        Optional<Contact> opt = contactRepository.findById(id);
        if (opt.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "not-found"; // エラー画面表示
        }

        Contact contactToUpdate = opt.get();

        if (result.hasErrors()) {
            // 入力内容とエラー情報をFlashスコープに保存
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.contact", result);
            redirectAttributes.addFlashAttribute("contact", contact);
            return "redirect:/contact/edit/" + id; // editにリダイレクト
        }

        // 入力内容を既存エンティティに反映
        BeanUtils.copyProperties(contact, contactToUpdate, "id", "createdAt","updatedAt"); //setしたくない項目を指定し、それ以外は自動でsetしてくれる
        contactToUpdate.setUpdatedAt(LocalDateTime.now());

        contactRepository.save(contactToUpdate);

        return "redirect:/contact/detail/" + id;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpServletResponse response) {
   	 if(contactRepository.existsById(id)) { 
   		contactRepository.deleteById(id);
   	 } else {
   		 response.setStatus(HttpServletResponse.SC_NOT_FOUND);
   		 return "not-found";
   	 }
        return "redirect:/contact";
    }
}