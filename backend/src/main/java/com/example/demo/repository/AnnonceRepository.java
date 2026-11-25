package com.example.demo.repository;

import com.example.demo.model.Annonce;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnnonceRepository extends JpaRepository<Annonce, Long> {

    Optional<Annonce> findByName(String name);
}
