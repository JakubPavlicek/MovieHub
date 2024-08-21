package com.moviehub.repository;

import com.moviehub.entity.ProductionCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductionCompanyRepository extends JpaRepository<ProductionCompany, String> {

    Optional<ProductionCompany> findByName(String name);

    boolean existsByName(String name);

}
