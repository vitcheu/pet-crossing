DROP TABLE owner IF EXISTS;

CREATE TABLE `owner` (
    user_id BIGINT PRIMARY key,
    money INTEGER NOT NULL 
);

DROP TABLE owned_prop IF EXISTS;

CREATE Table owned_prop (
    id BIGINT PRIMARY key,
     prop_id INTEGER,
      owner_id INTEGER NOT NULL,
       amount INTEGER not null
);

DROP TABLE t_pets IF EXISTS;

CREATE Table t_pets (
    pet_id INTEGER PRIMARY KEY,
     owner_id BIGINT
);

ALTER TABLE t_pets
ADD CONSTRAINT fk_pet_id_owner FOREIGN KEY (owner_id) REFERENCES owner (user_id);