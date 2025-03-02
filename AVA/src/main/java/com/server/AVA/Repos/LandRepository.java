package com.server.AVA.Repos;

import com.server.AVA.Models.Land;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LandRepository extends JpaRepository<Land,Long> {
    Optional<Land> findByPropertyId(Long propertyId);
}
