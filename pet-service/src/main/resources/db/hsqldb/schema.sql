DROP TABLE pets IF EXISTS;
DROP TABLE types IF EXISTS;
DROP TABLE owners IF EXISTS;

CREATE TABLE types (
  id   INTEGER IDENTITY PRIMARY KEY,
  name VARCHAR(80)
);
CREATE INDEX types_name ON types (name);

CREATE TABLE owners (
  id         INTEGER IDENTITY PRIMARY KEY,
  name VARCHAR(30),
  address    VARCHAR(255),
);

CREATE TABLE pets (
  id         INTEGER IDENTITY PRIMARY KEY,
  name       VARCHAR(30),
  birth_date DATE,
  type_id    INTEGER NOT NULL,
  owner_id   INTEGER NOT NULL,
  properties_id INTEGER not null
);
ALTER TABLE pets ADD CONSTRAINT fk_pets_owners FOREIGN KEY (owner_id) REFERENCES owners (id);
ALTER TABLE pets ADD CONSTRAINT fk_pets_types FOREIGN KEY (type_id) REFERENCES types (id);
CREATE INDEX idx_pets_name ON pets (name);
CREATE INDEX idx_properties ON pets (properties_id );



DROP TABLE pet_properties IF EXISTS;

CREATE TABLE  pet_properties(
  id   INTEGER PRIMARY KEY,
  atk INTEGER ,
  def INTEGER,
  hp INTEGER,
  mp INTEGER,
  speed INTEGER,
  luck INTEGER,
  favorability INTEGER
);

ALTER TABLE pets 
ADD CONSTRAINT fk_properties
  FOREIGN KEY (properties_id) 
  REFERENCES  pet_properties(id); 