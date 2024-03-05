create table persons (
    id uuid primary key,
    nickname varchar(15) not null unique,
    email varchar(40) not null unique,
    password varchar(16) not null,
    job jobs not null,
    verified boolean not null);
