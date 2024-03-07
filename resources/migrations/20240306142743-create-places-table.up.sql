create table places (
    id uuid primary key,
    industry industries not null,
    label varchar(60) not null,
    handle varchar(80) not null,
    nudity nudities not null,
    status statuses not null,
    blob jsonb not null,
    address_id uuid not null,
    constraint fk_address
        foreign key (address_id)
        references addresses (id)
        on delete set null);
