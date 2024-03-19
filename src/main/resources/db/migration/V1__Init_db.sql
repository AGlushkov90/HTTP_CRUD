CREATE TABLE IF NOT EXISTS users  (
 
    id serial NOT NULL PRIMARY KEY,
    name varchar NOT NULL,
    created timestamp NOT NULL,
    updated timestamp,
    status int NOT NULL

);

CREATE TABLE IF NOT EXISTS files  (

    id serial NOT NULL PRIMARY KEY,
    name varchar NOT NULL,
    filePath varchar NOT NULL,
    created timestamp NOT NULL,
    updated timestamp,
    status int NOT NULL,
    data bytea NOT NULL
);

CREATE TABLE IF NOT EXISTS events (

    id serial NOT NULL PRIMARY KEY,
    user_id int NOT NULL,
    file_id int NOT NULL,
    created timestamp NOT NULL,
    updated timestamp,
    status int NOT NULL,
    CONSTRAINT fk_events_users FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_events_files FOREIGN KEY (file_id) REFERENCES files (id)

);



