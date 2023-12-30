drop table if exists continents;
--;;
create table if not exists continents (
    id smallint primary key,
    full_name text NOT NULL );
