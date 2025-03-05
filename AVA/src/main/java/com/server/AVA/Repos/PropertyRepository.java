package com.server.AVA.Repos;

import com.server.AVA.Models.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property,Long> {

    @Query("SELECT p FROM Property p WHERE p.price > :price AND p.forSell = :forSell")
    List<Property> getPropertiesByFilter(@Param("price") Long price, @Param("forSell") Boolean forSell);
}
