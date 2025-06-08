ALTER TABLE place
    ADD COLUMN nation TEXT,
    ADD COLUMN state TEXT,
    ADD COLUMN county TEXT,
    ADD COLUMN city TEXT,
    ADD COLUMN district TEXT,
    DROP COLUMN geo_id;
