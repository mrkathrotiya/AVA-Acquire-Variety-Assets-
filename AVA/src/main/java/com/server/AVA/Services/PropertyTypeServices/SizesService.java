package com.server.AVA.Services.PropertyTypeServices;

import com.server.AVA.Models.DTOs.PropertyDTOs.LandDTO;
import com.server.AVA.Models.DTOs.PropertyDTOs.SizeDTO;
import com.server.AVA.Models.Land;
import com.server.AVA.Models.Sizes;

public interface SizesService {
    Sizes getSizesById(Long sizeId);
    void updateSizes(Long sizesId, SizeDTO sizeDTO);
}
