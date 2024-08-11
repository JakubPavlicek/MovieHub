package com.movie_manager.repository;

import com.movie_manager.entity.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DirectorRepository extends JpaRepository<Director, String> {

    Optional<Director> findByName(String name);

    boolean existsByName(String name);

}
