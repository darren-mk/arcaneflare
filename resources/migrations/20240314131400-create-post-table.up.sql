create table post (
    id uuid primary key,
    title varchar(60) not null,
    subject varchar(20) not null,
    curb varchar(20) not null,
    detail text not null,
    location jsonb not null,
    author_id uuid not null,
    created_at timestamp not null,
    updated_at timestamp not null,
    constraint fk_author
        foreign key (author_id)
            references person(id));
