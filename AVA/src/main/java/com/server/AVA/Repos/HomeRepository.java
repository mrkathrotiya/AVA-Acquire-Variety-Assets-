package com.server.AVA.Repos;

import com.server.AVA.Models.Home;
import com.server.AVA.Models.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HomeRepository extends CrudRepository<Home,Long> {
    Optional<Home> findHomeById(Long homeId);
}
