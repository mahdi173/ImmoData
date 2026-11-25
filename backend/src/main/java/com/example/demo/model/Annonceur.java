package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "annonceurs")
public class Annonceur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Column
    private Integer siren;

    @JsonIgnore
    @OneToMany(mappedBy = "annonceur")
    private List<Annonce> annonceList = new ArrayList<>();

    public Annonceur() {
    }

    public Annonceur(String name, Integer siren) {
        this.name = name;
        this.siren = siren;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSiren() {
        return siren;
    }

    public void setSiren(Integer siren) {
        this.siren = siren;
    }

    @JsonIgnore
    public List<Annonce> getAnnonces() {
        return annonceList;
    }

    public void addAnnonce(Annonce annonce) {
        annonceList.add(annonce);
        annonce.setAnnonceur(this);
    }
}
