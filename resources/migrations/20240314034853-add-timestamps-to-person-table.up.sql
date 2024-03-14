alter table person
add column created_at timestamp,
add column edited_at timestamp;
--;;
update person
set created_at = now();
--;;
update person
set edited_at = now();
--;;
alter table person
alter column created_at set not null,
alter column edited_at set not null;
