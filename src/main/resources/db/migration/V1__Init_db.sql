CREATE TABLE IF NOT EXISTS User_name  (
 
    id serial NOT NULL PRIMARY KEY,
    name varchar NOT NULL,
    created timestamp NOT NULL,
    updated timestamp,
    status int NOT NULL

);

CREATE TABLE IF NOT EXISTS Event (

    id serial NOT NULL PRIMARY KEY,
    user_id int NOT NULL,
    created timestamp NOT NULL,
    updated timestamp,
    status int NOT NULL,
     CONSTRAINT fk
       FOREIGN KEY (user_id) REFERENCES User_name (id)

);

CREATE TABLE IF NOT EXISTS File  (

    id serial NOT NULL PRIMARY KEY,
    event_id int NOT NULL,
    name varchar NOT NULL,
    filePath varchar NOT NULL,
    created timestamp NOT NULL,
    updated timestamp,
    status int NOT NULL,
    CONSTRAINT fk
           FOREIGN KEY (event_id) REFERENCES Event (id)

);

