create table place (
  id uuid primary key,
  name text not null,
  handle text not null unique,
  address text,
  city text,
  district text,
  state text,
  zipcode text,
  country text,
  county text,
  region text,
  lat double precision,
  lon double precision
);