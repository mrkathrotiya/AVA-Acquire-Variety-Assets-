package com.server.AVA.Models;

import com.server.AVA.Models.enums.AreaType;
import com.server.AVA.Models.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shop implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer area;

    private Short floorNo;

    private int shopNo;

    private Boolean isComplex;

    @Column(name = "property_id")
    private Long propertyId;
}
