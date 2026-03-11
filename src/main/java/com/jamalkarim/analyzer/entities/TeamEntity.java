package com.jamalkarim.analyzer.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "team")
public class TeamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "teamEntity", cascade = CascadeType.ALL)
    private List<PlayerEntity> roster = new ArrayList<>();
}
