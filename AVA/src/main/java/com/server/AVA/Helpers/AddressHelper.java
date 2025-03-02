package com.server.AVA.Helpers;

import com.server.AVA.Models.*;
import com.server.AVA.Models.DTOs.AddressDTO.AddressDTO;
import com.server.AVA.Models.DTOs.PropertyDTOs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AddressHelper {

    public AddressDTO convertAddressToDTO(Address address){
        Objects.requireNonNull(address);
        AddressDTO addressDTO = new AddressDTO();
        Optional.ofNullable(address.getArea()).ifPresent(addressDTO::setArea);
        Optional.ofNullable(address.getCity()).ifPresent(addressDTO::setCity);
        Optional.ofNullable(address.getState()).ifPresent(addressDTO::setState);
        Optional.ofNullable(address.getNearbyFamousPlaces()).ifPresent(addressDTO::setNearbyFamousPlaces);
        return addressDTO;
    }

    public Address mapAddressDTOToEntity(AddressDTO addressDTO) {
        Address address = new Address();
        Optional.ofNullable(addressDTO.getStreet()).ifPresent(address::setStreet);
        Optional.ofNullable(addressDTO.getArea()).ifPresent(address::setArea);
        Optional.ofNullable(addressDTO.getCity()).ifPresent(address::setCity);
        Optional.ofNullable(addressDTO.getState()).ifPresent(address::setState);
        Optional.ofNullable(addressDTO.getNearbyFamousPlaces()).ifPresent(address::setNearbyFamousPlaces);
        return address;
    }


}
