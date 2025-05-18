alter table place
  drop column if exists phone_number,
  drop column if exists website_url,
  drop column if exists twitter_url,
  drop column if exists instagram_url,
  drop column if exists facebook_url;