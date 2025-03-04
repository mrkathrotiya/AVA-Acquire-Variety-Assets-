package com.server.AVA.Models;

import com.server.AVA.Models.enums.AreaType;
import com.server.AVA.Models.enums.HomeAreaType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity

public class Sizes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Enumerated(EnumType.STRING)
    private HomeAreaType homeAreaType;

    private Integer size;
}
