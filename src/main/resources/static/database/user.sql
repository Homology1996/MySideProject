CREATE SEQUENCE user_key_seq START 100;

CREATE TABLE user_info(
	user_key INTEGER UNIQUE NOT NULL,
	account TEXT NOT NULL,
	password TEXT,
	PRIMARY KEY (user_key, account)
);