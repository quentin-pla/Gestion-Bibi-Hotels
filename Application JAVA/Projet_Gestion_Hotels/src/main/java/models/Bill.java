package models;

import database.DatabaseColumns;
import database.DatabaseData;
import database.DatabaseModel;

/**
 * Modèle facture de la base de données
 */
public class Bill extends DatabaseModel {
    /**
     * Réservation liée à la facture
     */
    private int RESERVATION_ID;

    /**
     * ID du client lié
     */
    private int CLIENT_ID;

    /**
     * Montant de la facture
     */
    private double AMOUNT;

    /**
     * Facture payée ou pas
     */
    private boolean IS_PAYED;

    /**
     * Facture archivée
     */
    private boolean IS_ARCHIVED;

    /**
     * Liste des colonnes
     */
    public enum Columns implements DatabaseColumns {
        RESERVATION_ID,CLIENT_ID,AMOUNT,IS_PAYED,IS_ARCHIVED
    }

    /**
     * Constructeur
     */
    public Bill() {
        super(Tables.BILLS);
    }

    //Constructeur surchargé
    public Bill(int RESERVATION_ID, int CLIENT_ID, double AMOUNT,boolean IS_PAYED, boolean IS_ARCHIVED) {
        super(Tables.BILLS);
        this.RESERVATION_ID = RESERVATION_ID;
        this.CLIENT_ID = CLIENT_ID;
        this.AMOUNT = AMOUNT;
        this.IS_PAYED = IS_PAYED;
        this.IS_ARCHIVED = IS_ARCHIVED;
        this.save();
    }

    //************* REFERENCES ***************//

    /**
     * Réservation liée
     * @return réservation
     */
    public Reservation getReservation() {
        return (Reservation) DatabaseData.getInstance().getReferenceFromID(Tables.RESERVATIONS,RESERVATION_ID);
    }

    /**
     * Client lié
     * @return client
     */
    public Client getClient() {
        return (Client) DatabaseData.getInstance().getReferenceFromID(Tables.CLIENTS,CLIENT_ID);
    }

    //************* GETTERS & SETTERS ***************//

    @Override
    public DatabaseColumns[] getColumns() {
        return Columns.values();
    }

    public int getRESERVATION_ID() {
        return RESERVATION_ID;
    }

    public void setRESERVATION_ID(int RESERVATION_ID) {
        this.RESERVATION_ID = RESERVATION_ID;
    }

    public int getCLIENT_ID() {
        return CLIENT_ID;
    }

    public void setCLIENT_ID(int CLIENT_ID) {
        this.CLIENT_ID = CLIENT_ID;
    }

    public double getAMOUNT() {
        return AMOUNT;
    }

    public void setAMOUNT(double AMOUNT) {
        this.AMOUNT = AMOUNT;
    }

    public boolean getIS_PAYED() {
        return IS_PAYED;
    }

    public void setIS_PAYED(boolean IS_PAYED) {
        this.IS_PAYED = IS_PAYED;
    }

    public boolean getIS_ARCHIVED() {
        return IS_ARCHIVED;
    }

    public void setIS_ARCHIVED(boolean IS_ARCHIVED) {
        this.IS_ARCHIVED = IS_ARCHIVED;
    }
}
