package com.moviehub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Country {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String countryId;

    @Column(
        length = 15,
        nullable = false,
        unique = true
    )
    private String name;

}
