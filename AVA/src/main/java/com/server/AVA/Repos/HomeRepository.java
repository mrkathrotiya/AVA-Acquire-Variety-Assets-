package com.server.AVA.Repos;

import com.server.AVA.Models.Home;
import com.server.AVA.Models.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeRepository extends JpaRepository<Home,Long> {

}
