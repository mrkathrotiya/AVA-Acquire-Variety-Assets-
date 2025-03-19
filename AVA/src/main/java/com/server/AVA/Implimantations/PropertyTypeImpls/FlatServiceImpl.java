package com.server.AVA.Implimantations.PropertyTypeImpls;

import com.server.AVA.Models.DTOs.PropertyDTOs.FlatDTO;
import com.server.AVA.Models.DTOs.PropertyDTOs.LandDTO;
import com.server.AVA.Models.DTOs.PropertyDTOs.ShopDTO;
import com.server.AVA.Models.Flat;
import com.server.AVA.Models.Home;
import com.server.AVA.Models.Shop;
import com.server.AVA.Repos.FlatRepository;
import com.server.AVA.Repos.ShopRepository;
import com.server.AVA.Services.PropertyTypeServices.FlatService;
import com.server.AVA.Services.PropertyTypeServices.HomeService;
import com.server.AVA.Services.PropertyTypeServices.ShopService;
import com.server.AVA.Services.RedisService;
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
public class FlatServiceImpl implements FlatService {
    private static final Logger log = LoggerFactory.getLogger(FlatServiceImpl.class);
    private final FlatRepository flatRepository;
    private final HomeService homeService;
    private final RedisService redisService;

    @Override
    @Cacheable(value = "FLAT", key = "#propertyId")
    public Flat getFlatByPropertyId(Long propertyId) throws Exception {
        return flatRepository.findByPropertyId(propertyId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Land not found for propertyId: " + propertyId));
    }

    @Override
    @Transactional
    public void saveFlat(FlatDTO flatDTO, Long propertyId) throws Exception {
        log.info("Entered to save flat");

        if (flatDTO == null) return;
        Home home = homeService.saveHome(flatDTO.getHomeDTO());
        Flat flat = new Flat();
        Optional.ofNullable(flatDTO.getFloorNo()).ifPresent(flat::setFloorNo);
        Optional.ofNullable(flatDTO.getIsElevator()).ifPresent(flat::setElevator);
        flat.setPropertyId(propertyId);
        flat.setHome(home);
        flatRepository.save(flat);
        log.info("flat saved to DB");
    }

    @Override
    @Transactional
    @CacheEvict(value = "FLAT", key = "#propertyId")
    public void deleteFlat(Long propertyId) throws Exception {
        log.info("Entered to delete flat");
        Flat flat = getFlatByPropertyId(propertyId);
        log.info("flat retrieved");
        if (flat == null) {
            log.warn("No flat found for propertyId: {}", propertyId);
            return;
        }

        if (flat.getHome() != null) {
            homeService.deleteHome(flat.getHome().getId());
        } else {
            log.warn("Flat with ID {} does not have an associated Home.", flat.getId());
        }

        flatRepository.deleteById(flat.getId());
        log.info("Flat with ID {} deleted successfully.", flat.getId());
    }

    @Override
    @Transactional
    @CachePut(value = "FLAT", key = "#propertyId")
    public Flat updateFlat(FlatDTO flatDTO, Long propertyId) throws Exception {
        log.info("FLAT SERVICE: Entered to update flat!");
        Flat flat = getFlatByPropertyId(propertyId);
        log.info("FLAT SERVICE: Flat fetched!");
        Optional.ofNullable(flatDTO.getFloorNo()).ifPresent(flat::setFloorNo);
        Optional.ofNullable(flatDTO.getIsElevator()).ifPresent(flat::setElevator);
        if (flatDTO.getHomeDTO() != null){
            flat.setHome(homeService.updateHome(flatDTO.getHomeDTO(),flat.getHome().getId()));
        }
        return flatRepository.save(flat);
    }
}