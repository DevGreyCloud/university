create table student_lecturer
(
    student_id  bigint not null,
    lecturer_id bigint not null,
    constraint student_lecturer_pk
        primary key (lecturer_id, student_id),
    constraint student_lecturer_lecturer_id_fk
        foreign key (lecturer_id) references lecturer (id)
            on delete cascade,
    constraint student_lecturer_student_id_fk
        foreign key (student_id) references student (id)
            on delete cascade
);

