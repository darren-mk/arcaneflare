alter table place
    drop column if exists website_url,
    drop column if exists twitter_url,
    drop column if exists instagram_url,
    drop column if exists facebook_url;
--;;
CREATE TABLE place_social (
    place_id UUID REFERENCES place(id) ON DELETE CASCADE,
    platform TEXT CHECK (platform IN ('twitter', 'instagram', 'facebook', 'website')),
    url TEXT NOT NULL,
    PRIMARY KEY (place_id, platform)
);
