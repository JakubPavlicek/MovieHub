package com.movie_manager.repository;

import com.movie_manager.entity.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenderRepository extends JpaRepository<Gender, String> {

    Optional<Gender> findByName(String name);

}
