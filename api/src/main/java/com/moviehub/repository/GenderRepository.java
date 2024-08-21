package com.moviehub.repository;

import com.moviehub.entity.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenderRepository extends JpaRepository<Gender, String> {

    Optional<Gender> findByName(String name);

}
