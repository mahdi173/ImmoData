package com.example.demo.repository;

import com.example.demo.model.Annonceur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnnonceurRepository extends JpaRepository<Annonceur, Long> {

    Optional<Annonceur> findBySiren(Integer siren);

    Optional<Annonceur> findByNameIgnoreCase(String name);
}
