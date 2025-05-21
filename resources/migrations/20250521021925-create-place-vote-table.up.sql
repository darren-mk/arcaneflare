create table place_vote (
    member_id uuid references member(id),
    place_id uuid references place(id),
    score smallint not null check (score in (-1, 1)),
    voted_at timestamptz not null default now(),
    primary key (member_id, place_id)
);
