create type jobs as enum (
    'customer', 'provider',
    'owner', 'staff');
--;;
create table people (
    id uuid primary key,
    username varchar(15) not null unique,
    email varchar(40) not null unique,
    job jobs not null,
    verified boolean not null,
    created_at timestamp not null,
    edited_at timestamp not null);
