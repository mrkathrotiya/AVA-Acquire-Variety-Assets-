package com.server.AVA.Repos;

import com.server.AVA.Models.Address;
import com.server.AVA.Models.Sizes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SizesRepository extends JpaRepository<Sizes,Long> {
    Optional<Sizes> findSizesById(Long sizesId);
}
