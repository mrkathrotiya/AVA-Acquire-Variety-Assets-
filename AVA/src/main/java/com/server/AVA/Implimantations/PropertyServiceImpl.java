package com.server.AVA.Implimantations;

import com.server.AVA.Models.*;
import com.server.AVA.Models.DTOs.PropertyDTOs.*;
import com.server.AVA.Helpers.PropertyHelper;
import com.server.AVA.Repos.*;
import com.server.AVA.Services.AsyncService;
import com.server.AVA.Services.PropertyService;
import com.server.AVA.Services.PropertyTypeServices.*;
import com.server.AVA.Services.RedisService;
import com.server.AVA.Services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PropertyServiceImpl implements PropertyService {
    private static final Logger log = LoggerFactory.getLogger(PropertyServiceImpl.class);
    private final PropertyRepository propertyRepository;
    private final LandService landService;
    private final ShopService shopService;
    private final FlatService flatService;
    private final HouseService houseService;
    private final UserService userService;
    private final SellerRepository sellerRepository;
    private final AddressRepository addressRepository;
    private final InsightsService insightsService;
    private final RedisService redisService;
    //helpers
    private final PropertyHelper propertyHelper;
    @Override
    @Transactional
    public PropertyDTO createProperty(String token, CreatePropertyDTO createPropertyDTO) throws Exception {
        Objects.requireNonNull(token);
        PropertyDTO propertyDTO = Objects.requireNonNull(createPropertyDTO.getPropertyDTO());

        User user = userService.getUser(token);
        Seller seller = sellerRepository.getSellerByUserId(user.getId())
                .orElseThrow(() -> new UsernameNotFoundException("You are unauthorized to sell!"));

        Property property = propertyHelper.mapPropertyDTOToEntity(propertyDTO);
        Address address = propertyHelper.mapAddressDTOToEntity(propertyDTO.getAddressDTO());
        property.setAddress(addressRepository.save(address));
        Insights insights = new Insights();
        insights.setInterested(0);
        insights.setViews(0);
        insights.setCallCount(0);
        property.setInsights(insightsService.saveInsights(insights));
        property = propertyRepository.save(property);

        handleSavePropertyType(createPropertyDTO, propertyDTO, property);
        seller.getSellList().add(property);
        sellerRepository.save(seller);
        return propertyHelper.convertPropertyToDTO(property);
    }

    @Override
    @Async("customThreadPool")
    public Property getPropertyById(Long propertyId) throws Exception {
        Property property = redisService.get(String.valueOf(propertyId),Property.class);
        if (property != null){
            return property;
        }
        else {
            Objects.requireNonNull(propertyId);
            Property property1 = propertyRepository.findById(propertyId).orElseThrow(() -> new EntityNotFoundException("not found!!"));
            if (property1 != null) {
                redisService.set(Objects.requireNonNull(String.valueOf(property1.getId())), property1, 300L);
            }
            return property1;
        }
    }

    @Override
    public PropertyResponse getWholePropertyById(Long propertyId) throws Exception {
        PropertyResponse propertyResponse= redisService.get("WP"+propertyId,PropertyResponse.class);
        if (propertyResponse != null) return  propertyResponse;
        else {
            Property property = getPropertyById(propertyId);
            if (property != null) {
                PropertyResponse propertyResponse1 = getPropertyResponse(property);
                redisService.set("WP"+propertyId,propertyResponse1,300L);
                return propertyResponse1;
            }
            return null;
        }
    }

    @Override
    @Transactional
    public List<PropertyResponse> addToInterestedList(String token, Long propertyId) throws Exception {
        User user = userService.getUser(token);
        Property property = getPropertyById(propertyId);

        if (!user.getInterestedList().contains(property)) {
            user.getInterestedList().add(property);
            // Optional.ofNullable(property.getInsights())
              //      .ifPresent(insights -> insights.setInterested(insights.getInterested() + 1));
            insightsService.addInterestedCount(property.getInsights().getId());
            propertyRepository.save(property);
        }

        user = userService.saveUser(user);

        return convertToPropertyResponseList(user.getInterestedList());
    }

    @Override
    @Transactional
    public List<PropertyResponse> removeFromInterestedList(String token, Long propertyId) throws Exception {
        User user = userService.getUser(token);
        Property property = getPropertyById(propertyId);

        if (property == null) {
            throw new ChangeSetPersister.NotFoundException();
        }

        user.getInterestedList().removeIf(p -> p.getId().equals(propertyId));

        if (property.getInsights() != null) {
            //property.getInsights().setInterested(Math.max(0, property.getInsights().getInterested() - 1));
            insightsService.subtractInterestedCount(property.getInsights().getId());
            propertyRepository.save(property);
        }

        user=userService.saveUser(user);

        return convertToPropertyResponseList(user.getInterestedList());
    }

    @Override
    public List<PropertyResponse> getInterestedList(String token) throws Exception {
        User user = userService.getUser(token);
        return convertToPropertyResponseList(user.getInterestedList());
    }

    @Override
    @Transactional
    public void deleteProperty(String token, Long propertyId) throws Exception {
        log.info("SERVICE: Entered to delete property");

        User user = userService.getUser(token);
        log.info("SERVICE: User retrieved");

        Seller seller = sellerRepository.getSellerByUserId(user.getId())
                .orElseThrow(() -> new UsernameNotFoundException("You are not a seller!"));
        log.info("SERVICE: Seller retrieved");

        Property property = getPropertyById(propertyId);
        log.info("SERVICE: Property retrieved");

        if (seller.getSellList().contains(property)) {
            seller.getSellList().remove(property);
            log.info("SERVICE: Property removed from seller's list");
        } else {
            log.warn("SERVICE: Property was not found in seller's list");
        }

        sellerRepository.save(seller);
        log.info("SERVICE: Seller updated");

        if (property.getAddress() != null) {
            addressRepository.deleteById(property.getAddress().getId());
            log.info("SERVICE: Address deleted");
        }

        if (property.getInsights() != null) {
            insightsService.deleteInsights(property.getInsights());
            log.info("SERVICE: Insights deleted");
        }

        userService.removePropertyFromAllUserList(property.getId());

        handleDeleteProperty(property);

        log.info("SERVICE: Property deleted successfully");
    }

    @Override
    @Transactional
    public PropertyResponse updateProperty(Long propertyId, UpdatePropertyDTO updatePropertyDTO) throws Exception {
        Property property = getPropertyById(propertyId);
        if (updatePropertyDTO == null) {
            throw new IllegalArgumentException("Update details shouldn't be null!");
        }

        Optional.ofNullable(updatePropertyDTO.getPropertyImages())
                .ifPresent(images -> property.getImages().addAll(images));

        Optional.ofNullable(updatePropertyDTO.getPaperImages())
                .ifPresent(paperImages -> property.getPaperImages().addAll(paperImages));

        Optional.ofNullable(updatePropertyDTO.getIsForSell()).ifPresent(property::setForSell);
        Optional.ofNullable(updatePropertyDTO.getAreaType()).ifPresent(property::setAreaType);
        Optional.ofNullable(updatePropertyDTO.getPrice()).ifPresent(property::setPrice);
        Optional.ofNullable(updatePropertyDTO.getRent()).ifPresent(property::setRent);

        if (updatePropertyDTO.getAddressDTO() != null){
            Address address = property.getAddress();
            Optional.ofNullable(updatePropertyDTO.getAddressDTO().getArea()).ifPresent(address::setArea);
            Optional.ofNullable(updatePropertyDTO.getAddressDTO().getCity()).ifPresent(address::setCity);
            Optional.ofNullable(updatePropertyDTO.getAddressDTO().getStreet()).ifPresent(address::setStreet);
            Optional.ofNullable(updatePropertyDTO.getAddressDTO().getState()).ifPresent(address::setState);
            Optional.ofNullable(updatePropertyDTO.getAddressDTO().getNearbyFamousPlaces()).ifPresent(address::setNearbyFamousPlaces);
            property.setAddress(addressRepository.save(address));
        }

        handleUpdateProperty(property,updatePropertyDTO);
        propertyRepository.save(property);
        log.info("Successfully Updated property ID: {}", property.getId());
        return getPropertyResponse(property);
    }

    @Override
    public List<PropertyResponse> getList(Long price, Boolean forSell) throws Exception {
        List<Property> propertyList = propertyRepository.getPropertiesByFilter(price,forSell);
        return convertToPropertyResponseList(propertyList);
    }

    @Override
    public void addCallCount(Long propertyId) throws Exception {
        Property property = Objects.requireNonNull(getPropertyById(propertyId));
        insightsService.addCallCount(Objects.requireNonNull(property.getInsights().getId()));
    }

    @Override
    public void addViewCount(Long propertyId) throws Exception {
        Property property = Objects.requireNonNull(getPropertyById(propertyId));
        insightsService.addViewCount(Objects.requireNonNull(property.getInsights().getId()));
    }

    private void handleSavePropertyType(CreatePropertyDTO createPropertyDTO, PropertyDTO propertyDTO, Property property) throws Exception {
        switch (propertyDTO.getPropertyType()) {
            case Land:
                log.info("Save land called");
                landService.saveLand(createPropertyDTO.getLandDTO(), property.getId());
                break;
            case Shop:
                log.info("save shop called");
                shopService.saveShop(createPropertyDTO.getShopDTO(), property.getId());
                break;
            case Flat:
                log.info("save flat called");
                flatService.saveFlat(createPropertyDTO.getFlatDTO(), property.getId());
                break;
            case House:
                log.info("save house called");
                houseService.saveHouse(createPropertyDTO.getHouseDTO(), property.getId());
                break;
        }
    }

    @Transactional
    private void handleDeleteProperty(Property property) throws Exception {
        if (property == null) {
            log.warn("SERVICE: Received null property for deletion");
            return;
        }

        log.info("SERVICE: Entered to handle deletion for property ID: {}", property.getId());

        switch (property.getPropertyType()) {
            case Land:
                log.info("Deleting land...");
                landService.deleteLand(property.getId());
                break;
            case Shop:
                log.info("Deleting shop...");
                shopService.deleteShop(property.getId());
                break;
            case Flat:
                log.info("Deleting flat...");
                flatService.deleteFlat(property.getId());
                break;
            case House:
                log.info("Deleting house...");
                houseService.deleteHouse(property.getId());
                break;
            default:
                log.warn("SERVICE: Unknown property type: {}", property.getPropertyType());
                throw new IllegalArgumentException("Unknown property type: " + property.getPropertyType());
        }

        log.info("Deleting property from database...");
        propertyRepository.deleteById(property.getId());

        log.info("Successfully deleted property ID: {}", property.getId());
    }

    @Transactional
    private void handleUpdateProperty(Property property, UpdatePropertyDTO updatePropertyDTO) throws Exception {
        if (property == null) {
            log.warn("SERVICE: Received null property for update");
            return;
        }

        log.info("SERVICE: Entered to handle update for property ID: {}", property.getId());

        switch (property.getPropertyType()) {
            case Land:
                log.info("Updating land...");
                landService.updateLand(updatePropertyDTO.getLandDTO(), property.getId());
                break;
            case Shop:
                log.info("Updating shop...");
                shopService.updateShop(updatePropertyDTO.getShopDTO(), property.getId());
                break;
            case Flat:
                log.info("Updating flat...");
                flatService.updateFlat(updatePropertyDTO.getFlatDTO(), property.getId());

                break;
            case House:
                log.info("Updating house...");
                houseService.updateHouse(updatePropertyDTO.getHouseDTO(), property.getId());
                break;
            default:
                throw new IllegalArgumentException("Unknown property type: " + property.getPropertyType());
        }
    }

    public PropertyResponse getPropertyResponse(Property property) throws Exception {
        log.info("SERVICE: Entered to helper.");
        if (property == null) {
            throw new IllegalArgumentException("Property cannot be null");
        }
        log.info("SERVICE: property is not null ->");
        PropertyResponse propertyResponse = new PropertyResponse();
        propertyResponse.setPropertyDTO(propertyHelper.convertPropertyToDTO(property));
        log.info("SERVICE: done to set property dto to response.");
        log.info("SERVICE: "+property.getPropertyType().name());

        switch (property.getPropertyType()) {
            case Land:
                propertyResponse.setLandDTO(propertyHelper.convertLandToDTO(landService.getLandByPropertyId(property.getId())));
                break;
            case Shop:
                propertyResponse.setShopDTO(propertyHelper.convertShopToDTO(shopService.getShopByPropertyId(property.getId())));
                break;
            case Flat:
                log.info("Entered to convert flat");
                propertyResponse.setFlatDTO(propertyHelper.convertFlatToDTO(flatService.getFlatByPropertyId(property.getId())));
                break;
            case House:
                propertyResponse.setHouseDTO(propertyHelper.convertHouseToDTO(houseService.getHouseByPropertyId(property.getId())));
                break;
            default:
                throw new IllegalArgumentException("Unknown property type: " + property.getPropertyType());
        }


        log.info("SERVICE: Returns property response");
        return propertyResponse;
    }

    private List<PropertyResponse> convertToPropertyResponseList(List<Property> propertyList) throws Exception {
        List<PropertyResponse> list = new ArrayList<>();
        for (Property property : propertyList) {
            PropertyResponse propertyResponse = getPropertyResponse(property);
            list.add(propertyResponse);
        }
        return list;
    }
}
