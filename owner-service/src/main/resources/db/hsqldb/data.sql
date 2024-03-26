/*
 * Engine: MySQL
 * Version: 0.0.1
 * Description: Initial database structure
 */

/*
 * Owners
 */
INSERT INTO `owner` VALUES (1,200);

INSERT INTO `owner` VALUES (2,200);

INSERT INTO `owner` VALUES (3,200);
INSERT INTO `owner` VALUES (4,200);
INSERT INTO `owner` VALUES (5,200);
INSERT INTO `owner` VALUES (6,200);
INSERT INTO `owner` VALUES (7,200);
INSERT INTO `owner` VALUES (8,200);
INSERT INTO `owner` VALUES (9,200);
INSERT INTO `owner` VALUES (10,200);




/*
 * Props
 */
INSERT INTO
    `owned_prop` (
        `id`, `prop_id`, `owner_id`, `amount`
    )
VALUES (0, 1, 1, 1);

INSERT INTO
    `owned_prop` (
        `id`, `prop_id`, `owner_id`, `amount`
    )
VALUES (1, 2, 1, 2);

INSERT INTO
    `owned_prop` (
        `id`, `prop_id`, `owner_id`, `amount`
    )
VALUES (2, 3, 2, 1);

INSERT INTO
    `owned_prop` (
        `id`, `prop_id`, `owner_id`, `amount`
    )
VALUES (3, 4, 3, 5);

/*
 * Pets
 */
INSERT INTO
    `t_pets` (`pet_id`, `owner_id`)
VALUES (1, 1),
    (2, 2),

    (3, 3),
    (4, 3),

    (5, 4),
    (6, 5),

    (7, 6),
    (8, 6),

    (9, 7),
    (10, 8),
    (11, 9),

    (12, 10),
    (13, 10)
    ;