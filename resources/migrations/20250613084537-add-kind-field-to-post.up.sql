ALTER TABLE post
    ADD COLUMN kind TEXT NOT NULL DEFAULT 'discussion',
    ADD CONSTRAINT post_kind_check
        CHECK (kind IN ('review', 'discussion', 'news', 'article'));
