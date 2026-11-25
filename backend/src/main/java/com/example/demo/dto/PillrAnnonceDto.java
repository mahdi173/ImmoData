package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PillrAnnonceDto(
        String id,
        @JsonProperty("id_portail") String idPortail,
        String lien,
        String source,
        String type,
        String insee,
        @JsonProperty("code_postal") String codePostal,
        String commune,
        Double lat,
        Double lon,
        BigDecimal prix,
        Double surface,
        Integer pieces,
        @JsonProperty("prix_m2") BigDecimal prixM2,
        String dpe,
        Integer score,
        @JsonProperty("rendement_brut") Double rendementBrut,
        @JsonProperty("rendement_net") Double rendementNet,
        @JsonProperty("loyer_estime") BigDecimal loyerEstime,
        String profil,
        PillrAnnonceurDto annonceur,
        @JsonProperty("residence_geree") Boolean residenceGeree,
        @JsonProperty("premiere_parution") Instant premiereParution
) {
}
