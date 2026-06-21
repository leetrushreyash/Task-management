package com.project.todo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.todo.entity.UserEntry;

@Repository
public interface UserRepository extends JpaRepository<UserEntry, Long> {
    public boolean existsByEmail(String email);

    Optional<UserEntry> findByEmail(String email);

    List<UserEntry> findAll();
}
