package com.lesson.inquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lesson.inquiry.model.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}