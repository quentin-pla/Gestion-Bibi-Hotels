/* Procédure permettant d'avoir les chambres disponibles dans une certaine ville, pour des dates données */
DELIMITER $$
CREATE OR REPLACE PROCEDURE getAvailableRoomsByCity(city VARCHAR(30), arrival_date VARCHAR(10), exit_date VARCHAR(10))
BEGIN
  SELECT RO.ID FROM ROOMS RO
  LEFT JOIN HOTELS HO ON HO.ID = RO.HOTEL_ID
  WHERE HO.CITY LIKE CONCAT("%",city,"%")
  AND RO.ID NOT IN (
    SELECT DISTINCT OC.ROOM_ID FROM RESERVATIONS RE
    LEFT JOIN OCCUPATIONS OC ON RE.ID = OC.RESERVATION_ID
    WHERE DATE(arrival_date) >= RE.ARRIVAL_DATE AND DATE(exit_date) <= RE.EXIT_DATE);
END;
$$

/* Exemple d'exécution de la procédure */
call getAvailableRoomsByCity("Marseille", "2020-05-15","2020-05-18")



