package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PillrAnnonceurDto(
        @JsonProperty("type_vendeur") String typeVendeur,
        String nom,
        String enseigne,
        String siren
) {
}
