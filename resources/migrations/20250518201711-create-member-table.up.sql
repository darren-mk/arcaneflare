create table member (
  id uuid primary key,
  username text not null unique,
  email text not null unique,
  role text not null check (role in ('performer', 'customer', 'staff', 'admin')),
  passcode_hash text not null,
  created_at timestamptz not null default now(),
  edited_at timestamptz);