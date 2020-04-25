package models;

import database.DatabaseColumns;
import database.DatabaseModel;

public class Room extends DatabaseModel {
    //ID
    private int ID;
    //ID de l'hotel lié
    private int HOTEL_ID;
    //ID type de chambre lié
    private int ROOMTYPE_ID;

    //Liste des colonnes
    private enum Columns implements DatabaseColumns {
        ID,HOTEL_ID,ROOMTYPE_ID
    }

    @Override
    public DatabaseColumns[] getModelColumns() {
        return Columns.values();
    }

    @Override
    public DatabaseModel newInstance() {
        return new Room();
    }

    public int getID() {
        return ID;
    }

    public void setID(String ID) { this.ID = Integer.parseInt(ID); }

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
