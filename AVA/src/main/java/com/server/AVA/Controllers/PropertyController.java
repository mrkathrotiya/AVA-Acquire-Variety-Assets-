package com.server.AVA.Controllers;

import com.server.AVA.Models.DTOs.PropertyDTOs.*;
import com.server.AVA.Models.Property;
import com.server.AVA.Models.Seller;
import com.server.AVA.Models.Sizes;
import com.server.AVA.Models.User;
import com.server.AVA.Repos.PropertyRepository;
import com.server.AVA.Repos.SellerRepository;
import com.server.AVA.Repos.SizesRepository;
import com.server.AVA.Services.PropertyService;
import com.server.AVA.Services.PropertyTypeServices.SizesService;
import com.server.AVA.Services.UserService;
import lombok.AllArgsConstructor;
import org.hibernate.engine.jdbc.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/property")
@AllArgsConstructor
public class PropertyController {
    private static final Logger log = LoggerFactory.getLogger(PropertyController.class);
    private final PropertyService propertyService;
    private final UserService userService;
    private final SellerRepository sellerRepository;

    @PostMapping("/add")
    public ResponseEntity<PropertyDTO> createProperty(@RequestHeader("Authorization") String token,
                                                      @RequestBody CreatePropertyDTO createPropertyDTO) throws Exception {
        Objects.requireNonNull(token);
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        log.info("Property Controller, request goes to service");
        return ResponseEntity.ok(propertyService.createProperty(token,createPropertyDTO));
    }

    @GetMapping("/{propertyId}")
    public ResponseEntity<PropertyResponse> getProperty(@PathVariable Long propertyId) throws Exception{
        if (propertyId == null) throw new Exception("Property Id can't be null!");
        log.info("prop. id is not null in controller!");
        log.info("Entering to property service..");
        return ResponseEntity.ok(propertyService.getWholePropertyById(propertyId));
    }

    @PostMapping("/add-to-interested-list/{propertyId}")
    public ResponseEntity<List<PropertyResponse>> addToInterestedList(@RequestHeader("Authorization") String token,
                                                                      @PathVariable Long propertyId) throws Exception {
        if (token.startsWith("Bearer ")) {
            token = Objects.requireNonNull(token.substring(7));
        }
        return ResponseEntity.ok(propertyService.addToInterestedList(token,propertyId));
    }

    @PutMapping("/delete-from-interested-list/{propertyId}")
    public ResponseEntity<List<PropertyResponse>> removeFromInterestedList(@RequestHeader("Authorization") String token,
                                                                           @PathVariable Long propertyId) throws Exception{
        if (token.startsWith("Bearer ")) {
            token = Objects.requireNonNull(token.substring(7));
        }
        return ResponseEntity.ok(propertyService.removeFromInterestedList(token,propertyId));
    }

    @GetMapping("/get-interested-list")
    public ResponseEntity<List<PropertyResponse>> getInterestedList(@RequestHeader("Authorization") String token) throws Exception{
        if (token.startsWith("Bearer ")) {
            token = Objects.requireNonNull(token.substring(7));
        }
            return ResponseEntity.ok(propertyService.getInterestedList(token));
    }

    @DeleteMapping("/delete/{propertyId}")
    @Transactional
    public ResponseEntity<String> deleteProperty(@RequestHeader("Authorization") String token,
                                                           @PathVariable Long propertyId) throws Exception{
        if (token.startsWith("Bearer ")) {
            token = Objects.requireNonNull(token.substring(7));
        }
        propertyService.deleteProperty(token,propertyId);
        return ResponseEntity.ok("Deleted..");
    }

    @PutMapping("/update/{propertyId}")
    @Transactional
    public ResponseEntity<PropertyResponse> updateProperty(@PathVariable Long propertyId,
                                                           @RequestBody UpdatePropertyDTO updatePropertyDTO) throws Exception {
        return ResponseEntity.ok(propertyService.updateProperty(propertyId,
                updatePropertyDTO));
    }

    @GetMapping("/get-list/{price}/{forSell}")
    @Transactional
    public ResponseEntity<List<PropertyResponse>> getList(@PathVariable Long price,
                                                          @PathVariable Boolean forSell) throws Exception {
        return ResponseEntity.ok(propertyService.getList(price,forSell));


    }


}

