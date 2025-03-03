package com.server.AVA.Implimantations;

import com.server.AVA.Models.*;
import com.server.AVA.Models.DTOs.PropertyDTOs.*;
import com.server.AVA.Helpers.PropertyHelper;
import com.server.AVA.Repos.*;
import com.server.AVA.Services.PropertyService;
import com.server.AVA.Services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PropertyServiceImpl implements PropertyService {
    private static final Logger log = LoggerFactory.getLogger(PropertyServiceImpl.class);
    private final PropertyRepository propertyRepository;
    private final LandRepository landRepository;
    private final ShopRepository shopRepository;
    private final HomeRepository homeRepository;
    private final HouseRepository houseRepository;
    private final FlatRepository flatRepository;
    private final UserService userService;
    private final SellerRepository sellerRepository;
    private final AddressRepository addressRepository;
    private final InsightsRepository insightsRepository;
    //helpers
    private final PropertyHelper propertyHelper;
    @Override
    public PropertyDTO createProperty(String token, CreatePropertyDTO createPropertyDTO) throws Exception {
        Objects.requireNonNull(token);
        PropertyDTO propertyDTO = Objects.requireNonNull(createPropertyDTO.getPropertyDTO());
        log.info("property DTO created!");

        User user = userService.getUser(token);
        log.info("User retrieved");

        Seller seller = sellerRepository.getSellerByUserId(user.getId())
                .orElseThrow(() -> new UsernameNotFoundException("You are unauthorized to sell!"));
        log.info("Seller assigned!");

        Property property = propertyHelper.mapPropertyDTOToEntity(propertyDTO);
        log.info("property converted");

        Address address = propertyHelper.mapAddressDTOToEntity(propertyDTO.getAddressDTO());
        log.info("address converted");

        property.setAddress(addressRepository.save(address));
        log.info("address saved and assigned to property");

        property.setInsights(insightsRepository.save(new Insights())); // Ensure Insights is saved
        log.info("insights assigned to property");

        property = propertyRepository.save(property); // Save Property to get ID
        log.info("property saved to DB");


        log.info("Handle property called");
        handlePropertyType(createPropertyDTO, propertyDTO, property);

        seller.getSellList().add(property);
        log.info("property added to seller's sell list");

        sellerRepository.save(seller);
        log.info("seller saved to DB");
        return propertyHelper.convertPropertyToDTO(property);
    }

    @Override
    public Property getPropertyById(Long propertyId) throws Exception {
        Objects.requireNonNull(propertyId);
        return propertyRepository.findById(propertyId)
                .orElseThrow(()-> new EntityNotFoundException("Property not found with this id: "+propertyId));
    }

    @Override
    public PropertyResponse getWholePropertyById(Long propertyId) throws Exception {
        log.info("SERVICE: Entered to property service.");
        log.info("SERVICE: Starting to fetch property..");
        Property property = getPropertyById(propertyId);
        log.info("SERVICE: Property fetched!");
        log.info("SERVICE: Entering to helper..");
        return getPropertyResponse(property);
    }

    @Override
    @Transactional
    public List<PropertyResponse> addToInterestedList(String token, Long propertyId) throws Exception {
        User user = userService.getUser(token);
        Property property = getPropertyById(propertyId);

        if (!user.getInterestedList().contains(property)) {
            user.getInterestedList().add(property);
        }

        user = userService.saveUser(user);

        return convertToPropertyResponseList(user.getInterestedList());
    }

    @Override
    public List<PropertyResponse> removeFromInterestedList(String token, Long propertyId) throws Exception {
        User user = userService.getUser(token);
        Property property = getPropertyById(propertyId);
        user.getInterestedList().remove(property);
        user = userService.saveUser(user);
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
            insightsRepository.delete(property.getInsights());
            log.info("SERVICE: Insights deleted");
        }

        userService.removePropertyFromAllUserList(property.getId());

        handleDeleteProperty(property);

        log.info("SERVICE: Property deleted successfully");
    }


    private void handlePropertyType(CreatePropertyDTO createPropertyDTO, PropertyDTO propertyDTO, Property property) {
        switch (propertyDTO.getPropertyType()) {
            case Land:
                log.info("Save land called");
                saveLand(createPropertyDTO.getLandDTO(), property.getId());
                break;
            case Shop:
                log.info("save shop called");
                saveShop(createPropertyDTO.getShopDTO(), property.getId());
                break;
            case Flat:
                log.info("save flat called");
                saveFlat(createPropertyDTO.getFlatDTO(), property.getId());
                break;
            case House:
                log.info("save house called");
                saveHouse(createPropertyDTO.getHouseDTO(), property.getId());
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
                deleteLand(property.getId());
                break;
            case Shop:
                log.info("Deleting shop...");
                deleteShop(property.getId());
                break;
            case Flat:
                log.info("Deleting flat...");
                deleteFlat(property.getId());
                break;
            case House:
                log.info("Deleting house...");
                deleteHouse(property.getId());
                break;
            default:
                log.warn("SERVICE: Unknown property type: {}", property.getPropertyType());
                throw new IllegalArgumentException("Unknown property type: " + property.getPropertyType());
        }

        log.info("Deleting property from database...");
        propertyRepository.deleteById(property.getId());

        log.info("Successfully deleted property ID: {}", property.getId());
    }

    private void saveLand(LandDTO landDTO, Long propertyId) {
        log.info("Entered to save land");

        Land land = new Land();
        Optional.ofNullable(landDTO.getArea()).ifPresent(land::setArea);
        Optional.ofNullable(landDTO.getLandType()).ifPresent(land::setLandType);
        land.setPropertyId(propertyId);

        landRepository.save(land);
        log.info("land saved to DB");
    }

    private void saveShop(ShopDTO shopDTO, Long propertyId) {
        log.info("Entered to save shop");
        Shop shop = new Shop();
        Optional.ofNullable(shopDTO.getArea()).ifPresent(shop::setArea);
        Optional.ofNullable(shopDTO.getFloorNo()).ifPresent(shop::setFloorNo);
        Optional.ofNullable(shopDTO.getShopNo()).ifPresent(shop::setShopNo);
        Optional.ofNullable(shopDTO.getIsComplex()).ifPresent(shop::setIsComplex);
        shop.setPropertyId(propertyId);
        shopRepository.save(shop);
        log.info("shop saved to DB");
    }

    private void saveFlat(FlatDTO flatDTO, Long propertyId) {
        log.info("Entered to save flat");

        if (flatDTO == null) return;
        Home home = saveHome(flatDTO.getHomeDTO(), propertyId);
        Flat flat = new Flat();
        Optional.ofNullable(flatDTO.getFloorNo()).ifPresent(flat::setFloorNo);
        Optional.ofNullable(flatDTO.getIsElevator()).ifPresent(flat::setElevator);
        flat.setPropertyId(propertyId);
        flat.setHome(home);
        flatRepository.save(flat);
        log.info("flat saved to DB");
    }

    private void saveHouse(HouseDTO houseDTO, Long propertyId) {
        log.info("Entered to save house");

        if (houseDTO == null) return;
        Home home = saveHome(houseDTO.getHomeDTO(), propertyId);
        House house = new House();
        Optional.ofNullable(houseDTO.getFloors()).ifPresent(house::setFloors);
        Optional.ofNullable(houseDTO.getIsGarage()).ifPresent(house::setIsGarage);
        Optional.ofNullable(houseDTO.getGarageSize()).ifPresent(house::setGarageSize);
        house.setPropertyId(propertyId);
        house.setHome(home);
        houseRepository.save(house);
        log.info("house saved to DB");
    }

    private Home saveHome(HomeDTO homeDTO, Long propertyId) {
        if (homeDTO == null) return null;

        List<Sizes> sizeList = homeDTO.getSizeDTOS().stream().map(sizeDTO -> {
            Sizes sizes = new Sizes();
            Optional.ofNullable(sizeDTO.getHomeAreaType()).ifPresent(sizes::setHomeAreaType);
            Optional.ofNullable(sizeDTO.getSize()).ifPresent(sizes::setSize);
            return sizes;
        }).collect(Collectors.toList());

        Home home = new Home();
        Optional.ofNullable(homeDTO.getArea()).ifPresent(home::setArea);
        Optional.ofNullable(homeDTO.getUsableArea()).ifPresent(home::setUsableArea);
        Optional.ofNullable(homeDTO.getKitchen()).ifPresent(home::setKitchen);
        Optional.ofNullable(homeDTO.getBed()).ifPresent(home::setBed);
        Optional.ofNullable(homeDTO.getLiving()).ifPresent(home::setLiving);
        Optional.ofNullable(homeDTO.getToilet()).ifPresent(home::setToilet);
        Optional.ofNullable(homeDTO.getBathroom()).ifPresent(home::setBathroom);
        Optional.ofNullable(homeDTO.getBalcony()).ifPresent(home::setBalcony);
        Optional.ofNullable(homeDTO.getFurnitureType()).ifPresent(home::setFurnitureType);
        Optional.ofNullable(homeDTO.getGardenType()).ifPresent(home::setGardenType);
        home.setSizes(sizeList);

        return homeRepository.save(home);
    }

    @Transactional
    private void deleteLand(Long propertyId) {
        landRepository.findByPropertyId(propertyId)
                .ifPresentOrElse(landRepository::delete,
                        () -> log.warn("No land found for propertyId: {}", propertyId));
    }

    @Transactional
    private void deleteShop(Long propertyId) {
        shopRepository.findByPropertyId(propertyId)
                .ifPresentOrElse(shopRepository::delete,
                        () -> log.warn("No shop found for propertyId: {}", propertyId));
    }

    @Transactional
    private void deleteHouse(Long propertyId) throws Exception {
        House house = getHouseByPropertyId(propertyId);
        if (house == null) {
            log.warn("No house found for propertyId: {}", propertyId);
            return;
        }

        if (house.getHome() != null) {
            deleteHome(house.getHome().getId());
        } else {
            log.warn("House with ID {} does not have an associated Home.", house.getId());
        }

        houseRepository.deleteById(house.getId());
        log.info("House with ID {} deleted successfully.", house.getId());
    }

    @Transactional
    private void deleteFlat(Long propertyId) throws Exception {
        log.info("Entered to delete flat");
        Flat flat = getFlatByPropertyId(propertyId);
        log.info("flat retrieved");
        if (flat == null) {
            log.warn("No flat found for propertyId: {}", propertyId);
            return;
        }

        if (flat.getHome() != null) {
            deleteHome(flat.getHome().getId());
        } else {
            log.warn("Flat with ID {} does not have an associated Home.", flat.getId());
        }

        flatRepository.deleteById(flat.getId());
        log.info("Flat with ID {} deleted successfully.", flat.getId());
    }

    @Transactional
    private void deleteHome(Long homeId) throws Exception {
        log.info("Entered to delete home");
        if (!homeRepository.existsById(homeId)) {
            log.warn("Home with ID {} not found.", homeId);
            return;
        }
        log.info("home exists");

        homeRepository.deleteById(homeId);
        log.info("home deleted");

        log.info("Home with ID {} deleted successfully.", homeId);
    }


    private Land getLandByPropertyId(Long propertyId) {
        log.info("Entered to get land method");
        Land land= landRepository.findByPropertyId(propertyId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Land not found for propertyId: " + propertyId));
        log.info("Land fetched!");
        return land;
    }

    private Flat getFlatByPropertyId(Long propertyId) {
        return flatRepository.findByPropertyId(propertyId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Land not found for propertyId: " + propertyId));
    }

    private House getHouseByPropertyId(Long propertyId) {
        return houseRepository.findByPropertyId(propertyId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Land not found for propertyId: " + propertyId));
    }

    private Shop getShopByPropertyId(Long propertyId) {
        return shopRepository.findByPropertyId(propertyId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Land not found for propertyId: " + propertyId));
    }

    public PropertyResponse getPropertyResponse(Property property) {
        log.info("SERVICE: Entered to helper.");
        if (property == null) {
            throw new IllegalArgumentException("Property cannot be null");
        }
        log.info("SERVICE: property is not null ->");
        PropertyResponse propertyResponse = new PropertyResponse();
        propertyResponse.setPropertyDTO(propertyHelper.convertPropertyToDTO(property));
        log.info("SERVICE: done to set property dto to response.");

        switch (property.getPropertyType()) {
            case Land ->
                    propertyResponse.setLandDTO(propertyHelper.convertLandToDTO(getLandByPropertyId(property.getId())));
            case Shop ->
                    propertyResponse.setShopDTO(propertyHelper.convertShopToDTO(getShopByPropertyId(property.getId())));
            case Flat ->
                    propertyResponse.setFlatDTO(propertyHelper.convertFlatToDTO(getFlatByPropertyId(property.getId())));
            case House ->
                    propertyResponse.setHouseDTO(propertyHelper.convertHouseToDTO(getHouseByPropertyId(property.getId())));
            default -> throw new IllegalArgumentException("Unknown property type: " + property.getPropertyType());
        }

        log.info("SERVICE: Returns property response");
        return propertyResponse;
    }

    private List<PropertyResponse> convertToPropertyResponseList(List<Property> propertyList){
        return propertyList.stream()
                .map(this::getPropertyResponse)
                .collect(Collectors.toList());
    }


}
