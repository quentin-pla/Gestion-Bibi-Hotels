package models;

import database.DatabaseColumns;

public class Occupation extends Reservation {
    /**
     * Table liée à la base de données
     */
    private Tables table = Tables.OCCUPATIONS;

    /**
     * ID de la réservation liée
     */
    private int RESERVATION_ID;

    /**
     * ID de la chambre liée
     */
    private int ROOM_ID;

    /**
     * Client présent dans la chambre
     */
    private boolean IS_CLIENT_PRESENT;

    /**
     * Liste des colonnes
     */
    private enum Columns implements DatabaseColumns {
        RESERVATION_ID,ROOM_ID,IS_CLIENT_PRESENT
    }

    /**
     * Constructeur
     */
    public Occupation() {}

    /**
     * Constructeur surchargé
     * @param reservation_id id de la réservation liée
     * @param room_id id de la chambre liée
     * @param is_client_present client présent ou non dans la chambre
     */
    public Occupation(int reservation_id, int room_id, boolean is_client_present) {
        this.RESERVATION_ID = reservation_id;
        this.ROOM_ID = room_id;
        this.IS_CLIENT_PRESENT = is_client_present;
        this.save();
    }

    //************* GETTERS & SETTERS ***************//

    @Override
    public DatabaseColumns[] getModelColumns() { return Columns.values(); }

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
