package com.moviehub.repository;

import com.moviehub.entity.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DirectorRepository extends JpaRepository<Director, UUID> {

    Optional<Director> findByName(String name);

    boolean existsByName(String name);

}
