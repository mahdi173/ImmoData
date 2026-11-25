package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "annonces")
public class Annonce {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Portal listing id (e.g. lbc:..., bienici:...), used as unique business key. */
    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String source;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeBien type;

    @Column(nullable = false)
    private String insee;

    @Column
    private Double lat;

    @Column
    private Double lon;

    @Column
    private BigDecimal price;

    @Column
    private Double surface;

    @Column
    private Integer pieces;

    @Column(name = "price_m2")
    private BigDecimal priceM2;

    @Column
    private String dpe;

    @Column
    private Integer score;

    @Column(name = "rendement_brut")
    private Double rendementBrut;

    @Column(name = "rendement_net")
    private Double rendementNet;

    @Column(name = "loyer_estime")
    private BigDecimal loyerEstime;

    @Column
    private String profile;

    @ManyToOne
    @JoinColumn(name = "annonceur_id")
    private Annonceur annonceur;

    @ManyToOne
    @JoinColumn(name = "commune_id")
    private Commune commune;

    @Column(name = "residence_geree")
    private Boolean residenceGeree;

    @Column(name = "publish_date")
    private Instant publishDate;

    public Annonce() {
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public TypeBien getType() {
        return type;
    }

    public void setType(TypeBien type) {
        this.type = type;
    }

    public String getInsee() {
        return insee;
    }

    public void setInsee(String insee) {
        this.insee = insee;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Double getSurface() {
        return surface;
    }

    public void setSurface(Double surface) {
        this.surface = surface;
    }

    public Integer getPieces() {
        return pieces;
    }

    public void setPieces(Integer pieces) {
        this.pieces = pieces;
    }

    public BigDecimal getPriceM2() {
        return priceM2;
    }

    public void setPriceM2(BigDecimal priceM2) {
        this.priceM2 = priceM2;
    }

    public String getDpe() {
        return dpe;
    }

    public void setDpe(String dpe) {
        this.dpe = dpe;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Double getRendementBrut() {
        return rendementBrut;
    }

    public void setRendementBrut(Double rendementBrut) {
        this.rendementBrut = rendementBrut;
    }

    public Double getRendementNet() {
        return rendementNet;
    }

    public void setRendementNet(Double rendementNet) {
        this.rendementNet = rendementNet;
    }

    public BigDecimal getLoyerEstime() {
        return loyerEstime;
    }

    public void setLoyerEstime(BigDecimal loyerEstime) {
        this.loyerEstime = loyerEstime;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Annonceur getAnnonceur() {
        return annonceur;
    }

    public void setAnnonceur(Annonceur annonceur) {
        this.annonceur = annonceur;
    }

    public Commune getCommune() {
        return commune;
    }

    public void setCommune(Commune commune) {
        this.commune = commune;
    }

    public Boolean getResidenceGeree() {
        return residenceGeree;
    }

    public void setResidenceGeree(Boolean residenceGeree) {
        this.residenceGeree = residenceGeree;
    }

    public Instant getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Instant publishDate) {
        this.publishDate = publishDate;
    }
}
