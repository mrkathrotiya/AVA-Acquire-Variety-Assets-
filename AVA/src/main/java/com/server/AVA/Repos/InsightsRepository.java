package com.server.AVA.Repos;

import com.server.AVA.Models.Address;
import com.server.AVA.Models.Insights;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsightsRepository extends JpaRepository<Insights,Long> {

}
