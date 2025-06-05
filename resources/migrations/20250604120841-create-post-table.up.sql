CREATE TABLE post (
    id UUID PRIMARY KEY,
    author_id UUID NOT NULL REFERENCES member(id),
    title TEXT NOT NULL,
    body TEXT NOT NULL,
    nation TEXT,
    state TEXT,
    county TEXT,
    city TEXT,
    district TEXT,
    place_id UUID REFERENCES place(id),
    created_at TIMESTAMPTZ DEFAULT now(),
    updated_at TIMESTAMPTZ
);
