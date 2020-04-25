package models;

import database.DatabaseModel;

public class Bill implements DatabaseModel {
    //ID
    private int ID;
    //ID du client lié
    private int CLIENT_ID;
    //Montant de la facture
    private float AMOUNT;

    //Liste des colonnes
    private enum columns {
        ID,CLIENT_ID,AMOUNT
    }

    @Override
    public DatabaseModel newInstance() {
        return new Bill();
    }

    @Override
    public void setColumn(String columnItem, String value) {
        Bill.columns column = Bill.columns.valueOf(columnItem);
        switch (column) {
            case ID:
                this.setID(Integer.parseInt(value));
                break;
            case CLIENT_ID:
                this.setCLIENT_ID(Integer.parseInt(value));
                break;
            case AMOUNT:
                this.setAMOUNT(Float.parseFloat(value));
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

    public int getCLIENT_ID() {
        return CLIENT_ID;
    }

    public void setCLIENT_ID(int CLIENT_ID) {
        this.CLIENT_ID = CLIENT_ID;
    }

    public float getAMOUNT() {
        return AMOUNT;
    }

    public void setAMOUNT(float AMOUNT) {
        this.AMOUNT = AMOUNT;
    }
}
