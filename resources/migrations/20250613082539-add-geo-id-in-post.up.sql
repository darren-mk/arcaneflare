ALTER TABLE post
    ADD COLUMN geo_id UUID REFERENCES geo(id),
    DROP COLUMN nation,
    DROP COLUMN state,
    DROP COLUMN county,
    DROP COLUMN city,
    DROP COLUMN district;
