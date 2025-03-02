package com.server.AVA.Models;

import com.server.AVA.Models.enums.AreaType;
import com.server.AVA.Models.enums.FurnitureType;
import com.server.AVA.Models.enums.GardenType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Home {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer area;

    private Integer usableArea;

    private Short kitchen;

    private Short bed;

    private Short living;

    private Short toilet;

    private Short bathroom;

    private Short balcony;

    @Enumerated(EnumType.STRING)
    private GardenType gardenType;

    @Enumerated(EnumType.STRING)
    private FurnitureType furnitureType;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "home_sizes",
            joinColumns = @JoinColumn(name = "home_id"),
            inverseJoinColumns = @JoinColumn(name = "sizes_id"))
    private List<Sizes> sizes;

}
