package com.example.demo.service;

import com.example.demo.model.Commune;
import com.example.demo.repository.CommuneRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CommuneService {

    private final CommuneRepository communeRepository;

    public CommuneService(CommuneRepository communeRepository) {
        this.communeRepository = communeRepository;
    }

    public List<Commune> findAll() {
        return communeRepository.findAll();
    }

    public Commune findById(Long id) {
        return communeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Commune not found"));
    }

    public Commune findOrCreate(String name, String code) {
        return communeRepository.findByCode(code)
                .map(existing -> {
                    if (name != null && !name.isBlank() && !name.equals(existing.getName())) {
                        existing.setName(name);
                        return communeRepository.save(existing);
                    }
                    return existing;
                })
                .orElseGet(() -> communeRepository.save(new Commune(
                        name != null && !name.isBlank() ? name : code,
                        code
                )));
    }

    public Commune create(Commune item) {
        item.setId(null);
        return communeRepository.save(item);
    }

    public Commune update(Long id, Commune updated) {
        Commune existing = findById(id);
        existing.setName(updated.getName());
        existing.setCode(updated.getCode());
        return communeRepository.save(existing);
    }

    public void delete(Long id) {
        if (!communeRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Commune not found");
        }
        communeRepository.deleteById(id);
    }
}
