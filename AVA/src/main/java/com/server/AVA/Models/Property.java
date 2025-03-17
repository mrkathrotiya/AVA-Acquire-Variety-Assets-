package com.server.AVA.Models;

import com.server.AVA.Models.enums.AreaType;
import com.server.AVA.Models.enums.PropertyType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cache.annotation.Cacheable;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Property{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @Column(length = 1000)
    private List<String> images;

    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;

    @ElementCollection
    @Column(length = 1000)
    private List<String> paperImages;

    private Long price;

    private Long rent;

    private Boolean forSell;

    private Date dateOfUpload = new Date();

    @OneToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id", unique = true)
    private Address address;

    @OneToOne
    @JoinColumn(name = "insights_id", referencedColumnName = "id", unique = true)
    private Insights insights;

    @Enumerated(EnumType.STRING)
    private AreaType areaType;

}
