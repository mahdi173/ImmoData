package com.example.demo.controller;

import com.example.demo.dto.LoadAnnoncesResult;
import com.example.demo.model.Annonce;
import com.example.demo.service.AnnonceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/annonces")
public class AnnonceController {

    private final AnnonceService annonceService;

    public AnnonceController(AnnonceService annonceService) {
        this.annonceService = annonceService;
    }

    @PostMapping("/load")
    public LoadAnnoncesResult loadAnnonces() {
        return annonceService.loadFromPillr();
    }

    @GetMapping
    public List<Annonce> getAll() {
        return annonceService.findAll();
    }

    @GetMapping("/{id}")
    public Annonce getById(@PathVariable Long id) {
        return annonceService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Annonce create(@Valid @RequestBody Annonce annonce) {
        return annonceService.create(annonce);
    }

    @PutMapping("/{id}")
    public Annonce update(@PathVariable Long id, @Valid @RequestBody Annonce annonce) {
        return annonceService.update(id, annonce);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        annonceService.delete(id);
    }
}
