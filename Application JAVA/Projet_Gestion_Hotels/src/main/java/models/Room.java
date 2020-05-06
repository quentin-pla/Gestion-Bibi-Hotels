package models;

import database.DatabaseColumns;
import database.DatabaseData;
import database.DatabaseModel;

/**
 * Modèle Chambre depuis la base de données
 */
public class Room extends DatabaseModel {
    /**
     * ID de l'hotel lié
     */
    private int HOTEL_ID;

    /**
     * ID type de chambre lié
     */
    private int ROOMTYPE_ID;

    /**
     * Liste des colonnes
     */
    public enum Columns implements DatabaseColumns {
        HOTEL_ID,ROOMTYPE_ID
    }

    /**
     * Constructeur
     */
    public Room() {
        super(Tables.ROOMS);
    }

    /**
     * Constructeur surchargé
     * @param HOTEL_ID id de l'hotel lié
     * @param ROOMTYPE_ID id du type de chambre lié
     */
    public Room(int HOTEL_ID, int ROOMTYPE_ID) {
        super(Tables.ROOMS);
        this.HOTEL_ID = HOTEL_ID;
        this.ROOMTYPE_ID = ROOMTYPE_ID;
        this.save();
    }

    //************* REFERENCES ***************//

    /**
     * Hotel lié
     * @return hotel
     */
    public Hotel getHotel() {
        return (Hotel) DatabaseData.getInstance().getReferenceFromID(Tables.HOTELS,HOTEL_ID);
    }

    /**
     * Type de chambre lié
     * @return type de chambre
     */
    public RoomType getRoomType() {
        return (RoomType) DatabaseData.getInstance().getReferenceFromID(Tables.ROOMTYPES,ROOMTYPE_ID);
    }

    //************* GETTERS & SETTERS ***************//

    @Override
    public DatabaseColumns[] getColumns() {
        return Columns.values();
    }

    public int getHOTEL_ID() {
        return HOTEL_ID;
    }

    public void setHOTEL_ID(int HOTEL_ID) {
        this.HOTEL_ID = HOTEL_ID;
    }

    public int getROOMTYPE_ID() {
        return ROOMTYPE_ID;
    }

    public void setROOMTYPE_ID(int ROOMTYPE_ID) {
        this.ROOMTYPE_ID = ROOMTYPE_ID;
    }
}
