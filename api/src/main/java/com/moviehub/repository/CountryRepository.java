package com.moviehub.repository;

import com.moviehub.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CountryRepository extends JpaRepository<Country, UUID> {

    Optional<Country> findByName(String name);

    boolean existsByName(String name);

}
