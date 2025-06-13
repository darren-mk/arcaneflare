ALTER TABLE post
    ADD COLUMN parent_id UUID REFERENCES post(id);
