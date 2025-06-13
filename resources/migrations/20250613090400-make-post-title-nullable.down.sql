-- WARNING: This will fail if any rows have NULL in title.
-- Make sure to update or delete such rows before running this.

ALTER TABLE post
    ALTER COLUMN title SET NOT NULL;
