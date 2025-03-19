package com.server.AVA.Implementations.PropertyTypeImpls;

import com.server.AVA.Models.DTOs.PropertyDTOs.LandDTO;
import com.server.AVA.Models.Land;
import com.server.AVA.Repos.LandRepository;
import com.server.AVA.Services.PropertyTypeServices.LandService;
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
public class LandServiceImpl implements LandService {
    private static final Logger log = LoggerFactory.getLogger(LandServiceImpl.class);
    private final LandRepository landRepository;

    @Override
    @Cacheable(value = "LAND", key = "#propertyId")
    public Land getLandByPropertyId(Long propertyId) throws Exception {
        log.info("Entered to get land method");
        Land land= landRepository.findByPropertyId(propertyId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Land not found for propertyId: " + propertyId));
        log.info("Land fetched!");
        return land;
    }

    @Override
    @Transactional
    public void saveLand(LandDTO landDTO, Long propertyId) throws Exception {
        log.info("Entered to save land");

        Land land = new Land();
        Optional.ofNullable(landDTO.getArea()).ifPresent(land::setArea);
        Optional.ofNullable(landDTO.getLandType()).ifPresent(land::setLandType);
        land.setPropertyId(propertyId);

        landRepository.save(land);
        log.info("land saved to DB");
    }

    @Override
    @Transactional
    @CacheEvict(value = "LAND", key = "#propertyId")
    public void deleteLand(Long propertyId) throws Exception {
        landRepository.findByPropertyId(propertyId)
                .ifPresentOrElse(landRepository::delete,
                        () -> log.warn("No land found for propertyId: {}", propertyId));
    }

    @Override
    @Transactional
    @CachePut(value = "LAND", key = "#propertyId")
    public Land updateLand(LandDTO landDTO, Long propertyId) throws Exception {
        log.info("LAND SERVICE: Entered to update land");
        Land land = getLandByPropertyId(propertyId);
        log.info("LAND SERVICE: Land fetched");
        Optional.ofNullable(landDTO.getArea()).ifPresent(land::setArea);
        Optional.ofNullable(landDTO.getLandType()).ifPresent(land::setLandType);
        log.info("LAND SERVICE: attribute settled");
        log.info("LAND SERVICE: Land updated");
        return landRepository.save(land);
    }
}
