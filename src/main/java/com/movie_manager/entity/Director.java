package com.movie_manager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Director {

    @Id
    @SequenceGenerator(
        name = "director_id_generator",
        sequenceName = "director_id_seq",
        allocationSize = 1
    )
    @GeneratedValue(
        generator = "director_id_generator",
        strategy = GenerationType.SEQUENCE
    )
    private Long id;

    @Column(
        length = 50,
        nullable = false,
        unique = true
    )
    private String name;

}
