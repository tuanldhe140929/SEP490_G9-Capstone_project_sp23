package com.SEP490_G9.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SEP490_G9.entities.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long>{

}
