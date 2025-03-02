package com.server.AVA.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Flat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Short floorNo;

    private Boolean elevator;

    @OneToOne
    @JoinColumn(name = "home_id", referencedColumnName = "id", unique = true)
    private Home home;

    @Column(name = "property_id")
    private Long propertyId;

}
