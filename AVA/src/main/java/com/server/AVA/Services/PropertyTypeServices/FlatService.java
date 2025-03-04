package com.server.AVA.Services.PropertyTypeServices;

import com.server.AVA.Models.DTOs.PropertyDTOs.FlatDTO;
import com.server.AVA.Models.DTOs.PropertyDTOs.LandDTO;
import com.server.AVA.Models.Flat;
import com.server.AVA.Models.Land;

public interface FlatService {
    Flat getFlatByPropertyId(Long propertyId) throws Exception;
    void saveFlat(FlatDTO flatDTO, Long propertyId) throws Exception;
    void deleteFlat(Long propertyId) throws Exception;
    void updateFlat(FlatDTO flatDTO, Long propertyId) throws Exception;
}
