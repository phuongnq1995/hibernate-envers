package org.phuongnq.hibernate_envers.service;

import java.util.List;
import java.util.UUID;
import org.phuongnq.hibernate_envers.domain.Address;
import org.phuongnq.hibernate_envers.domain.Store;
import org.phuongnq.hibernate_envers.model.AddressDTO;
import org.phuongnq.hibernate_envers.repos.AddressRepository;
import org.phuongnq.hibernate_envers.repos.StoreRepository;
import org.phuongnq.hibernate_envers.util.NotFoundException;
import org.phuongnq.hibernate_envers.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final StoreRepository storeRepository;

    public AddressService(final AddressRepository addressRepository,
            final StoreRepository storeRepository) {
        this.addressRepository = addressRepository;
        this.storeRepository = storeRepository;
    }

    public List<AddressDTO> findAll() {
        final List<Address> addresses = addressRepository.findAll(Sort.by("id"));
        return addresses.stream()
                .map(address -> mapToDTO(address, new AddressDTO()))
                .toList();
    }

    public AddressDTO get(final UUID id) {
        return addressRepository.findById(id)
                .map(address -> mapToDTO(address, new AddressDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final AddressDTO addressDTO) {
        final Address address = new Address();
        mapToEntity(addressDTO, address);
        return addressRepository.save(address).getId();
    }

    public void update(final UUID id, final AddressDTO addressDTO) {
        final Address address = addressRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(addressDTO, address);
        addressRepository.save(address);
    }

    public void delete(final UUID id) {
        addressRepository.deleteById(id);
    }

    private AddressDTO mapToDTO(final Address address, final AddressDTO addressDTO) {
        addressDTO.setId(address.getId());
        addressDTO.setName(address.getName());
        return addressDTO;
    }

    private Address mapToEntity(final AddressDTO addressDTO, final Address address) {
        address.setName(addressDTO.getName());
        return address;
    }

    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Address address = addressRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Store masterAddressStore = storeRepository.findFirstByMasterAddress(address);
        if (masterAddressStore != null) {
            referencedWarning.setKey("address.store.masterAddress.referenced");
            referencedWarning.addParam(masterAddressStore.getId());
            return referencedWarning;
        }
        return null;
    }

}
