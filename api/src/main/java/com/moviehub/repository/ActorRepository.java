package com.moviehub.repository;

import com.moviehub.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ActorRepository extends JpaRepository<Actor, UUID> {

    Optional<Actor> findByName(String name);

    boolean existsByName(String name);
}
