-- Active: 1709780909689@@127.0.0.1@3306@petapp
CREATE DATABASE IF NOT EXISTS petApp;
GRANT ALL PRIVILEGES ON petApp.* TO pc@localhost IDENTIFIED BY 'pc';

USE petApp;
DROP TABLE IF EXISTS props;

CREATE TABLE IF NOT EXISTS props (
  id INT   PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(30),
  type_id    INTEGER NOT NULL,
  quantity INTEGER NOT NULL ,
  description varchar(255),
  price INTEGER NOT NULL
) engine=InnoDB;
