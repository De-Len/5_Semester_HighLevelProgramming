create table users (
    id bigint auto_increment primary key,
    login varchar(50) not null unique,
    password_hash varchar(255) not null,
    salt varchar(255) not null
);

create table resources (
    id bigint auto_increment primary key,
    path varchar(255) not null unique,
    max_volume int not null
);

create table permissions (
    id bigint auto_increment primary key,
    user_id bigint not null,
    resource_id bigint not null,
    role varchar(20) not null
);