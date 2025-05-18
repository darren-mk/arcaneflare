-- :name get-place-by-id :? :1
select * from place where id = :id;

-- :name get-place-by-handle :? :1
select * from place where handle = :handle;

-- :name insert-place! :! :n
insert into place (
  id, name, handle, address, city,
  district, state, zipcode, country,
  county, region, lat, lon)
values (
  :id, :name, :handle, :address, :city,
  :district, :state, :zipcode, :country,
  :county, :region, :lat, :lon);

-- :name upsert-place! :! :n
insert into place (
  id, name, handle, address, city,
  district, state, zipcode, country,
  county, region, lat, lon)
values (
  :id, :name, :handle, :address, :city,
  :district, :state, :zipcode, :country,
  :county, :region, :lat, :lon)
on conflict (id) do update set
  name = excluded.name,
  address = excluded.address,
  city = excluded.city,
  district = excluded.district,
  state = excluded.state,
  zipcode = excluded.zipcode,
  country = excluded.country,
  county = excluded.county,
  region = excluded.region,
  lat = excluded.lat,
  lon = excluded.lon;