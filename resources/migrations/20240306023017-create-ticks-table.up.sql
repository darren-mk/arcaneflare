create table ticks (
    id uuid primary key default gen_random_uuid(),
    created_at timestamp default now());
