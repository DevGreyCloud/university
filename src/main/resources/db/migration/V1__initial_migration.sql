create table lecturer
(
    id      bigint auto_increment
        primary key,
    name    varchar(255) not null,
    surname varchar(255) not null
);

create table student
(
    id      bigint auto_increment
        primary key,
    name    varchar(255) not null,
    surname varchar(255) not null
);

