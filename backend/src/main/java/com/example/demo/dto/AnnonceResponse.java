package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AnnonceResponse(
        List<PillrAnnonceDto> donnees,
        Integer total,
        @JsonProperty("curseur_suivant") String curseurSuivant
) {
}
