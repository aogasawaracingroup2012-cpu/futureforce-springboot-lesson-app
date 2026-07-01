package com.lesson.inquiry.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Data
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "名前を入力してください")
    @Column(nullable = false)
    private String name;
    
    @NotBlank(message = "メールアドレスを入力してください")
    @Email(message = "メールアドレスの形式が正しくありません")
    @Column(nullable = false)
    private String email;
    
    private String phone;
    
    @NotBlank(message = "件名を入力してください")
    @Column(nullable = false)
    private String subject;
    
    @NotBlank(message = "内容を入力してください")
    @Size(min = 10, message = "内容は10文字以上で入力してください")
    @Column(nullable = false)
    private String message;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
}