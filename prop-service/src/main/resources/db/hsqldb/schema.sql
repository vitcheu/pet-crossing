DROP TABLE props IF EXISTS;

/* DROP TABLE types IF EXISTS; */

CREATE TABLE props (
  id INTEGER IDENTITY PRIMARY KEY,
  name VARCHAR(30),
  type_id    INTEGER NOT NULL,
  quantity INTEGER NOT NULL ,
  description varchar(255),
  price INTEGER NOT NULL
);
CREATE INDEX idx_name ON props (name);

/* CREATE TABLE types (
  id   INTEGER IDENTITY PRIMARY KEY,
  name VARCHAR(80)
);
CREATE INDEX types_name ON types (name); */


/* ALTER TABLE `props` ADD CONSTRAINT fk_props_types FOREIGN KEY (type_id) REFERENCES types (id); */