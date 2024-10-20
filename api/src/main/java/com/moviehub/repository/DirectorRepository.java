package com.moviehub.repository;

import com.moviehub.entity.Director;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Repository interface for managing Director entities.
@Repository
public interface DirectorRepository extends JpaRepository<Director, UUID> {

    Optional<Director> findByName(String name);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, UUID directorId);

    Page<Director> findAllByName(String name, Pageable pageable);

}
