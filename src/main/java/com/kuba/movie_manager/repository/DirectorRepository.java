package com.kuba.movie_manager.repository;

import com.kuba.movie_manager.entity.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Long> {

}
