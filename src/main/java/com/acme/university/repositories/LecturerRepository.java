package com.acme.university.repositories;

import com.acme.university.entities.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface LecturerRepository extends JpaRepository<Lecturer, Long> {
}
