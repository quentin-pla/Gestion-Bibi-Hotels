package models;

import database.DatabaseModel;

public class Room implements DatabaseModel {
    //ID
    private int ID;
    //ID de l'hotel lié
    private int HOTEL_ID;
    //ID type de chambre lié
    private int ROOMTYPE_ID;

    //Liste des colonnes
    private enum columns {
        ID,HOTEL_ID,ROOMTYPE_ID
    }

    @Override
    public DatabaseModel newInstance() {
        return new Room();
    }

    @Override
    public void setColumn(String columnItem, String value) {
        Room.columns column = Room.columns.valueOf(columnItem);
        switch (column) {
            case ID:
                this.setID(Integer.parseInt(value));
                break;
            case HOTEL_ID:
                this.setHOTEL_ID(Integer.parseInt(value));
                break;
            case ROOMTYPE_ID:
                this.setROOMTYPE_ID(Integer.parseInt(value));
                break;
            default:
                break;
        }
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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
