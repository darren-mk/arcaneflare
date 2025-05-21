create table place_thumbnail (
    id uuid primary key,
    place_id uuid references place(id) on delete cascade,
    image_url text not null,
    alt_text text,
    caption text,
    position integer not null default 0,
    uploaded_at timestamptz not null default now()
);
