package com.server.AVA.Services.PropertyTypeServices;

import com.server.AVA.Models.DTOs.PropertyDTOs.LandDTO;
import com.server.AVA.Models.DTOs.PropertyDTOs.ShopDTO;
import com.server.AVA.Models.Land;
import com.server.AVA.Models.Shop;

public interface ShopService {
    Shop getShopByPropertyId(Long propertyId) throws Exception;
    void saveShop(ShopDTO shopDTO, Long propertyId) throws Exception;
    void deleteShop(Long propertyId) throws Exception;
    void updateShop(ShopDTO shopDTO, Long propertyId) throws Exception;
}
