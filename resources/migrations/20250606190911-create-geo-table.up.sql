CREATE TABLE geo (
	id UUID PRIMARY KEY,
    kind TEXT NOT NULL CHECK (kind IN ('nation', 'state',
    'region', 'county', 'city', 'borough', 'district')),
	full_name TEXT NOT NULL,
	parent_id UUID REFERENCES geo(id),
	is_ending BOOLEAN NOT NULL);
