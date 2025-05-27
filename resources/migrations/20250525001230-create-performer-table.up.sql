-- members table already exists
CREATE TABLE performer (
    member_id UUID PRIMARY KEY REFERENCES member(id) ON DELETE CASCADE,
    display_name TEXT,
    bio TEXT,
    created_at TIMESTAMP DEFAULT now(),
    edited_at TIMESTAMP
);