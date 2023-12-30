create table if not exists countries (
    id smallint primary key,
    continent_id smallint not null references continents(id),
    full_name text not null );
