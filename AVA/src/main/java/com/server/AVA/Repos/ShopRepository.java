package com.server.AVA.Repos;

import com.server.AVA.Models.Flat;
import com.server.AVA.Models.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop,Long> {
    Optional<Shop> findByPropertyId(Long propertyId);
}
