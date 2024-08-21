package com.moviehub.repository;

import com.moviehub.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, String> {

    Optional<Genre> findByName(String name);

    boolean existsByName(String name);

}
