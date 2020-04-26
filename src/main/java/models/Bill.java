package models;

import database.DatabaseColumns;
import database.DatabaseModel;

public class Bill extends DatabaseModel {
    /**
     * Table liée à la base de données
     */
    private Tables table = Tables.BILLS;

    /**
     * ID du client lié
     */
    private int CLIENT_ID;

    /**
     * Montant de la facture
     */
    private float AMOUNT;

    /**
     * Liste des colonnes
     */
    private enum Columns implements DatabaseColumns {
        CLIENT_ID,AMOUNT
    }

    /**
     * Constructeur
     */
    public Bill() {}

    //Constructeur surchargé
    public Bill(int client_id, float amount) {
        this.CLIENT_ID = client_id;
        this.AMOUNT = amount;
        this.save();
    }

    //************* GETTERS & SETTERS ***************//

    @Override
    public DatabaseColumns[] getModelColumns() {
        return Columns.values();
    }

    public int getCLIENT_ID() {
        return CLIENT_ID;
    }

    public void setCLIENT_ID(String CLIENT_ID) { this.CLIENT_ID = Integer.parseInt(CLIENT_ID); }

    public float getAMOUNT() {
        return AMOUNT;
    }

    public void setAMOUNT(String AMOUNT) { this.AMOUNT = Float.parseFloat(AMOUNT); }
}
