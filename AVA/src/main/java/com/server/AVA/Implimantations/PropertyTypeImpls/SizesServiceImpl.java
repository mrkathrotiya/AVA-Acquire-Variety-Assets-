package com.server.AVA.Implimantations.PropertyTypeImpls;

import com.server.AVA.Models.DTOs.PropertyDTOs.SizeDTO;
import com.server.AVA.Models.Sizes;
import com.server.AVA.Repos.SizesRepository;
import com.server.AVA.Services.PropertyTypeServices.SizesService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SizesServiceImpl implements SizesService {
    private static final Logger log = LoggerFactory.getLogger(SizesServiceImpl.class);
    private final SizesRepository sizesRepository;
    @Override
    public Sizes getSizesById(Long sizeId) {
        return sizesRepository.findSizesById(sizeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sizes not found for size is: "+sizeId
                ));
    }

    @Override
    @Transactional
    public void updateSizes(Long sizesId, SizeDTO sizeDTO) {
        log.info("SIZES SERVICE: Entered to update sizes");
        Sizes sizes = getSizesById(sizesId);
        log.info("SIZES SERVICE: Size fetched!");
        Optional.ofNullable(sizeDTO.getHomeAreaType()).ifPresent(sizes::setHomeAreaType);
        Optional.ofNullable(sizeDTO.getSize()).ifPresent(sizes::setSize);
        log.info("SIZES SERVICE: Parameter settled");
        sizesRepository.save(sizes);
    }

}
