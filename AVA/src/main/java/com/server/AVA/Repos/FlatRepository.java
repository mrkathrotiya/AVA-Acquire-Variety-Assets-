package com.server.AVA.Repos;

import com.server.AVA.Models.Flat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlatRepository extends JpaRepository<Flat,Long> {
    Optional<Flat> findByPropertyId(Long propertyId);
}
