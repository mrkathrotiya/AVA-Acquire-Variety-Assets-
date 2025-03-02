package com.server.AVA.Repos;

import com.server.AVA.Models.Address;
import com.server.AVA.Models.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller,Long> {
    Optional<Seller> getSellerByUserId(Long userId) throws Exception;
}
