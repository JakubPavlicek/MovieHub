package com.movie_manager.repository;

import com.movie_manager.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActorRepository extends JpaRepository<Actor, String> {

    Optional<Actor> findByName(String name);
}
