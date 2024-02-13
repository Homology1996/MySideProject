CREATE SEQUENCE daily_record_seq START 100;

CREATE TABLE daily_record(
	record_key INTEGER UNIQUE NOT NULL,
	record_date TEXT NOT NULL,
	user_key INTEGER NOT NULL,
	food INTEGER DEFAULT 0,
	clothes INTEGER DEFAULT 0,
	entertainment INTEGER DEFAULT 0,
	accommodation INTEGER DEFAULT 0,
	transportation INTEGER DEFAULT 0,
	PRIMARY KEY (record_key)
);