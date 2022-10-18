drop table if exists jc_student_child;
drop table if exists jc_student_order;
drop table if exists jc_passport_office;
drop table if exists jc_register_office;
drop table if exists jc_country_struct;
drop table if exists jc_university;
drop table if exists jc_street;

create table jc_street
(
    street_code integer not null,
    street_name varchar(300) not null,
    primary key (street_code)
);

create table jc_university
(
    university_id integer not null,
    university_name varchar(300) not null,
    primary key (university_id)
);

create table jc_country_struct
(
    area_id char(12) not null,
    area_name varchar(200),
    primary key (area_id)
);

create table jc_passport_office
(
    p_office_id integer not null,
    p_office_area_id char(12) not null,
    p_office_name varchar(200),
    primary key (p_office_id),
    foreign key (p_office_area_id) references jc_country_struct (area_id) on delete restrict
);

create table jc_register_office
(
    r_office_id integer not null,
    r_office_area_id char(12) not null,
    r_office_name varchar(200),
    primary key (r_office_id),
    foreign key (r_office_area_id) references jc_country_struct (area_id) on delete restrict
);

create table jc_student_order
(
    student_order_id serial,
    student_order_status int,
    student_order_date timestamp not null,
    h_sur_name varchar(100) not null,
    h_given_name varchar(100) not null,
    h_patronymic varchar(100) not null,
    h_date_of_birth date not null,
    h_passport_series varchar(4) not null,
    h_passport_number varchar(6) not null,
    h_passport_date date not null,
    h_passport_office_id integer not null,
    h_post_index varchar(10),
    h_street_code integer not null,
    h_building varchar(10) not null,
    h_extension varchar(10),
    h_apartment varchar(10),
    h_university_id integer not null,
    h_student_number varchar(30),
    w_sur_name varchar(100) not null,
    w_given_name varchar(100) not null,
    w_patronymic varchar(100) not null,
    w_date_of_birth date not null,
    w_passport_series varchar(4) not null,
    w_passport_number varchar(6) not null,
    w_passport_date date not null,
    w_passport_office_id integer not null,
    w_post_index varchar(10),
    w_street_code integer not null,
    w_building varchar(10) not null,
    w_extension varchar(10),
    w_apartment varchar(10),
    w_university_id integer not null,
    w_student_number varchar(30),
    certificate_id varchar(20) not null,
    register_office_id integer not null,
    marriage_date date not null,
    primary key (student_order_id),
    foreign key (h_street_code) references jc_street (street_code) on delete restrict,
    foreign key (h_passport_office_id) references jc_passport_office (p_office_id) on delete restrict,
    foreign key (h_university_id) references jc_university (university_id) on delete restrict,
    foreign key (w_street_code) references jc_street (street_code) on delete restrict,
    foreign key (w_passport_office_id) references jc_passport_office (p_office_id) on delete restrict,
    foreign key (w_university_id) references jc_university (university_id) on delete restrict,
    foreign key (register_office_id) references jc_register_office (r_office_id) on delete restrict
);

create table jc_student_child
(
    student_child_id serial,
    student_order_id integer,
    ch_sur_name varchar(100) not null,
    ch_given_name varchar(100) not null,
    ch_patronymic varchar(100) not null,
    ch_date_of_birth date not null,
    ch_certificate_number varchar(10) not null,
    ch_certificate_date date not null,
    ch_register_office_id integer not null,
    ch_post_index varchar(10),
    ch_street_code integer not null,
    ch_building varchar(10) not null,
    ch_extension varchar(10),
    ch_apartment varchar(10),
    primary key (student_child_id),
    foreign key (ch_street_code) references jc_street (street_code) on delete restrict,
    foreign key (ch_register_office_id) references jc_register_office (r_office_id) on delete restrict
);

create index idx_student_order_status on jc_student_order(student_order_status);

create index idx_student_order_id on jc_student_child(student_order_id);
