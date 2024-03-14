alter table place
alter column industry type varchar(20),
alter column nudity type varchar(20),
alter column status type varchar(20);
--;;
alter table place
rename column industry to sector;
