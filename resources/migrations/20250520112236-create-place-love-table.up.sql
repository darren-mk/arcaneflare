create table place_love (
  member_id uuid references member(id),
  place_id uuid references place(id),
  loved_at timestamptz not null default now(),
  primary key (member_id, place_id));
