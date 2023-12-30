create table if not exists states (
    id smallint primary key,
    countries_id smallint not null references countries(id),
    code text not null,
    full_name text not null );
