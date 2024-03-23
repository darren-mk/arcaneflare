create table file (
    id uuid primary key,
    post_id uuid not null,
    kind varchar(15) not null,
    object_key varchar(30) not null,
    designation varchar(50) not null,
    size integer not null,
    created_at timestamp not null,
    constraint fk_post
        foreign key (post_id)
            references post(id));
