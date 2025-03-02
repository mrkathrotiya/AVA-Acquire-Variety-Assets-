package com.server.AVA.Repos;

import com.server.AVA.Models.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HouseRepository extends JpaRepository<House,Long> {
    Optional<House> findByPropertyId(Long propertyId);
}
