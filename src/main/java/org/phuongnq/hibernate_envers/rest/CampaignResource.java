package org.phuongnq.hibernate_envers.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.phuongnq.hibernate_envers.model.CampaignDTO;
import org.phuongnq.hibernate_envers.service.CampaignService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/campaigns", produces = MediaType.APPLICATION_JSON_VALUE)
public class CampaignResource {

    private final CampaignService campaignService;

    public CampaignResource(final CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GetMapping
    public ResponseEntity<List<CampaignDTO>> getAllCampaigns() {
        return ResponseEntity.ok(campaignService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampaignDTO> getCampaign(@PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(campaignService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createCampaign(@RequestBody @Valid final CampaignDTO campaignDTO) {
        final UUID createdId = campaignService.create(campaignDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateCampaign(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final CampaignDTO campaignDTO) {
        campaignService.update(id, campaignDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCampaign(@PathVariable(name = "id") final UUID id) {
        campaignService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
