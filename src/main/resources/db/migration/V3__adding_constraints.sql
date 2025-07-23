alter table lecturer
    add constraint lecturer_name_surname
        unique (name, surname);

alter table student
    add constraint student_name_surname
        unique (name, surname);

alter table lecturer
    add constraint lecturer_name_alphanumeric
        check (name REGEXP '^[a-zA-Z0-9]+$');

alter table lecturer
    add constraint lecturer_surname_alphanumeric
        check (surname REGEXP '^[a-zA-Z0-9]+$');

alter table student
    add constraint student_name_alphanumeric
        check (name REGEXP '^[a-zA-Z0-9]+$');

alter table student
    add constraint student_surname_alphanumeric
        check (surname REGEXP '^[a-zA-Z0-9]+$');