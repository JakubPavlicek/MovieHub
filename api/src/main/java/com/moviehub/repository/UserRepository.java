package com.moviehub.repository;

import com.moviehub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Repository interface for managing User entities.
@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
