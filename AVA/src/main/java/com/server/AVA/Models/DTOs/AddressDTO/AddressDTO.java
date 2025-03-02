package com.server.AVA.Models.DTOs.AddressDTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddressDTO {
    private String street;
    private String area;
    private String city;
    private String state;
    private List<String> nearbyFamousPlaces;
}
