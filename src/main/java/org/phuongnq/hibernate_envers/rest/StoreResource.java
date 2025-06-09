package org.phuongnq.hibernate_envers.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.phuongnq.hibernate_envers.model.StoreDTO;
import org.phuongnq.hibernate_envers.service.StoreService;
import org.phuongnq.hibernate_envers.util.ReferencedException;
import org.phuongnq.hibernate_envers.util.ReferencedWarning;
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
@RequestMapping(value = "/api/stores", produces = MediaType.APPLICATION_JSON_VALUE)
public class StoreResource {

    private final StoreService storeService;

    public StoreResource(final StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping
    public ResponseEntity<List<StoreDTO>> getAllStores() {
        return ResponseEntity.ok(storeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreDTO> getStore(@PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(storeService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createStore(@RequestBody @Valid final StoreDTO storeDTO) {
        final UUID createdId = storeService.create(storeDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateStore(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final StoreDTO storeDTO) {
        storeService.update(id, storeDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteStore(@PathVariable(name = "id") final UUID id) {
        final ReferencedWarning referencedWarning = storeService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        storeService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
