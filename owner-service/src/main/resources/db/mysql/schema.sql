-- Active: 1709780909689@@127.0.0.1@3306@petapp
CREATE TABLE IF NOT EXISTS `owner` (
    user_id BIGINT PRIMARY key,
    money INTEGER NOT NULL 
);

DROP TABLE  IF EXISTS `owned_prop`;

CREATE Table IF NOT EXISTS `owned_prop` (
    id BIGINT  PRIMARY KEY AUTO_INCREMENT,
    prop_id INTEGER ,
    owner_id BIGINT NOT NULL, 
    amount INTEGER not null
);

ALTER TABLE `owned_prop`
ADD CONSTRAINT fk_owned_prop_prop_id FOREIGN KEY 
(owner_id) REFERENCES `owner`(user_id);


DROP TABLE IF EXISTS `t_pets` ;

CREATE Table IF NOT EXISTS  `t_pets` (
    pet_id INTEGER PRIMARY KEY,
     owner_id BIGINT 
);

ALTER TABLE `t_pets`
ADD CONSTRAINT fk_pet_id_owner FOREIGN KEY (owner_id) REFERENCES `owner`(user_id);

INSERT INTO `owner`() VALUES
()
;