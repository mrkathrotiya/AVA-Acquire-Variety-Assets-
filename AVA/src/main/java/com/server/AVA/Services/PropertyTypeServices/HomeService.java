package com.server.AVA.Services.PropertyTypeServices;

import com.server.AVA.Models.DTOs.PropertyDTOs.HomeDTO;
import com.server.AVA.Models.Home;

public interface HomeService {
    Home getHomeById(Long homeId) throws Exception;
    Home saveHome(HomeDTO homeDTO) throws Exception;
    void deleteHome(Long homeId) throws Exception;
    Home updateHome(HomeDTO homeDTO,Long homeId) throws Exception;
}
