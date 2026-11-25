package com.example.demo.dto;

public record LoadAnnoncesResult(
        int fetched,
        int saved,
        int pages
) {
}
