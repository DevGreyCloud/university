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
@Table(name = "lecturer")
public class Lecturer {
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
            joinColumns = @JoinColumn(name = "lecturer_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    @Builder.Default
    private Set<Student> students = new HashSet<>();

    public void addStudent(Student student) {
        students.add(student);
        student.getLecturers().add(this);
    }

    public void removeStudent(Student student) {
        students.remove(student);
        student.getLecturers().remove(this);
    }
}