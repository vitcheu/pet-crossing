-- Active: 1709780909689@@127.0.0.1@3306@petapp
CREATE DATABASE IF NOT EXISTS petApp;
GRANT ALL PRIVILEGES ON petApp.* TO pc@localhost IDENTIFIED BY 'pc';

USE petApp;

CREATE TABLE IF NOT EXISTS types (
  id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(80),
  INDEX(name)
) engine=InnoDB;

DROP TABLE if EXISTS `pets` ;

CREATE TABLE IF NOT EXISTS pets (
  id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(30),
  birth_date DATE,
  type_id INT(4) UNSIGNED NOT NULL,
  owner_id INT(4) UNSIGNED NOT NULL,
  properties_id INT(4) not null,
  INDEX(name),
  FOREIGN KEY (type_id) REFERENCES types(id)
) engine=InnoDB;


  /* FOREIGN KEY (owner_id) REFERENCES owners(id), */

CREATE TABLE IF NOT EXISTS pet_properties(
  id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  atk INT(4) ,
  def INT(4),
  hp INT(4),
  mp INT(4),
  speed INT(3),
  luck INT(3),
  favorability INT(2)
) engine=InnoDB;