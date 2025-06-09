package org.phuongnq.hibernate_envers.service;

import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import org.phuongnq.hibernate_envers.domain.Address;
import org.phuongnq.hibernate_envers.domain.Campaign;
import org.phuongnq.hibernate_envers.domain.Language;
import org.phuongnq.hibernate_envers.domain.Store;
import org.phuongnq.hibernate_envers.model.StoreDTO;
import org.phuongnq.hibernate_envers.repos.AddressRepository;
import org.phuongnq.hibernate_envers.repos.CampaignRepository;
import org.phuongnq.hibernate_envers.repos.LanguageRepository;
import org.phuongnq.hibernate_envers.repos.StoreRepository;
import org.phuongnq.hibernate_envers.util.NotFoundException;
import org.phuongnq.hibernate_envers.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class StoreService {

    private final StoreRepository storeRepository;
    private final AddressRepository addressRepository;
    private final LanguageRepository languageRepository;
    private final CampaignRepository campaignRepository;

    public StoreService(final StoreRepository storeRepository,
            final AddressRepository addressRepository, final LanguageRepository languageRepository,
            final CampaignRepository campaignRepository) {
        this.storeRepository = storeRepository;
        this.addressRepository = addressRepository;
        this.languageRepository = languageRepository;
        this.campaignRepository = campaignRepository;
    }

    public List<StoreDTO> findAll() {
        final List<Store> stores = storeRepository.findAll(Sort.by("id"));
        return stores.stream()
                .map(store -> mapToDTO(store, new StoreDTO()))
                .toList();
    }

    public StoreDTO get(final UUID id) {
        return storeRepository.findById(id)
                .map(store -> mapToDTO(store, new StoreDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final StoreDTO storeDTO) {
        final Store store = new Store();
        mapToEntity(storeDTO, store);
        return storeRepository.save(store).getId();
    }

    public void update(final UUID id, final StoreDTO storeDTO) {
        final Store store = storeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(storeDTO, store);
        storeRepository.save(store);
    }

    public void delete(final UUID id) {
        storeRepository.deleteById(id);
    }

    private StoreDTO mapToDTO(final Store store, final StoreDTO storeDTO) {
        storeDTO.setId(store.getId());
        storeDTO.setName(store.getName());
        storeDTO.setMasterAddress(store.getMasterAddress() == null ? null : store.getMasterAddress().getId());
        storeDTO.setDeliveryAddress(store.getDeliveryAddress() == null ? null : store.getDeliveryAddress().getId());
        storeDTO.setLanguages(store.getLanguages().stream()
                .map(language -> language.getId())
                .toList());
        return storeDTO;
    }

    private Store mapToEntity(final StoreDTO storeDTO, final Store store) {
        store.setName(storeDTO.getName());
        final Address masterAddress = storeDTO.getMasterAddress() == null ? null : addressRepository.findById(storeDTO.getMasterAddress())
                .orElseThrow(() -> new NotFoundException("masterAddress not found"));
        store.setMasterAddress(masterAddress);
        final Address deliveryAddress = storeDTO.getDeliveryAddress() == null ? null : addressRepository.findById(storeDTO.getDeliveryAddress())
                .orElseThrow(() -> new NotFoundException("deliveryAddress not found"));
        store.setDeliveryAddress(deliveryAddress);
        final List<Language> languages = languageRepository.findAllById(
                storeDTO.getLanguages() == null ? List.of() : storeDTO.getLanguages());
        if (languages.size() != (storeDTO.getLanguages() == null ? 0 : storeDTO.getLanguages().size())) {
            throw new NotFoundException("one of languages not found");
        }
        store.setLanguages(new HashSet<>(languages));
        return store;
    }

    public boolean masterAddressExists(final UUID id) {
        return storeRepository.existsByMasterAddressId(id);
    }

    public boolean deliveryAddressExists(final UUID id) {
        return storeRepository.existsByDeliveryAddressId(id);
    }

    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Store store = storeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Campaign storeCampaign = campaignRepository.findFirstByStore(store);
        if (storeCampaign != null) {
            referencedWarning.setKey("store.campaign.store.referenced");
            referencedWarning.addParam(storeCampaign.getId());
            return referencedWarning;
        }
        final Store deliveryAddressStore = storeRepository.findFirstByDeliveryAddressAndIdNot(store, store.getId());
        if (deliveryAddressStore != null) {
            referencedWarning.setKey("store.store.deliveryAddress.referenced");
            referencedWarning.addParam(deliveryAddressStore.getId());
            return referencedWarning;
        }
        return null;
    }

}
