package com.server.AVA.Implimantations.PropertyTypeImpls;

import com.server.AVA.Models.DTOs.PropertyDTOs.ShopDTO;
import com.server.AVA.Models.Shop;
import com.server.AVA.Repos.ShopRepository;
import com.server.AVA.Services.PropertyTypeServices.ShopService;
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
public class ShopServiceImpl implements ShopService {
    private static final Logger log = LoggerFactory.getLogger(ShopServiceImpl.class);
    private final ShopRepository shopRepository;
    @Override
    @Cacheable(value = "SHOP", key = "#propertyId")
    public Shop getShopByPropertyId(Long propertyId) throws Exception {
        return shopRepository.findByPropertyId(propertyId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Land not found for propertyId: " + propertyId));
    }

    @Override
    @Transactional
    public void saveShop(ShopDTO shopDTO, Long propertyId) throws Exception {
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

    @Override
    @Transactional
    @CacheEvict(value = "SHOP", key = "#propertyId")
    public void deleteShop(Long propertyId) throws Exception {
        shopRepository.findByPropertyId(propertyId)
                .ifPresentOrElse(shopRepository::delete,
                        () -> log.warn("No shop found for propertyId: {}", propertyId));
    }

    @Override
    @Transactional
    @CachePut(value = "SHOP", key = "#propertyId")
    public Shop updateShop(ShopDTO shopDTO, Long propertyId) throws Exception {
        log.info("SHOP SERVICE: Entered to update shop!");
        Shop shop = getShopByPropertyId(propertyId);
        log.info("SHOP SERVICE: Shop fetched!");

        Optional.ofNullable(shopDTO.getArea()).ifPresent(shop::setArea);
        Optional.ofNullable(shopDTO.getShopNo()).ifPresent(shop::setShopNo);
        Optional.ofNullable(shopDTO.getFloorNo()).ifPresent(shop::setFloorNo);
        Optional.ofNullable(shopDTO.getIsComplex()).ifPresent(shop::setIsComplex);
        log.info("SHOP SERVICE: Parameter settled!");
        shopRepository.save(shop);
        log.info("SHOP SERVICE: Shop updated!");
        return shop;
    }
}