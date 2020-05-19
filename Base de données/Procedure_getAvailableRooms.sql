/* Procédure permettant d'avoir les chambres disponibles pour des dates données */
DELIMITER $$
CREATE OR REPLACE PROCEDURE getAvailableRooms(arrival_date VARCHAR(10), exit_date VARCHAR(10))
BEGIN  
  SELECT ID FROM ROOMS
  WHERE ID NOT IN (
    SELECT DISTINCT OC.ROOM_ID FROM RESERVATIONS RE
    LEFT JOIN OCCUPATIONS OC ON RE.ID = OC.RESERVATION_ID
    WHERE DATE(arrival_date) >= RE.ARRIVAL_DATE AND DATE(exit_date) <= RE.EXIT_DATE);
END;
$$

/* Exemple d'utilisation de la procédure */
call getAvailableRooms("2020-05-15","2020-05-18")



