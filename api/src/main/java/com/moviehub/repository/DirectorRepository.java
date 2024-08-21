package com.moviehub.repository;

import com.moviehub.entity.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DirectorRepository extends JpaRepository<Director, String> {

    Optional<Director> findByName(String name);

    boolean existsByName(String name);

}
