-- :name insert-member! :! :n
insert into member (
  id, username, email, role, passcode_hash, created_at)
values (
  :id, :username, :email, :role, :passcode_hash, now());

-- :name get-member-by-id :? :1
select * from member
where id = :id;

-- :name get-member-by-username :? :1
select * from member
where username = :username;

-- :name get-member-by-email :? :1
select * from member
where email = :email;

-- :name update-member-passcode! :! :n
update member
set passcode_hash = :passcode_hash,
    edited_at = now()
where id = :id;

-- :name list-members :? :*
select id, username, role
from member
order by created_at desc;

-- :name update-last-login! :! :n
update member
set last_login = now()
where id = :id;