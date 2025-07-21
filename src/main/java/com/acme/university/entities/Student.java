package com.acme.university.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @ManyToMany(mappedBy = "students")
    @ToString.Exclude
    @Builder.Default
    private Set<Lecturer> lecturers = new HashSet<>();

    public void addLecturer(Lecturer lecturer) {
        lecturers.add(lecturer);
        lecturer.getStudents().add(this);
    }

    public void removeLecturer(Lecturer lecturer) {
        lecturers.remove(lecturer);
        lecturer.getStudents().remove(this);
    }
}