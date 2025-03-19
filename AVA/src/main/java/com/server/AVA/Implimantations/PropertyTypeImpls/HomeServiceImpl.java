package com.server.AVA.Implimantations.PropertyTypeImpls;

import com.server.AVA.Models.DTOs.PropertyDTOs.FlatDTO;
import com.server.AVA.Models.DTOs.PropertyDTOs.HomeDTO;
import com.server.AVA.Models.DTOs.PropertyDTOs.SizeDTO;
import com.server.AVA.Models.Flat;
import com.server.AVA.Models.Home;
import com.server.AVA.Models.Sizes;
import com.server.AVA.Repos.FlatRepository;
import com.server.AVA.Repos.HomeRepository;
import com.server.AVA.Services.PropertyTypeServices.FlatService;
import com.server.AVA.Services.PropertyTypeServices.HomeService;
import com.server.AVA.Services.PropertyTypeServices.SizesService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HomeServiceImpl implements HomeService {
    private static final Logger log = LoggerFactory.getLogger(HomeServiceImpl.class);
    private final HomeRepository homeRepository;

    @Override
    @Cacheable(value = "HOME", key = "#homeId")
    public Home getHomeById(Long homeId) throws Exception {
        Home home = homeRepository.findHomeById(homeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Home not found for Id: " + homeId));
        log.info("Home fetched!");
        return home;
    }

    @Override
    @Transactional
    public Home saveHome(HomeDTO homeDTO) throws Exception {
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

    @Override
    @Transactional
    @CacheEvict(value = "HOME", key = "#homeId")
    public void deleteHome(Long homeId) throws Exception {
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

    @Override
    @Transactional
    @CachePut(value = "HOME", key = "#homeId")
    public Home updateHome(HomeDTO homeDTO,Long homeId) throws Exception {
        log.info("HOME SERVICE: Entered to update home!");
        Home home = getHomeById(homeId);
        log.info("HOME SERVICE: Sizes fetched!");
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

        List<Sizes> existingSizes = home.getSizes();

        if (existingSizes != null && homeDTO.getSizeDTOS() != null) {
            List<Sizes> updatedSizes = new ArrayList<>();

            for (Sizes existingSize : existingSizes) {
                Optional<SizeDTO> matchingSizeDTO = homeDTO.getSizeDTOS().stream()
                        .filter(sizeDTO -> existingSize.getHomeAreaType().equals(sizeDTO.getHomeAreaType()))
                        .findFirst();

                matchingSizeDTO.ifPresent(sizeDTO -> {
                    existingSize.setHomeAreaType(sizeDTO.getHomeAreaType());
                    existingSize.setSize(sizeDTO.getSize());
                });

                updatedSizes.add(existingSize);
            }

            home.getSizes().retainAll(updatedSizes);

            List<Sizes> newSizes = homeDTO.getSizeDTOS().stream()
                    .filter(sizeDTO -> existingSizes.stream()
                            .noneMatch(existingSize -> existingSize.getHomeAreaType().equals(sizeDTO.getHomeAreaType())))
                    .map(sizeDTO -> {
                        Sizes newSize = new Sizes();
                        newSize.setHomeAreaType(sizeDTO.getHomeAreaType());
                        newSize.setSize(sizeDTO.getSize());
                        return newSize;
                    })
                    .toList();
            home.getSizes().addAll(newSizes);
        }
        return homeRepository.save(home);
    }
}