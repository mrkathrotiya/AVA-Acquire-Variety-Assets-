package com.server.AVA.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "home_id", referencedColumnName = "id", unique = true)
    private Home home;

    private Short floors;

    private Boolean isGarage;

    private Integer garageSize;

    @Column(name = "property_id")
    private Long propertyId;

}
