alter table persons
rename to person;
--;;
alter table person
rename column id to person_id;
--;;
alter table person
drop column job;
--;;
alter table person
add column job varchar(15);
--;;
update person
set job = 'customer';
--;;
alter table person
alter column job set not null;
