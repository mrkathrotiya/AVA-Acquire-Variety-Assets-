package com.server.AVA.Helpers;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.server.AVA.Models.*;
import com.server.AVA.Models.DTOs.AddressDTO.AddressDTO;
import com.server.AVA.Models.DTOs.PropertyDTOs.*;
import com.server.AVA.Models.enums.PropertyType;
import com.server.AVA.Repos.FlatRepository;
import com.server.AVA.Repos.HouseRepository;
import com.server.AVA.Repos.LandRepository;
import com.server.AVA.Repos.ShopRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.apache.juli.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class PropertyHelper {
    private static final Logger log = LoggerFactory.getLogger(PropertyHelper.class);

    public AddressDTO convertAddressToDTO(Address address){
        Objects.requireNonNull(address);
        AddressDTO addressDTO = new AddressDTO();
        Optional.ofNullable(address.getArea()).ifPresent(addressDTO::setArea);
        Optional.ofNullable(address.getCity()).ifPresent(addressDTO::setCity);
        Optional.ofNullable(address.getState()).ifPresent(addressDTO::setState);
        Optional.ofNullable(address.getNearbyFamousPlaces()).ifPresent(addressDTO::setNearbyFamousPlaces);
        return addressDTO;
    }

    public PropertyDTO convertPropertyToDTO(Property property) {
        log.info("HELPER: Entered to set property dto to response");
        PropertyDTO dto = new PropertyDTO();

        dto.setPropertyImages(property.getImages());
        dto.setPaperImages(property.getPaperImages());
        dto.setPropertyType(property.getPropertyType());
        dto.setPrice(property.getPrice());
        dto.setRent(property.getRent());
        dto.setIsForSell(property.getForSell());
        dto.setAreaType(property.getAreaType());
        log.info("HELPER: all attributes set accept address");
        // Convert Address Entity to DTO
        if (property.getAddress() != null) {
            dto.setAddressDTO(convertAddressToDTO(property.getAddress()));
        }
        log.info("HELPER: Address dto set and returns dto");
        return dto;
    }

    public LandDTO convertLandToDTO(Land land) {
        log.info("HELPER: Entered to covert land");
        if (land == null) {
            throw new IllegalArgumentException("Land cannot be null");
        }
        log.info("HELPER: returns land");
        return new LandDTO(land.getArea(), land.getLandType());
    }

    public FlatDTO convertFlatToDTO(Flat flat) {
        log.info("HELPER: Entered to convert flat.");
        if (flat == null) {
            throw new IllegalArgumentException("Flat cannot be null");
        }
        HomeDTO homeDTO = (flat.getHome() != null) ? convertHomeToDTO(flat.getHome()) : null;
        log.info("HELPER: returns flat");
        return new FlatDTO(flat.getFloorNo(),flat.getElevator(),homeDTO);
    }

    public ShopDTO convertShopToDTO(Shop shop) {
        log.info("HELPER: Entered to convert shop");
        if (shop == null) {
            throw new IllegalArgumentException("shop cannot be null");
        }
        log.info("HELPER: returns shop");
        return new ShopDTO(shop.getArea(),shop.getFloorNo(),shop.getShopNo(),shop.getIsComplex());
    }

    public HouseDTO convertHouseToDTO(House house) {
        log.info("HELPER: Entered to convert house");
        if (house == null) {
            throw new IllegalArgumentException("House cannot be null");
        }
        HomeDTO homeDTO = (house.getHome() != null) ? convertHomeToDTO(house.getHome()) : null;
        log.info("HELPER: returns house");
        return new HouseDTO(house.getFloors(),house.getIsGarage(),house.getGarageSize(),
                homeDTO);
    }

    private HomeDTO convertHomeToDTO(Home home) {
        if (home == null) {
            throw new IllegalArgumentException("Home cannot be null");
        }

        List<SizeDTO> sizeDTOList = home.getSizes().stream()
                .map(size -> new SizeDTO(size.getHomeAreaType(), size.getSize()))
                .collect(Collectors.toList());

        return new HomeDTO(
                home.getArea(), home.getUsableArea(), home.getKitchen(),
                home.getBed(), home.getLiving(), home.getToilet(),
                home.getBathroom(), home.getBalcony(), home.getFurnitureType(),
                home.getGardenType(), sizeDTOList
        );
    }

    public Property mapPropertyDTOToEntity(PropertyDTO propertyDTO) {
        log.info("entered to map");
        Property property = new Property();
        log.info("property object created");
        Optional.ofNullable(propertyDTO.getPropertyImages()).ifPresent(property::setImages);
        Optional.ofNullable(propertyDTO.getPaperImages()).ifPresent(property::setPaperImages);
        log.info("both type of images has been set");
        Optional.ofNullable(propertyDTO.getPropertyType()).ifPresent(property::setPropertyType);
        log.info("property type has been set");
        Optional.ofNullable(propertyDTO.getPrice()).ifPresent(property::setPrice);
        log.info("property price has been set");
        Optional.ofNullable(propertyDTO.getRent()).ifPresent(property::setRent);
        log.info("property rent has been set");
        Optional.ofNullable(propertyDTO.getIsForSell()).ifPresent(property::setForSell);
        log.info("property forSell has been set");
        Optional.ofNullable(propertyDTO.getAreaType()).ifPresent(property::setAreaType);
        log.info("property area type has been set");
        property.setDateOfUpload(new Date());
        log.info("property date has been set");
        return property;
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
