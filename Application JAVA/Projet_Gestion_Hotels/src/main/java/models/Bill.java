package models;

import database.DatabaseColumns;
import database.DatabaseModel;

public class Bill extends DatabaseModel {
    //ID du client lié
    private int CLIENT_ID;
    //Montant de la facture
    private float AMOUNT;

    //Liste des colonnes
    private enum Columns implements DatabaseColumns {
        CLIENT_ID,AMOUNT
    }

    //Constructeur
    public Bill() {
        this.table = Tables.BILLS;
    }

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
