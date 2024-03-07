create table sessions (
    id uuid primary key,
    person_id uuid not null,
    expiration timestamp not null,
    constraint fk_person
        foreign key (person_id)
        references persons (id)
        on delete set null);
