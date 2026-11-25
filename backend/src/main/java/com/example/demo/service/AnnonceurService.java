package com.example.demo.service;

import com.example.demo.dto.PillrAnnonceurDto;
import com.example.demo.model.Annonceur;
import com.example.demo.repository.AnnonceurRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class AnnonceurService {

    private final AnnonceurRepository annonceurRepository;

    public AnnonceurService(AnnonceurRepository annonceurRepository) {
        this.annonceurRepository = annonceurRepository;
    }

    public List<Annonceur> findAll() {
        return annonceurRepository.findAll();
    }

    public Annonceur findById(Long id) {
        return annonceurRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Annonceur not found"));
    }

    public Optional<Annonceur> findOrCreateFromDto(PillrAnnonceurDto dto) {
        if (dto == null) {
            return Optional.empty();
        }

        Integer siren = parseSiren(dto.siren());
        String name = firstNonBlank(dto.nom(), dto.enseigne());

        if (siren != null) {
            return Optional.of(annonceurRepository.findBySiren(siren)
                    .map(existing -> {
                        if (name != null && !name.equals(existing.getName())) {
                            existing.setName(name);
                            return annonceurRepository.save(existing);
                        }
                        return existing;
                    })
                    .orElseGet(() -> annonceurRepository.save(new Annonceur(
                            name != null ? name : "SIREN " + siren,
                            siren
                    ))));
        }

        if (name == null) {
            return Optional.empty();
        }

        return Optional.of(annonceurRepository.findByNameIgnoreCase(name)
                .orElseGet(() -> annonceurRepository.save(new Annonceur(name, null))));
    }

    public Annonceur create(Annonceur item) {
        item.setId(null);
        return annonceurRepository.save(item);
    }

    public Annonceur update(Long id, Annonceur updated) {
        Annonceur existing = findById(id);
        existing.setName(updated.getName());
        existing.setSiren(updated.getSiren());
        return annonceurRepository.save(existing);
    }

    public void delete(Long id) {
        if (!annonceurRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Annonceur not found");
        }
        annonceurRepository.deleteById(id);
    }

    private static Integer parseSiren(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Integer.valueOf(value.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }
        return null;
    }
}
