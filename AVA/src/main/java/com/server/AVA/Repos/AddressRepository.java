package com.server.AVA.Repos;

import com.server.AVA.Models.Address;
import com.server.AVA.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {

}
