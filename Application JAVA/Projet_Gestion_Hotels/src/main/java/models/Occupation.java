package models;

import database.DatabaseModel;

public class Occupation extends Reservation implements DatabaseModel {
    //ID
    private int ID;
    //ID de la réservation liée
    private int RESERVATION_ID;
    //ID de la chambre liée
    private int ROOM_ID;
    //Client présent dans la chambre
    private boolean IS_CLIENT_PRESENT;

    //Liste des colonnes
    private enum columns {
        ID,RESERVATION_ID,ROOM_ID,IS_CLIENT_PRESENT
    }

    @Override
    public DatabaseModel newInstance() {
        return new Occupation();
    }

    @Override
    public void setColumn(String columnItem, String value) {
        Occupation.columns column = Occupation.columns.valueOf(columnItem);
        switch (column) {
            case ID:
                this.setID(Integer.parseInt(value));
                break;
            case RESERVATION_ID:
                this.setRESERVATION_ID(Integer.parseInt(value));
                break;
            case ROOM_ID:
                this.setROOM_ID(Integer.parseInt(value));
                break;
            case IS_CLIENT_PRESENT:
                this.setIS_CLIENT_PRESENT(Boolean.parseBoolean(value));
                break;
            default:
                break;
        }
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void setID(int ID) {
        this.ID = ID;
    }

    public int getRESERVATION_ID() {
        return RESERVATION_ID;
    }

    public void setRESERVATION_ID(int RESERVATION_ID) {
        this.RESERVATION_ID = RESERVATION_ID;
    }

    public int getROOM_ID() {
        return ROOM_ID;
    }

    public void setROOM_ID(int ROOM_ID) {
        this.ROOM_ID = ROOM_ID;
    }

    public boolean isIS_CLIENT_PRESENT() {
        return IS_CLIENT_PRESENT;
    }

    public void setIS_CLIENT_PRESENT(boolean IS_CLIENT_PRESENT) {
        this.IS_CLIENT_PRESENT = IS_CLIENT_PRESENT;
    }
}
