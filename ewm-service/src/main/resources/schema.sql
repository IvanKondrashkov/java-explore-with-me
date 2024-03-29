DROP TABLE IF EXISTS users, friendship, categories, compilations, events, compilations_events, requests cascade;

--create table users--
CREATE TABLE IF NOT EXISTS users(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    UNIQUE(email)
);

--create table friendship--
CREATE TABLE IF NOT EXISTS friendship(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    is_friend BOOLEAN NOT NULL,
    created TIMESTAMP NOT NULL,
    initiator_id BIGINT REFERENCES users(id),
    friend_id BIGINT REFERENCES users(id)
);

--create table categories--
CREATE TABLE IF NOT EXISTS categories(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    UNIQUE(name)
);

--create table compilations--
CREATE TABLE IF NOT EXISTS compilations(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title VARCHAR(300) NOT NULL,
    pinned BOOLEAN NOT NULL
);

--create table locations--
CREATE TABLE IF NOT EXISTS locations(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    lat FLOAT NOT NULL,
    lon FLOAT NOT NULL
);

--create table events--
CREATE TABLE IF NOT EXISTS events(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title VARCHAR(120) NOT NULL,
    annotation VARCHAR(2000) NOT NULL,
    description VARCHAR(7000) NOT NULL,
    event_date TIMESTAMP NOT NULL,
    created_on TIMESTAMP NOT NULL,
    published_on TIMESTAMP,
    paid BOOLEAN NOT NULL,
    request_moderation BOOLEAN NOT NULL,
    participant_limit INT NOT NULL,
    state VARCHAR(9) NOT NULL,
    location_id BIGINT REFERENCES locations(id),
    category_id BIGINT REFERENCES categories(id),
    initiator_id BIGINT REFERENCES users(id)
);

--create table requests--
CREATE TABLE IF NOT EXISTS requests(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    created TIMESTAMP NOT NULL,
    status VARCHAR NOT NULL,
    requester_id BIGINT REFERENCES users(id),
    event_id BIGINT REFERENCES events(id)
);

--create table compilations_events--
CREATE TABLE IF NOT EXISTS compilations_events(
    compilation_id BIGINT REFERENCES compilations(id),
    event_id BIGINT REFERENCES events(id)
);