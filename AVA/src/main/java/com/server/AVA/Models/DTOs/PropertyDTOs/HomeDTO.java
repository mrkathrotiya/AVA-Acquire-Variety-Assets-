package com.server.AVA.Models.DTOs.PropertyDTOs;

import com.server.AVA.Models.enums.FurnitureType;
import com.server.AVA.Models.enums.GardenType;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeDTO {
    private Integer area;
    private Integer usableArea;
    private Short kitchen;
    private Short bed;
    private Short living;
    private Short toilet;
    private Short bathroom;
    private Short balcony;
    private FurnitureType furnitureType;
    private GardenType gardenType;
    private List<SizeDTO> sizeDTOS;

}
