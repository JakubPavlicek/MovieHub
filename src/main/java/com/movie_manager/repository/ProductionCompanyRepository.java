package com.movie_manager.repository;

import com.movie_manager.entity.ProductionCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductionCompanyRepository extends JpaRepository<ProductionCompany, String> {

    Optional<ProductionCompany> findByName(String name);

    boolean existsByName(String name);

}
