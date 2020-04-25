package models;

import database.DatabaseColumns;
import database.DatabaseModel;

public class Bill extends DatabaseModel {
    //ID
    private int ID;
    //ID du client lié
    private int CLIENT_ID;
    //Montant de la facture
    private float AMOUNT;

    //Liste des colonnes
    private enum Columns implements DatabaseColumns {
        ID,CLIENT_ID,AMOUNT
    }

    @Override
    public DatabaseColumns[] getModelColumns() {
        return Columns.values();
    }

    @Override
    public DatabaseModel newInstance() {
        return new Bill();
    }

    public int getID() {
        return ID;
    }

    public void setID(String ID) { this.ID = Integer.parseInt(ID); }

    public int getCLIENT_ID() {
        return CLIENT_ID;
    }

    public void setCLIENT_ID(String CLIENT_ID) { this.CLIENT_ID = Integer.parseInt(CLIENT_ID); }

    public float getAMOUNT() {
        return AMOUNT;
    }

    public void setAMOUNT(String AMOUNT) { this.AMOUNT = Float.parseFloat(AMOUNT); }
}
