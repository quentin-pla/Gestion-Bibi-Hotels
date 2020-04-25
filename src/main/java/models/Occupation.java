package models;

import com.sun.org.apache.xpath.internal.operations.Bool;
import database.DatabaseColumns;
import database.DatabaseModel;

public class Occupation extends Reservation {
    //ID de la réservation liée
    private int RESERVATION_ID;
    //ID de la chambre liée
    private int ROOM_ID;
    //Client présent dans la chambre
    private boolean IS_CLIENT_PRESENT;

    //Liste des colonnes
    private enum Columns implements DatabaseColumns {
        ID,RESERVATION_ID,ROOM_ID,IS_CLIENT_PRESENT
    }

    @Override
    public DatabaseColumns[] getModelColumns() {
        return Columns.values();
    }

    @Override
    public DatabaseModel newInstance() {
        return new Occupation();
    }

    public int getRESERVATION_ID() {
        return RESERVATION_ID;
    }

    public void setRESERVATION_ID(String RESERVATION_ID) {
        this.RESERVATION_ID = Integer.parseInt(RESERVATION_ID);
    }

    public int getROOM_ID() {
        return ROOM_ID;
    }

    public void setROOM_ID(String ROOM_ID) {
        this.ROOM_ID = Integer.parseInt(ROOM_ID);
    }

    public boolean isIS_CLIENT_PRESENT() {
        return IS_CLIENT_PRESENT;
    }

    public void setIS_CLIENT_PRESENT(String IS_CLIENT_PRESENT) { this.IS_CLIENT_PRESENT = Boolean.parseBoolean(IS_CLIENT_PRESENT); }
}
