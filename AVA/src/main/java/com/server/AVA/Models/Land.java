package com.server.AVA.Models;

import com.server.AVA.Models.enums.LandType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Land {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer area;

    @Enumerated(EnumType.STRING)
    private LandType landType;

        @Column(name = "property_id")
        private Long propertyId;
}
