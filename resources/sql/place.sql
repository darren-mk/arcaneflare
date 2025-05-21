-- :name get-place-by-id :? :1
select * from place where id = :id;

-- :name get-place-by-handle :? :1
select * from place where handle = :handle;

-- :name get-full-list :? :*
select id, handle, name
from place
order by name;

-- :name insert-place! :! :n
insert into place (
  id, name, handle, address, city, district, state,
  zipcode, country, county, region, lat, lon, phone_number,
  website_url, twitter_url, instagram_url, facebook_url)
values (
  :id, :name, :handle, :address, :city, :district, :state,
  :zipcode, :country, :county, :region, :lat, :lon, :phone_number,
  :website_url, :twitter_url, :instagram_url, :facebook_url)

-- :name upsert-place! :! :n
insert into place (
  id, name, handle, address, city, district, state,
  zipcode, country, county, region, lat, lon, phone_number,
  website_url, twitter_url, instagram_url, facebook_url)
values (
  :id, :name, :handle, :address, :city, :district, :state,
  :zipcode, :country, :county, :region, :lat, :lon, :phone_number,
  :website_url, :twitter_url, :instagram_url, :facebook_url)
on conflict (id) do update set
  name = excluded.name,
  handle = excluded.handle,
  address = excluded.address,
  city = excluded.city,
  district = excluded.district,
  state = excluded.state,
  zipcode = excluded.zipcode,
  country = excluded.country,
  county = excluded.county,
  region = excluded.region,
  lat = excluded.lat,
  lon = excluded.lon,
  phone_number = excluded.phone_number,
  website_url = excluded.website_url,
  twitter_url = excluded.twitter_url,
  instagram_url = excluded.instagram_url,
  facebook_url = excluded.facebook_url;

-- :name love-place! :! :n
insert into place_love (member_id, place_id)
values (:member_id, :place_id)
on conflict do nothing;

-- :name unlove-place! :! :n
delete from place_love
where member_id = :member_id
and place_id = :place_id;

-- :name get-place-loves :? :1
select count(*) as loves
from place_love
where place_id = :place_id;

-- :name get-member-loved-places :? :*
select place.*
from place_love
join place on place.id = place_love.place_id
where place_love.member_id = :member_id;

-- :name vote-place! :! :n
insert into place_vote (member_id, place_id, score)
values (:member_id, :place_id, :score)
on conflict (member_id, place_id) do update
set score = excluded.score,
    voted_at = now();

-- :name remove-vote! :! :n
delete from place_vote
where member_id = :member_id and place_id = :place_id;

-- :name get-vote-score :? :1
select sum(score) as score
from place_vote
where place_id = :place_id;

-- :name get-member-vote :? :1
select score
from place_vote
where member_id = :member_id and place_id = :place_id;

-- :name insert-thumbnail! :! :n
insert into place_thumbnail (
    id, place_id, image_url,
    alt_text, caption, position)
values (
    :id, :place_id, :image_url,
    :alt_text, :caption, :position);

-- :name get-thumbnails-for-place :? :*
select * from place_thumbnail
where place_id = :place_id
order by position;

-- :name delete-thumbnail! :! :n
delete from place_thumbnail
where id = :id;
