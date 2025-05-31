CREATE TABLE performer_social (
    performer_member_id UUID REFERENCES performer(member_id) ON DELETE CASCADE,
    platform TEXT CHECK (platform IN ('twitter', 'instagram', 'facebook', 'website')),
    url TEXT NOT NULL,
    PRIMARY KEY (performer_member_id, platform)
);
