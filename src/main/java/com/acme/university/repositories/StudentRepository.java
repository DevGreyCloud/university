package com.acme.university.repositories;

import com.acme.university.entities.Lecturer;
import com.acme.university.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByNameAndSurname(String name, String surname);
}
