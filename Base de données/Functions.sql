/* Fonction permettant d'avoir la promotion pour les clients réguliers */
DELIMITER $$
CREATE OR REPLACE FUNCTION getRegularClientDiscount()
RETURNS INTEGER(2)
RETURN 20;
$$

/* Fonction permettant d'avoir la promotion de groupes */
DELIMITER $$
CREATE OR REPLACE FUNCTION getGroupDiscount()
RETURNS INTEGER(2)
RETURN 10;
$$

