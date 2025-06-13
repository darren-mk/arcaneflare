ALTER TABLE post
    DROP CONSTRAINT IF EXISTS post_kind_check,
    DROP COLUMN kind;
