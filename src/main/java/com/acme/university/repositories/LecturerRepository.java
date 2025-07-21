package com.acme.university.repositories;

import com.acme.university.entities.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LecturerRepository extends JpaRepository<Lecturer, Long> {
    Optional<Lecturer> findByNameAndSurname(String name, String surname);
}