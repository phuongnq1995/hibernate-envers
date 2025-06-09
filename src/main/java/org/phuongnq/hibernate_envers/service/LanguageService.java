package org.phuongnq.hibernate_envers.service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import org.phuongnq.hibernate_envers.domain.Language;
import org.phuongnq.hibernate_envers.model.LanguageDTO;
import org.phuongnq.hibernate_envers.repos.LanguageRepository;
import org.phuongnq.hibernate_envers.repos.StoreRepository;
import org.phuongnq.hibernate_envers.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class LanguageService {

    private final LanguageRepository languageRepository;
    private final StoreRepository storeRepository;

    public LanguageService(final LanguageRepository languageRepository,
            final StoreRepository storeRepository) {
        this.languageRepository = languageRepository;
        this.storeRepository = storeRepository;
    }

    public List<LanguageDTO> findAll() {
        final List<Language> languages = languageRepository.findAll(Sort.by("id"));
        return languages.stream()
                .map(language -> mapToDTO(language, new LanguageDTO()))
                .toList();
    }

    public LanguageDTO get(final UUID id) {
        return languageRepository.findById(id)
                .map(language -> mapToDTO(language, new LanguageDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final LanguageDTO languageDTO) {
        final Language language = new Language();
        mapToEntity(languageDTO, language);
        return languageRepository.save(language).getId();
    }

    public void update(final UUID id, final LanguageDTO languageDTO) {
        final Language language = languageRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(languageDTO, language);
        languageRepository.save(language);
    }

    public void delete(final UUID id) {
        final Language language = languageRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        storeRepository.findAllByLanguages(language)
                .forEach(store -> store.getLanguages().remove(language));
        languageRepository.delete(language);
    }

    private LanguageDTO mapToDTO(final Language language, final LanguageDTO languageDTO) {
        languageDTO.setId(language.getId());
        languageDTO.setName(language.getName());
        return languageDTO;
    }

    private Language mapToEntity(final LanguageDTO languageDTO, final Language language) {
        language.setName(languageDTO.getName());
        return language;
    }

}
