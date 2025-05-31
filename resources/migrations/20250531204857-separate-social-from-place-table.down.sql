drop table if exists place_social;
--;;
alter table place
    add column website_url text,
    add column twitter_url text,
    add column instagram_url text,
    add column facebook_url text;
