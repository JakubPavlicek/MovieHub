package com.moviehub.repository;

import com.moviehub.entity.Actor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Repository interface for managing Actor entities.
@Repository
public interface ActorRepository extends JpaRepository<Actor, UUID> {

    Page<Actor> findAllByName(String name, Pageable pageable);

    Optional<Actor> findByName(String name);

    boolean existsByName(String name);

}
