-- This migration is not reversible because it drops data.
-- Restoring nation/state/county/city/district requires a backup.

-- To reverse, you'd need to:
-- 1. Re-add the columns (shown below),
-- 2. Populate them from saved data (not available here).

ALTER TABLE post
    ADD COLUMN nation TEXT,
    ADD COLUMN state TEXT,
    ADD COLUMN county TEXT,
    ADD COLUMN city TEXT,
    ADD COLUMN district TEXT,
    DROP COLUMN geo_id;

-- Note: All added fields will be NULL.
