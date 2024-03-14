create table commentary (
    id uuid primary key,
    post_id uuid not null,
    annotator_id uuid not null,
    content text not null,
    created_at timestamp not null,
    updated_at timestamp not null,
    constraint fk_post
        foreign key (post_id)
            references post(id),
    constraint fk_annotator
        foreign key (annotator_id)
            references person(id));
