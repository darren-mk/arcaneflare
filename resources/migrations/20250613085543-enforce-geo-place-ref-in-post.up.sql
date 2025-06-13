ALTER TABLE post
    ADD CONSTRAINT post_place_or_geo_check
        CHECK (
            (place_id IS NOT NULL OR geo_id IS NOT NULL)
            AND NOT (place_id IS NOT NULL AND geo_id IS NOT NULL));
