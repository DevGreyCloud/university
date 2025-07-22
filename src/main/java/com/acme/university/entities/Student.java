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
@ToString
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

    @ManyToMany
    @JoinTable(
            name = "student_lecturer",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "lecturer_id")
    )
    @Builder.Default
    @ToString.Exclude
    private Set<Lecturer> lecturers = new HashSet<>();

    public void addLecturer(Lecturer lecturer) {
        lecturer.getStudents().add(this);
        lecturers.add(lecturer);
    }
}