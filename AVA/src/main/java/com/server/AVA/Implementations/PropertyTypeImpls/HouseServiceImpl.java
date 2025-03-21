package com.server.AVA.Implementations.PropertyTypeImpls;

import com.server.AVA.Models.DTOs.PropertyDTOs.HouseDTO;
import com.server.AVA.Models.Home;
import com.server.AVA.Models.House;
import com.server.AVA.Repos.HouseRepository;
import com.server.AVA.Services.PropertyTypeServices.HomeService;
import com.server.AVA.Services.PropertyTypeServices.HouseService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class HouseServiceImpl implements HouseService {
    private static final Logger log = LoggerFactory.getLogger(HouseServiceImpl.class);
    private final HouseRepository houseRepository;
    private final HomeService homeService;

    @Override
    @Cacheable(value = "HOUSE", key = "#propertyId")
    public House getHouseByPropertyId(Long propertyId) throws Exception {
        return houseRepository.findByPropertyId(propertyId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Land not found for propertyId: " + propertyId));
    }

    @Override
    @Transactional
    public void saveHouse(HouseDTO houseDTO, Long propertyId) throws Exception {
        log.info("Entered to save house");

        if (houseDTO == null) return;
        Home home = homeService.saveHome(houseDTO.getHomeDTO());
        House house = new House();
        Optional.ofNullable(houseDTO.getFloors()).ifPresent(house::setFloors);
        Optional.ofNullable(houseDTO.getIsGarage()).ifPresent(house::setIsGarage);
        Optional.ofNullable(houseDTO.getGarageSize()).ifPresent(house::setGarageSize);
        house.setPropertyId(propertyId);
        house.setHome(home);
        houseRepository.save(house);
        log.info("house saved to DB");
    }

    @Override
    @Transactional
    @CacheEvict(value = "HOUSE", key = "#propertyId")
    public void deleteHouse(Long propertyId) throws Exception {
        House house = getHouseByPropertyId(propertyId);
        if (house == null) {
            log.warn("No house found for propertyId: {}", propertyId);
            return;
        }

        if (house.getHome() != null) {
            homeService.deleteHome(house.getHome().getId());
        } else {
            log.warn("House with ID {} does not have an associated Home.", house.getId());
        }

        houseRepository.deleteById(house.getId());
        log.info("House with ID {} deleted successfully.", house.getId());
    }

    @Override
    @Transactional
    @CachePut(value = "HOUSE", key = "#propertyId")
    public House updateHouse(HouseDTO houseDTO, Long propertyId) throws Exception {
        log.info("HOUSE SERVICE: Entered to update house!");
        House house = getHouseByPropertyId(propertyId);
        log.info("FLAT SERVICE: House fetched!");
        Optional.ofNullable(houseDTO.getFloors()).ifPresent(house::setFloors);
        Optional.ofNullable(houseDTO.getGarageSize()).ifPresent(house::setGarageSize);
        Optional.ofNullable(houseDTO.getIsGarage()).ifPresent(house::setIsGarage);
        if (houseDTO.getHomeDTO() != null){
            house.setHome(homeService.updateHome(houseDTO.getHomeDTO(),house.getHome().getId()));
        }
        return houseRepository.save(house);
    }
}