package com.example.demo.service;

import com.example.demo.config.PillrProperties;
import com.example.demo.dto.AnnonceResponse;
import com.example.demo.dto.LoadAnnoncesResult;
import com.example.demo.dto.PillrAnnonceDto;
import com.example.demo.model.Annonce;
import com.example.demo.model.TypeBien;
import com.example.demo.repository.AnnonceRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
public class AnnonceService {

    private static final int PAGE_SIZE = 50;

    private final AnnonceRepository annonceRepository;
    private final AnnonceurService annonceurService;
    private final CommuneService communeService;
    private final PillrProperties pillrProperties;
    private final RestClient restClient;

    public AnnonceService(
            AnnonceRepository annonceRepository,
            AnnonceurService annonceurService,
            CommuneService communeService,
            PillrProperties pillrProperties,
            RestClient.Builder restClientBuilder
    ) {
        this.annonceRepository = annonceRepository;
        this.annonceurService = annonceurService;
        this.communeService = communeService;
        this.pillrProperties = pillrProperties;
        this.restClient = restClientBuilder.build();
    }

    public List<Annonce> findAll() {
        return annonceRepository.findAll();
    }

    public Annonce findById(Long id) {
        return annonceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Annonce not found"));
    }

    public Annonce create(Annonce item) {
        item.setId(null);
        return annonceRepository.save(item);
    }

    public Annonce update(Long id, Annonce updated) {
        Annonce existing = findById(id);
        copyFields(updated, existing);
        return annonceRepository.save(existing);
    }

    public void delete(Long id) {
        if (!annonceRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Annonce not found");
        }
        annonceRepository.deleteById(id);
    }

    /**
     * Fetches pages from Pillr (cursor pagination) up to {@code pillr.max-results}
     * (default 200) and upserts annonces, communes and annonceurs.
     */
    public LoadAnnoncesResult loadFromPillr() {
        requirePillrConfig();

        int maxResults = Math.max(1, pillrProperties.maxResults());
        int fetched = 0;
        int saved = 0;
        int pages = 0;
        String cursor = null;

        do {
            int remaining = maxResults - fetched;
            int pageSize = Math.min(PAGE_SIZE, remaining);
            AnnonceResponse page = fetchPage(cursor, pageSize);
            pages++;

            List<PillrAnnonceDto> donnees = page.donnees() != null ? page.donnees() : List.of();
            for (PillrAnnonceDto dto : donnees) {
                if (fetched >= maxResults) {
                    break;
                }
                fetched++;
                if (upsertFromDto(dto) != null) {
                    saved++;
                }
            }

            cursor = page.curseurSuivant();
        } while (fetched < maxResults && StringUtils.hasText(cursor));

        return new LoadAnnoncesResult(fetched, saved, pages);
    }

    AnnonceResponse fetchPage(String cursor, int pageSize) {
        URI uri = UriComponentsBuilder
                .fromUriString(pillrProperties.annoncesUrl())
                .queryParam("limite", pageSize)
                .queryParamIfPresent("curseur", java.util.Optional.ofNullable(cursor).filter(StringUtils::hasText))
                .build(true)
                .toUri();

        AnnonceResponse response = restClient.get()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + pillrProperties.apiKey())
                .retrieve()
                .body(AnnonceResponse.class);

        if (response == null) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Empty response from Pillr");
        }
        return response;
    }

    @Transactional
    public Annonce upsertFromDto(PillrAnnonceDto dto) {
        if (dto == null) {
            return null;
        }

        String businessKey = firstNonBlank(dto.idPortail(), dto.id());
        TypeBien type = mapType(dto.type());
        if (!StringUtils.hasText(businessKey) || type == null || !StringUtils.hasText(dto.insee())) {
            return null;
        }

        Annonce annonce = annonceRepository.findByName(businessKey).orElseGet(Annonce::new);
        annonce.setName(businessKey);
        annonce.setSource(dto.source());
        annonce.setType(type);
        annonce.setInsee(dto.insee());
        annonce.setLat(dto.lat());
        annonce.setLon(dto.lon());
        annonce.setPrice(dto.prix());
        annonce.setSurface(dto.surface());
        annonce.setPieces(dto.pieces());
        annonce.setPriceM2(dto.prixM2());
        annonce.setDpe(dto.dpe());
        annonce.setScore(dto.score());
        annonce.setRendementBrut(dto.rendementBrut());
        annonce.setRendementNet(dto.rendementNet());
        annonce.setLoyerEstime(dto.loyerEstime());
        annonce.setProfile(dto.profil());
        annonce.setResidenceGeree(dto.residenceGeree());
        annonce.setPublishDate(dto.premiereParution());
        annonce.setCommune(communeService.findOrCreate(dto.commune(), dto.insee()));
        annonce.setAnnonceur(annonceurService.findOrCreateFromDto(dto.annonceur()).orElse(null));

        return annonceRepository.save(annonce);
    }

    private void requirePillrConfig() {
        if (!StringUtils.hasText(pillrProperties.apiKey()) || !StringUtils.hasText(pillrProperties.annoncesUrl())) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Pillr API is not configured (PILLR_API_KEY / PILLR_ANNONCES_URL)"
            );
        }
    }

    private static TypeBien mapType(String type) {
        if (!StringUtils.hasText(type)) {
            return null;
        }
        return switch (type.trim().toLowerCase()) {
            case "appartement" -> TypeBien.APPARTEMENT;
            case "maison" -> TypeBien.MAISON;
            case "immeuble" -> TypeBien.IMMEUBLE;
            default -> null;
        };
    }

    private static void copyFields(Annonce source, Annonce target) {
        target.setName(source.getName());
        target.setSource(source.getSource());
        target.setType(source.getType());
        target.setInsee(source.getInsee());
        target.setLat(source.getLat());
        target.setLon(source.getLon());
        target.setPrice(source.getPrice());
        target.setSurface(source.getSurface());
        target.setPieces(source.getPieces());
        target.setPriceM2(source.getPriceM2());
        target.setDpe(source.getDpe());
        target.setScore(source.getScore());
        target.setRendementBrut(source.getRendementBrut());
        target.setRendementNet(source.getRendementNet());
        target.setLoyerEstime(source.getLoyerEstime());
        target.setProfile(source.getProfile());
        target.setAnnonceur(source.getAnnonceur());
        target.setCommune(source.getCommune());
        target.setResidenceGeree(source.getResidenceGeree());
        target.setPublishDate(source.getPublishDate());
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (StringUtils.hasText(value)) {
                return value.trim();
            }
        }
        return null;
    }
}
