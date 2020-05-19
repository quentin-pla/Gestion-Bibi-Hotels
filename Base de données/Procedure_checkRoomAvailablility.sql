/* Procédure permettant d'avoir les chambres disponibles pour un hotel donné, une sélection de jours et un type de chambre */
DELIMITER $$
CREATE OR REPLACE PROCEDURE checkRoomAvailablility(hotel_id INTEGER, arrival_date VARCHAR(10), exit_date VARCHAR(10), room_type_id INTEGER)
BEGIN
  SELECT RO.ID FROM ROOMS RO
  WHERE RO.HOTEL_ID=hotel_id AND RO.ROOMTYPE_ID=room_type_id
  AND RO.ID NOT IN (
    SELECT DISTINCT OC.ROOM_ID FROM RESERVATIONS RE
    LEFT JOIN OCCUPATIONS OC ON RE.ID = OC.RESERVATION_ID
    WHERE DATE(arrival_date) >= RE.ARRIVAL_DATE AND DATE(exit_date) <= RE.EXIT_DATE);
END;
$$

/* Exemple d'appel de la procédure */
call checkRoomAvailablility(1, "2020-05-15","2020-05-18", 2)


