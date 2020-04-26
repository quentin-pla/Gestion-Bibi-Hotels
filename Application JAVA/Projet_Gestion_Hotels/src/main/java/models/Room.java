package models;

import database.DatabaseColumns;
import database.DatabaseModel;

public class Room extends DatabaseModel {
    //ID de l'hotel lié
    private int HOTEL_ID;
    //ID type de chambre lié
    private int ROOMTYPE_ID;

    //Liste des colonnes
    private enum Columns implements DatabaseColumns {
        HOTEL_ID,ROOMTYPE_ID
    }

    //Constructeur
    public Room() {
        this.table = Tables.ROOMS;
    }

    @Override
    public DatabaseColumns[] getModelColumns() {
        return Columns.values();
    }

    public int getHOTEL_ID() {
        return HOTEL_ID;
    }

    public void setHOTEL_ID(String HOTEL_ID) {
        this.HOTEL_ID = Integer.parseInt(HOTEL_ID);
    }

    public int getROOMTYPE_ID() {
        return ROOMTYPE_ID;
    }

    public void setROOMTYPE_ID(String ROOMTYPE_ID) {
        this.ROOMTYPE_ID = Integer.parseInt(ROOMTYPE_ID);
    }
}
