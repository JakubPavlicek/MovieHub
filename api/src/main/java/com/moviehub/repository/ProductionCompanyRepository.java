package com.moviehub.repository;

import com.moviehub.entity.ProductionCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductionCompanyRepository extends JpaRepository<ProductionCompany, UUID> {

    Optional<ProductionCompany> findByName(String name);

    boolean existsByName(String name);

}
