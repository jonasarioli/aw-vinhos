package com.algaworks.vinhos.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.algaworks.vinhos.model.Vinho;

public interface Vinhos extends JpaRepository<Vinho, Long> {

	
}
