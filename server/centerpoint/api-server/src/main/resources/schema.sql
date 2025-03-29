drop table if exists place_search_string;
drop table if exists places;


CREATE TABLE IF NOT EXISTS place_search_string(
  id SERIAL PRIMARY KEY,
  name varchar
);

CREATE TABLE IF NOT EXISTS places (
  id SERIAL PRIMARY KEY,
  place_id VARCHAR,
  osm_id VARCHAR,
  osm_type VARCHAR,
  display_name VARCHAR,
  lat NUMERIC,
  lon NUMERIC,
  address json,
  place_search_string_id INTEGER,
  place_search_string VARCHAR
);

CREATE TABLE IF NOT EXISTS users (
  username VARCHAR(512) PRIMARY KEY NOT NULL,
  password VARCHAR(2048) NOT NULL,
  enabled BOOLEAN DEFAULT FALSE NOT NULL
);

CREATE TABLE IF NOT EXISTS user_roles (
  id SERIAL PRIMARY KEY,
  username VARCHAR(512) NOT NULL,
  role VARCHAR(512) NOT NULL,
  UNIQUE (role, username),
  FOREIGN KEY (username) REFERENCES users(username)
);


