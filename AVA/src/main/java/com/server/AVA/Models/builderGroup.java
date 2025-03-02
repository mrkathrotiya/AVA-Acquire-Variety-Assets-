package com.server.AVA.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class builderGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String phone;

    private String email;

    private String headOffice;

    @OneToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id", unique = true)
    private Address address;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "builder_group_property",
            joinColumns = @JoinColumn(name = "builderGroup_id"),
            inverseJoinColumns = @JoinColumn(name = "property_id"))
    private List<Property> interestedList;

}
