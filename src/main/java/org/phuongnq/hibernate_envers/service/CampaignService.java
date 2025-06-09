package org.phuongnq.hibernate_envers.service;

import java.util.List;
import java.util.UUID;
import org.phuongnq.hibernate_envers.domain.Campaign;
import org.phuongnq.hibernate_envers.domain.Store;
import org.phuongnq.hibernate_envers.model.CampaignDTO;
import org.phuongnq.hibernate_envers.repos.CampaignRepository;
import org.phuongnq.hibernate_envers.repos.StoreRepository;
import org.phuongnq.hibernate_envers.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final StoreRepository storeRepository;

    public CampaignService(final CampaignRepository campaignRepository,
            final StoreRepository storeRepository) {
        this.campaignRepository = campaignRepository;
        this.storeRepository = storeRepository;
    }

    public List<CampaignDTO> findAll() {
        final List<Campaign> campaigns = campaignRepository.findAll(Sort.by("id"));
        return campaigns.stream()
                .map(campaign -> mapToDTO(campaign, new CampaignDTO()))
                .toList();
    }

    public CampaignDTO get(final UUID id) {
        return campaignRepository.findById(id)
                .map(campaign -> mapToDTO(campaign, new CampaignDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final CampaignDTO campaignDTO) {
        final Campaign campaign = new Campaign();
        mapToEntity(campaignDTO, campaign);
        return campaignRepository.save(campaign).getId();
    }

    public void update(final UUID id, final CampaignDTO campaignDTO) {
        final Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(campaignDTO, campaign);
        campaignRepository.save(campaign);
    }

    public void delete(final UUID id) {
        campaignRepository.deleteById(id);
    }

    private CampaignDTO mapToDTO(final Campaign campaign, final CampaignDTO campaignDTO) {
        campaignDTO.setId(campaign.getId());
        campaignDTO.setName(campaign.getName());
        campaignDTO.setStore(campaign.getStore() == null ? null : campaign.getStore().getId());
        return campaignDTO;
    }

    private Campaign mapToEntity(final CampaignDTO campaignDTO, final Campaign campaign) {
        campaign.setName(campaignDTO.getName());
        final Store store = campaignDTO.getStore() == null ? null : storeRepository.findById(campaignDTO.getStore())
                .orElseThrow(() -> new NotFoundException("store not found"));
        campaign.setStore(store);
        return campaign;
    }

}
