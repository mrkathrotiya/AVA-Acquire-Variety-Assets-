package com.server.AVA.Services.PropertyTypeServices;

import com.server.AVA.Models.DTOs.PropertyDTOs.LandDTO;
import com.server.AVA.Models.Land;

public interface LandService{
    Land getLandByPropertyId(Long propertyId) throws Exception;
    void saveLand(LandDTO landDTO, Long propertyId) throws Exception;
    void deleteLand(Long propertyId) throws Exception;
    Land updateLand(LandDTO landDTO, Long propertyId) throws Exception;
}
