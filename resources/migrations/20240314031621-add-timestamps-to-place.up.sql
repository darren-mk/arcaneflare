alter table place
add column created_at timestamp,
add column edited_at timestamp;
--;;
update place
set created_at = now();
--;;
update place
set edited_at = now();
--;;
alter table place
alter column created_at set not null,
alter column edited_at set not null;
