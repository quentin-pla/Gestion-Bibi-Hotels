/** Calculer le montant total d'une réservation **/
DELIMITER $$
CREATE OR REPLACE PROCEDURE getTotalAmount(reservation_id INTEGER)
BEGIN
  DECLARE group_discount INTEGER DEFAULT getGroupDiscount();
  DECLARE regular_client_discount INTEGER DEFAULT getRegularClientDiscount();
  DECLARE total_discount INTEGER DEFAULT 0;
  DECLARE client_id INTEGER;
  DECLARE is_regular_client BOOLEAN;
  DECLARE people_count INTEGER;
  DECLARE total DOUBLE DEFAULT 0;
  SELECT ROUND((RT.PRICE*RE.DURATION*RE.ROOM_COUNT)+COALESCE(SUM(SE.PRICE),0),2), RE.PEOPLE_COUNT, RE.CLIENT_ID INTO total, people_count, client_id
  FROM RESERVATIONS RE
  JOIN ROOMTYPES RT    ON RE.ROOMTYPE_ID = RT.ID
  JOIN OCCUPATIONS OC  ON OC.RESERVATION_ID = RE.ID
  JOIN BILLEDSERVICES BS ON BS.OCCUPATION_ID = OC.ID
  JOIN SERVICES SE     ON BS.SERVICE_ID = SE.ID
  WHERE RE.ID = reservation_id;
  SELECT IS_REGULAR INTO is_regular_client FROM CLIENTS WHERE ID = client_id;
  IF (is_regular_client = 1) THEN
    SET total_discount = regular_client_discount;
  END IF;
  IF (people_count >= 3) THEN
    SET total_discount = total_discount + group_discount;
  END IF;
  SET total = ROUND(total-((total*total_discount)/100),2);
  SELECT total;
END;
$$

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
    WHERE (NOT RE.IS_ARCHIVED OR NOT RE.IS_CANCELLED)
    AND DATE(arrival_date) >= RE.ARRIVAL_DATE AND DATE(exit_date) <= RE.EXIT_DATE);
END;
$$

/* Procédure permettant d'avoir les chambres disponibles pour des dates données */
DELIMITER $$
CREATE OR REPLACE PROCEDURE getAvailableRooms(arrival_date VARCHAR(10), exit_date VARCHAR(10))
BEGIN  
  SELECT ID FROM ROOMS
  WHERE ID NOT IN (
    SELECT DISTINCT OC.ROOM_ID FROM RESERVATIONS RE
    LEFT JOIN OCCUPATIONS OC ON RE.ID = OC.RESERVATION_ID
    WHERE (NOT RE.IS_ARCHIVED OR NOT RE.IS_CANCELLED)
    AND DATE(arrival_date) >= RE.ARRIVAL_DATE AND DATE(exit_date) <= RE.EXIT_DATE);
END;
$$

/* Procédure permettant d'avoir les chambres disponibles pour un hotel donné, une sélection de jours et un type de chambre */
DELIMITER $$
CREATE OR REPLACE PROCEDURE checkRoomAvailablility(hotel_id INTEGER, arrival_date VARCHAR(10), exit_date VARCHAR(10), room_type_id INTEGER)
BEGIN
  SELECT RO.ID FROM ROOMS RO
  WHERE RO.HOTEL_ID=hotel_id AND RO.ROOMTYPE_ID=room_type_id
  AND RO.ID NOT IN (
    SELECT DISTINCT OC.ROOM_ID FROM RESERVATIONS RE
    LEFT JOIN OCCUPATIONS OC ON RE.ID = OC.RESERVATION_ID
    WHERE (NOT RE.IS_ARCHIVED OR NOT RE.IS_CANCELLED)
    AND DATE(arrival_date) >= RE.ARRIVAL_DATE AND DATE(exit_date) <= RE.EXIT_DATE);
END;
$$

